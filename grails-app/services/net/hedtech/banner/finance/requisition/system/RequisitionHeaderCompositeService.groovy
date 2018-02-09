/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Class for Purchase Requisition Header Composite Service
 */
class RequisitionHeaderCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    boolean transactional = true

    def requisitionHeaderService
    def requisitionAccountingService
    def springSecurityService
    def financeSystemControlService
    def financeTextService
    def financeTextCompositeService
    def requisitionDetailService
    def documentManagementCompositeService
    def requisitionDetailsCompositeService
    def requisitionAccountingCompositeService

    /**
     * Create purchase requisition Header
     * @param map Map which contains the RequisitionHeader domain with values.
     * @return Requisition code.
     */
    def createPurchaseRequisitionHeader( map ) {
        RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
        def user = springSecurityService.getAuthentication().user
        if (user.oracleUserName) {
            def oracleUserName = user.oracleUserName
            requisitionHeaderRequest.userId = oracleUserName
            // Check for tax group
            if (financeSystemControlService.findActiveFinanceSystemControl().taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO) {
                requisitionHeaderRequest.taxGroup = null
            }
            def requisitionHeader = requisitionHeaderService.create( [domainModel: requisitionHeaderRequest] )
            LoggerUtility.debug LOGGER, "Requisition Header created " + requisitionHeader
            def header = RequisitionHeader.read( requisitionHeader.id )
            financeTextCompositeService.saveTextForHeader( requisitionHeader,
                                                           [privateComment: map.requisitionHeader.privateComment, publicComment: map.requisitionHeader.publicComment],
                                                           user.oracleUserName )
            return header.requestCode
        } else {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Delete Purchase Requisition
     * @param requestCode
     */
    def deletePurchaseRequisitionHeader( requestCode ) {
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeader )
        requisitionHeaderService.delete( [domainModel: requisitionHeader] )
        financeTextService.delete( financeTextService.listAllFinanceTextByCode( requestCode ) )
    }

    /**
     * Update Purchase requisition
     *
     * @param map the requisition map
     * @param requestCode
     */
    def updateRequisitionHeader( map, requestCode, baseCcy ) {
        // Update header
        def user = springSecurityService.getAuthentication().user
        if (map?.requisitionHeader && user.oracleUserName) {
            def existingHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
            map.requisitionHeader.requestCode = existingHeader.requestCode
            if (!checkHeaderUpdateEligibility( map, existingHeader, baseCcy )) {
                LoggerUtility.debug( LOGGER, 'Modification not required' )
                return existingHeader
            }
            FinanceProcurementHelper.checkCompleteRequisition( existingHeader )
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            boolean isDiscountChanged = requisitionHeaderRequest.discount && requisitionHeaderRequest.discount != existingHeader.discount
            boolean isCcyChanged = requisitionHeaderRequest.currency != existingHeader.currency
            requisitionHeaderRequest.id = existingHeader.id
            requisitionHeaderRequest.version = existingHeader.version
            requisitionHeaderRequest.requestCode = existingHeader.requestCode
            requisitionHeaderRequest.documentCopiedFrom = existingHeader.documentCopiedFrom
            def accountSize = requisitionAccountingService.findAccountingSizeByRequestCode( existingHeader.requestCode )
            boolean checkUpdateAccountRequire = checkAccountUpdateEligibility( map, existingHeader )
            if (requisitionHeaderRequest.isDocumentLevelAccounting != existingHeader.isDocumentLevelAccounting && accountSize > 0) {
                LoggerUtility.error( LOGGER, 'Document type cannot be modified once accounting associated with this' )
                throw new ApplicationException( RequisitionHeaderCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_DOCUMENT_CHANGE, [] ) )
            }
            requisitionHeaderRequest.userId = user.oracleUserName
            def requisitionHeader = requisitionHeaderService.update( [domainModel: requisitionHeaderRequest] )

            // updating the account sequences with valid fiscal year and period
            if( accountSize > 0 && checkUpdateAccountRequire){
                    requisitionAccountingService.findAccountingByRequestCode(requestCode).each {RequisitionAccounting requisitionAccounting ->
                    def account = null
                    account = requisitionAccounting
                    account.fiscalYear = null
                    account.period = null
                    requisitionAccountingService.update([domainModel: account])
                }
            }

            LoggerUtility.debug LOGGER, "Requisition Header updated " + requisitionHeader
            financeTextCompositeService.saveTextForHeader( requisitionHeader,
                                                           [privateComment: map.requisitionHeader.privateComment, publicComment: map.requisitionHeader.publicComment],
                                                           user.oracleUserName )
            if (isDiscountChanged || isCcyChanged) {
                reCalculateCommodities( requisitionHeader, isDiscountChanged, isCcyChanged )
            }

            return requisitionHeader
        } else {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionHeaderCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Get Filtered CCY information
     * @param ccyCode
     * @return
     */
    def getCurrencyDetailByReqCode( requestCode, baseCurrCode ) {
        def currencyCode = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ).currency
        currencyCode ? currencyCode : baseCurrCode
    }

    /**
     * Delete Purchase requisition
     * @param requestCode
     * @param forceDelete
     * @param mep
     * @param isBDMInstalled
     * @return
     */
    def deletePurchaseRequisition( requestCode, boolean forceDelete, mep, boolean isBDMInstalled ) {
        if (!forceDelete && isBDMInstalled) {
            if (documentManagementCompositeService.listDocumentsByRequisitionCode( requestCode, mep, isBDMInstalled )?.documentList?.size() > 0) {
                LoggerUtility.info( LOGGER, "Documents are present for this requisition. You may need to remove them before deleting this requisition" + requestCode )
                throw new ApplicationException(
                        RequisitionHeaderCompositeService,
                        new BusinessLogicValidationException( 'WARNING', [] ) )
            }
        }
        def requestHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if ((!requestHeader.completeIndicator)
                || (requestHeader.completeIndicator == null && !requestHeader.approvalIndicator)) {
            // Delete Accounting
            requisitionAccountingService.delete( requisitionAccountingService.findAccountingByRequestCode( requestCode ) )
            // Delete Detail
            requisitionDetailService.delete( requisitionDetailService.findDetailsRequestCode( requestCode ) )

            // Delete Header
            requisitionHeaderService.delete( requestHeader )
            return requestCode
        } else {
            LoggerUtility.error( LOGGER, "Only Draft/Disapproved requisition can be deleted = $requestCode" )
            throw new ApplicationException(
                    RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_DELETE_REQUISITION_DRAFT_OR_DISAPPROVED_REQ_IS_REQUIRED, [] ) )
        }
    }
    /**
     * Check if header's text is modified and save to db is required
     *
     * @param privateComment
     * @param publicComment
     * @param requestCode
     * @return
     */
    private boolean isCommentUnChanged( privateComment, publicComment, requestCode ) {
        def existingPrivateComment = FinanceProcurementConstants.EMPTY_STRING
        def existingPublicComment = FinanceProcurementConstants.EMPTY_STRING
        financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd( requestCode, FinanceValidationConstants.REQUISITION_INDICATOR_NO ).each {
            existingPrivateComment = existingPrivateComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
        }
        financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd( requestCode, FinanceValidationConstants.REQUISITION_INDICATOR_YES ).each {
            existingPublicComment = existingPublicComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
        }
        existingPublicComment == publicComment && existingPrivateComment == privateComment
    }

    /**
     * Check if header is modified and save to db is required
     * @param map
     * @param existingHeader
     * @return
     */
    private boolean checkHeaderUpdateEligibility( def map, RequisitionHeader existingHeader, baseCcy ) {
        RequisitionHeader newHeader = map.requisitionHeader
        return !(new java.sql.Date( newHeader.transactionDate.getTime() ) == existingHeader.transactionDate &&
                new java.sql.Date( newHeader.deliveryDate.getTime() ) == existingHeader.deliveryDate &&
                newHeader.requesterName == existingHeader.requesterName &&
                newHeader.ship == existingHeader.ship &&
                newHeader.vendorPidm == existingHeader.vendorPidm &&
                newHeader.vendorAddressType == existingHeader.vendorAddressType &&
                newHeader.vendorAddressTypeSequence == existingHeader.vendorAddressTypeSequence &&
                newHeader.chartOfAccount == existingHeader.chartOfAccount &&
                newHeader.organization == existingHeader.organization &&
                newHeader.attentionTo == existingHeader.attentionTo &&
                newHeader.isDocumentLevelAccounting == existingHeader.isDocumentLevelAccounting &&
                newHeader.deliveryComment == existingHeader.deliveryComment &&
                newHeader.taxGroup == existingHeader.taxGroup &&
                newHeader.discount == existingHeader.discount &&
                newHeader.requesterEmailAddress == existingHeader.requesterEmailAddress &&
                newHeader.vendorEmailAddress == existingHeader.vendorEmailAddress &&
                ((newHeader.currency == baseCcy && existingHeader.currency == null) || newHeader.currency == existingHeader.currency) &&
                isCommentUnChanged( map.requisitionHeader.privateComment, map.requisitionHeader.publicComment, newHeader.requestCode ))
    }

    /**
     * Recalculate the discount and converted amount if discount code or ccy changed
     * @param requisitionHeader
     * @param isDiscountChanged
     * @param isCcyChanged
     * @return
     */
    private def reCalculateCommodities( RequisitionHeader requisitionHeader, isDiscountChanged, isCcyChanged ) {
        try {
            def detailList = requisitionDetailService.findDetailsRequestCode( requisitionHeader.requestCode )
            if(detailList.size() > 0 ) {
                detailList.each { item ->
                    def requisitionDetailModel = item.class.declaredFields.findAll {
                        it.modifiers == java.lang.reflect.Modifier.PRIVATE
                    }.collectEntries { [it.name, item[it.name]] }
                    if (isDiscountChanged) {
                        requisitionDetailModel.discountAmount = null
                    }
                    if (isCcyChanged) {
                        requisitionDetailModel.convertedDiscountAmount = null
                    }
                    def detailDomainModel = [requisitionDetail: requisitionDetailModel]
                    requisitionDetailsCompositeService.updateRequisitionDetail(detailDomainModel)
                }
            }
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, 'Requisition Commodity Details are empty for requestCode=' + requisitionHeader.requestCode + ' and commodity recalculation is not performed' )
        }
    }

    /**
     * Check if Account Lines are modified and save to db is required
     * @param map
     * @param existingHeader
     * @return
     */
    private boolean checkAccountUpdateEligibility( map, RequisitionHeader existingHeader ) {
        RequisitionHeader newHeader = map.requisitionHeader
        return (new java.sql.Date(newHeader.transactionDate.getTime()) != existingHeader.transactionDate)
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
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
    def institutionalDescriptionService
    def financeTextService
    def financeTextCompositeService

    /**
     * Create purchase requisition Header
     * @param map Map which contains the RequisitionHeader domain with values.
     * @return Requisition code.
     */
    def createPurchaseRequisitionHeader( map ) {
        RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            requisitionHeaderRequest.userId = oracleUserName
            // Check for tax group
            if (financeSystemControlService.findActiveFinanceSystemControl()?.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO) {
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
    def deletePurchaseRequisition( requestCode ) {
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeader )
        requisitionHeaderService.delete( [domainModel: requisitionHeader] )
        financeTextService.delete( financeTextService.listAllFinanceTextByCode( requestCode ) )
    }

    /**
     *
     * @param privateComment
     * @param publicComment
     * @param requestCode
     * @return
     */
    private boolean isCommentModified( privateComment, publicComment, requestCode ) {
        def existingPrivateComment = FinanceProcurementConstants.EMPTY_STRING
        def existingPublicComment = FinanceProcurementConstants.EMPTY_STRING
        financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd( requestCode, FinanceValidationConstants.REQUISITION_INDICATOR_NO ).each {
            existingPrivateComment = existingPrivateComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
        }
        if (existingPrivateComment == privateComment) {
            return true
        }
        financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd( requestCode, FinanceValidationConstants.REQUISITION_INDICATOR_YES ).each {
            existingPublicComment = existingPublicComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
        }
        return existingPublicComment == publicComment
    }

    /**
     *
     * @param map
     * @param existingHeader
     * @return
     */
    private boolean checkHeaderUpdateEligibility( def map, RequisitionHeader existingHeader ) {
        RequisitionHeader newHeader = map.requisitionHeader
        if (new java.sql.Date( newHeader.transactionDate.getTime() ) == existingHeader.transactionDate &&
                new java.sql.Date( newHeader.deliveryDate.getTime() ) == existingHeader.deliveryDate &&
                newHeader.requesterName == existingHeader.requesterName &&
                newHeader.ship == existingHeader.ship &&
                newHeader.vendorPidm == existingHeader.vendorPidm &&
                newHeader.vendorAddressType == existingHeader.vendorAddressType &&
                newHeader.vendorAddressTypeSequence == existingHeader.vendorAddressTypeSequence &&
                newHeader.chartOfAccount == existingHeader.chartOfAccount &&
                newHeader.organization == existingHeader.organization &&
                newHeader.deliveryDate == existingHeader.deliveryDate &&
                newHeader.attentionTo == existingHeader.attentionTo &&
                newHeader.isDocumentLevelAccounting == existingHeader.isDocumentLevelAccounting &&
                newHeader.deliveryComment == existingHeader.deliveryComment &&
                newHeader.taxGroup == existingHeader.taxGroup &&
                newHeader.discount == existingHeader.discount &&
                newHeader.currency == existingHeader.currency && isCommentModified( map.requisitionHeader.privateComment, map.requisitionHeader.publicComment, newHeader.requestCode )

        ) {
            LoggerUtility.debug( LOGGER, 'Modification not required' )
        }
    }
    /**
     * Update Purchase requisition
     *
     * @param map the requisition map
     * @param requestCode
     */
    @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
    def updateRequisitionHeader( map, requestCode ) {
        // Update header
        def existingHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (!checkHeaderUpdateEligibility( map, existingHeader )) {
            LoggerUtility.debug( LOGGER, 'Modification not required' )
            //return existingHeader //TODO Need complete implementation
        }
        FinanceProcurementHelper.checkCompleteRequisition( existingHeader )
        def user = springSecurityService.getAuthentication()?.user
        if (map?.requisitionHeader && user?.oracleUserName) {
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            requisitionHeaderRequest.id = existingHeader.id
            requisitionHeaderRequest.version = existingHeader.version
            requisitionHeaderRequest.requestCode = existingHeader.requestCode
            if (requisitionHeaderRequest.isDocumentLevelAccounting != existingHeader.isDocumentLevelAccounting) {

                if (requisitionAccountingService.findAccountingSizeByRequestCode( existingHeader.requestCode ) > 0) {
                    LoggerUtility.error( LOGGER, 'Document type cannot be modified once accounting associated with this' )
                    throw new ApplicationException( RequisitionHeaderCompositeService,
                                                    new BusinessLogicValidationException(
                                                            FinanceProcurementConstants.ERROR_MESSAGE_DOCUMENT_CHANGE, [] ) )
                }
            }
            requisitionHeaderRequest.requestDate = existingHeader.requestDate
            requisitionHeaderRequest.userId = user?.oracleUserName
            def requisitionHeader = requisitionHeaderService.update( [domainModel: requisitionHeaderRequest] )
            LoggerUtility.debug LOGGER, "Requisition Header updated " + requisitionHeader
            financeTextCompositeService.saveTextForHeader( requisitionHeader,
                                                           [privateComment: map.requisitionHeader.privateComment, publicComment: map.requisitionHeader.publicComment],
                                                           user.oracleUserName )
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
    def getCurrencyDetailByReqCode( requestCode ) {
        def currencyCode = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ).currency
        if (!currencyCode) {
            currencyCode = institutionalDescriptionService.findByKey().baseCurrCode
        }
        return currencyCode
    }

}

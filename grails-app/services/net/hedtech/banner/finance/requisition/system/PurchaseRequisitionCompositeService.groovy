/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.system.FinanceSystemControl
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Composite
 */
class PurchaseRequisitionCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    boolean transactional = true

    def requisitionHeaderService
    def springSecurityService
    def requisitionDetailService
    def financeSystemControlService
    def financeCommodityService
    def requisitionInformationService
    def requisitionAccountingService

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
            FinanceSystemControl financeSystemControl = financeSystemControlService.findActiveFinanceSystemControl()
            if (financeSystemControl?.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO) {
                requisitionHeaderRequest.taxGroup = null
            }
            def requisitionHeader = requisitionHeaderService.create( [domainModel: requisitionHeaderRequest] )
            LOGGER.debug "Requisition Header created " + requisitionHeader
            def header = RequisitionHeader.read( requisitionHeader.id )
            return header.requestCode
        } else {
            LOGGER.error( 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Create purchase requisition detail
     * @param map Map which contains the RequisitionDetail domain with values.
     * @return requestCode and item number.
     */
    def createPurchaseRequisitionDetail( map ) {
        RequisitionDetail requisitionDetailRequest = map.requisitionDetail
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            def requestCode = requisitionDetailRequest.requestCode
            def lastItem = requisitionDetailService.getLastItem( requestCode )
            requisitionDetailRequest.userId = oracleUserName
            requisitionDetailRequest.item = lastItem.next()
            // Set all data with business logic.
            requisitionDetailRequest = setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest )
            RequisitionDetail requisitionDetail = requisitionDetailService.create( [domainModel: requisitionDetailRequest] )
            LOGGER.debug "Requisition Detail created " + requisitionDetail
            def requisitionDetailMap = [:]
            requisitionDetailMap.requestCode = requisitionDetail.requestCode
            requisitionDetailMap.item = requisitionDetail.item
            return requisitionDetailMap
        } else {
            LOGGER.error( 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Delete Purchase Requisition
     * @param requestCode
     */
    def deletePurchaseRequisition( requestCode ) {
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        requisitionHeaderService.delete( [domainModel: requisitionHeader] )
    }

    /**
     * Update Purchase requisition
     *
     * @param map the requisition map
     * @param requestCode
     */
    def updateRequisitionHeader( map, requestCode ) {
        // Update header
        def existingHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (map?.requisitionHeader) {
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            requisitionHeaderRequest.id = existingHeader.id
            requisitionHeaderRequest.version = existingHeader.version
            requisitionHeaderRequest.requestCode = existingHeader.requestCode
            def user = springSecurityService.getAuthentication()?.user
            if (user.oracleUserName) {
                def oracleUserName = user?.oracleUserName
                requisitionHeaderRequest.userId = oracleUserName
                def requisitionHeader = requisitionHeaderService.update( [domainModel: requisitionHeaderRequest] )
                LOGGER.debug "Requisition Header updated " + requisitionHeader
                def header = RequisitionHeader.read( requisitionHeader.id )
                return header
            } else {
                LOGGER.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

    /**
     * Delete Purchase Requisition Detail.
     * @param requestCode Requisition Code.
     * @param item Item.
     */
    def deletePurchaseRequisitionDetail( requestCode, Integer item ) {
        def requisitionDetail = requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( requestCode, item )
        requisitionDetailService.delete( [domainModel: requisitionDetail] )
    }

    /**
     * Update Purchase requisition detail commodity level.
     *
     * @param map the requisition detail map
     * @param requestCode
     */
    def updateRequisitionDetail( detailDomainModel, requestCode, item ) {
        // Null or empty check for item.
        if (!item) {
            LOGGER.error( 'Item is required to update the detail.' )
            throw new ApplicationException( PurchaseRequisitionCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED, [] ) )
        }
        def existingDetail = requisitionDetailService.findByRequestCodeAndItem( requestCode, item )
        if (detailDomainModel?.requisitionDetail) {
            RequisitionDetail requisitionDetailRequest = detailDomainModel.requisitionDetail
            requisitionDetailRequest.id = existingDetail.id
            requisitionDetailRequest.version = existingDetail.version
            requisitionDetailRequest.requestCode = existingDetail.requestCode
            def user = springSecurityService.getAuthentication()?.user
            if (user.oracleUserName) {
                requisitionDetailRequest = setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest )
                requisitionDetailRequest.lastModified = new Date()
                requisitionDetailRequest.item = existingDetail.item
                requisitionDetailRequest.userId = user.oracleUserName
                def requisitionDetail = requisitionDetailService.update( [domainModel: requisitionDetailRequest] )
                LOGGER.debug "Requisition Detail updated " + requisitionDetail
                def detail = RequisitionDetail.read( requisitionDetail.id )
                return detail
            } else {
                LOGGER.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

    /**
     * Service method to create Requisition Accounting level.
     * @param map Map which have all the required date to create requisition accounting.
     * @return map Map having requestCode, item and sequenceNumber of created requisition accounting.
     */
    def createPurchaseRequisitionAccounting( map ) {
        RequisitionAccounting requisitionAccountingRequest = map.requisitionAccounting
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            def requestCode = requisitionAccountingRequest.requestCode
            def lastSequenceNumber = requisitionAccountingService.getLastSequenceNumberByRequestCode( requestCode )
            requisitionAccountingRequest.userId = oracleUserName
            requisitionAccountingRequest.sequenceNumber = lastSequenceNumber.next()
            def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
            if (requisitionHeader && requisitionHeader.isDocumentLevelAccounting) {
                requisitionAccountingRequest.item = 0
            } else if (requisitionHeader && !requisitionHeader.isDocumentLevelAccounting) {
                def lastItem = requisitionAccountingService.getLastItemNumberByRequestCode( requestCode )
                requisitionAccountingRequest.item = lastItem.next()
            }
            RequisitionAccounting requisitionAccounting = requisitionAccountingService.create( [domainModel: requisitionAccountingRequest] )
            LOGGER.debug "Requisition Accounting created " + requisitionAccounting
            def requisitionAccountingMap = [:]
            requisitionAccountingMap.requestCode = requisitionAccounting.requestCode
            requisitionAccountingMap.item = requisitionAccounting.item
            requisitionAccountingMap.sequenceNumber = requisitionAccounting.sequenceNumber
            return requisitionAccountingMap
        } else {
            LOGGER.error( 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Delete Purchase Requisition Accounting Information.
     * @param requestCode Requisition Code.
     * @param item Item.
     * @param sequenceNumber Sequence number.
     */
    def deletePurchaseRequisitionAccountingInformation( requestCode, Integer item, Integer sequenceNumber ) {
        def requisitionAccounting = requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequenceNumber )
        requisitionAccountingService.delete( [domainModel: requisitionAccounting] )
    }

    /**
     * Update Purchase requisition Accounting information.
     *
     * @param map the requisition accounting map
     * @param requestCode
     */
    def updateRequisitionAccounting( accountingDomainModel, requestCode, Integer item, Integer sequenceNumber ) {
        // Null or empty check for item number and sequence number.
        if (item == null || sequenceNumber == null) {
            LOGGER.error( 'Item and Sequence number are required to update the Requisition Accounting information.' )
            throw new ApplicationException( PurchaseRequisitionCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED, [] ) )
        }
        RequisitionAccounting existingAccountingInfo = requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequenceNumber )
        if (accountingDomainModel?.requisitionAccounting) {
            RequisitionAccounting requisitionAccountingRequest = accountingDomainModel.requisitionAccounting
            requisitionAccountingRequest.id = existingAccountingInfo.id
            requisitionAccountingRequest.version = existingAccountingInfo.version
            requisitionAccountingRequest.requestCode = existingAccountingInfo.requestCode
            def user = springSecurityService.getAuthentication()?.user
            if (user.oracleUserName) {
                requisitionAccountingRequest.lastModified = new Date()
                requisitionAccountingRequest.item = existingAccountingInfo.item
                requisitionAccountingRequest.sequenceNumber = existingAccountingInfo.sequenceNumber
                requisitionAccountingRequest.userId = user.oracleUserName
                def requisitionAccounting = requisitionAccountingService.update( [domainModel: requisitionAccountingRequest] )
                LOGGER.debug "Requisition Accounting information updated " + requisitionAccounting
                def detail = RequisitionAccounting.read( requisitionAccounting.id )
                return detail
            } else {
                LOGGER.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

    /**
     * Returns list of Requisitions in defined data structure
     * @param buckets
     */
    def listRequisitionsByBucket( buckets, pagingParams ) {
        def user = springSecurityService.getAuthentication()?.user
        if (user == null || user.oracleUserName == null) {
            LOGGER.error( 'User' + user + ' is not valid' )
            throw new ApplicationException( PurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        def wrapperList = [];
        if (buckets?.isEmpty()) {
            buckets = [FinanceProcurementConstants.REQUISITION_LIST_BUCKET_ALL]
        }
        buckets = new ArrayList( buckets ).unique()
        buckets.each() {bucket ->
            processBucket wrapperList, bucket, pagingParams, user.oracleUserName
        }
        return wrapperList
    }

    /**
     * Process each Bucket
     *
     * @param wrapperList
     * @param bucket
     * @param pagingParams
     * @return
     */
    private def processBucket( wrapperList, bucket, pagingParams, user ) {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_LIST_STATUS_DRAFT, FinanceProcurementConstants.REQUISITION_LIST_STATUS_DISAPPROVED]

        def completedStatus = [FinanceProcurementConstants.REQUISITION_LIST_STATUS_COMPLETED, FinanceProcurementConstants.REQUISITION_LIST_STATUS_BUYER_ASSIGNED,
                               FinanceProcurementConstants.REQUISITION_LIST_STATUS_CONVERTED_TO_PO]

        def pendingStatus = [FinanceProcurementConstants.REQUISITION_LIST_STATUS_PENDING]
        switch (bucket) {
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_ALL:
                wrapperList.add( groupResult( FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT,
                                              listRequisitions( user, pagingParams, draftStatus ) ) )
                wrapperList.add( groupResult( FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING,
                                              listRequisitions( user, pagingParams, pendingStatus ) ) )
                wrapperList.add( groupResult( FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE,
                                              listRequisitions( user, pagingParams, completedStatus ) ) )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT:
                wrapperList.add( groupResult( bucket, listRequisitions( user, pagingParams, draftStatus ) ) )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING:
                wrapperList.add( groupResult( bucket, listRequisitions( user, pagingParams, pendingStatus ) ) )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE:
                wrapperList.add( groupResult( bucket, listRequisitions( user, pagingParams, completedStatus ) ) )
                break
            default:
                LOGGER.error( 'Group Type not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_INVALID_BUCKET_TYPE, [bucket] ) )
        }
    }

    /**
     * This method is used to set data for Create/Update requisition detail
     * @param requestCode Requisition Code.
     * @param requisitionDetailRequest Requisition details.
     * @return updated requisition details.
     */
    private def setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest ) {
        // Set all the required information from the Requisition Header.
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        requisitionDetailRequest.chartOfAccount = requisitionHeader.chartOfAccount
        requisitionDetailRequest.organization = requisitionHeader.organization
        requisitionDetailRequest.ship = requisitionHeader.ship
        requisitionDetailRequest.deliveryDate = requisitionHeader.deliveryDate
        // start check tax amount.
        FinanceSystemControl financeSystemControl = financeSystemControlService.findActiveFinanceSystemControl()
        if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO) {
            requisitionDetailRequest.taxGroup = null
        } else if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_YES
                && StringUtils.isBlank( requisitionDetailRequest.taxGroup )) {
            requisitionDetailRequest.taxGroup = null
        }
        // end check tax amount.
        // Check for Commodity
        if (requisitionDetailRequest.commodity) {
            def commodity = financeCommodityService.findCommodityByCode( requisitionDetailRequest.commodity )
            requisitionDetailRequest.commodityDescription = commodity.description
        }
        // If header have discount code setup then remove the discountAmount value from details
        if (requisitionHeader.discount != null) {
            requisitionDetailRequest.discountAmount = null
        }
        return requisitionDetailRequest
    }

    /**
     * Gets List of requisitions
     * @param pagingParams
     * @param status
     * @return
     */
    private listRequisitions( oracleUserName, pagingParams, status ) {
        return requisitionInformationService.listRequisitionsByStatus( status, pagingParams, oracleUserName )
    }

    /**
     * Groups the records as per buckets
     * @param groupType
     * @param records
     * @return
     */

    private def groupResult( groupType, records ) {
        [category: groupType, count: records.count, list: records.list]
    }
}

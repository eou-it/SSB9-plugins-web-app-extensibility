/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.CurrencyNotFoundException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.system.FinanceCurrencyService
import net.hedtech.banner.finance.system.FinanceDiscountService
import net.hedtech.banner.finance.system.FinanceSystemControl
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

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
    def shipToCodeService
    def financeOrganizationCompositeService
    def institutionalDescriptionService
    def currencyFormatService
    def chartOfAccountsService
    def financeTaxGroupService
    def financeVendorService
    def financeDiscountService
    def financeCurrencyService

    /**
     * Fetches Requisition Information
     * @param requestCode
     */
    def fetchPurchaseRequisition( requestCode ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        def shipTo = shipToCodeService.findShipToCodesByCode( header.ship )
        def pagination = [offset: 0, max: 1]
        def organization = financeOrganizationCompositeService.
                findOrganizationListByEffectiveDateAndSearchParam( [searchParam: header.organization, coaCode: header.chartOfAccount], pagination )
        def coa = chartOfAccountsService.getChartOfAccountByCode( header.chartOfAccount )
        def taxGroup = financeTaxGroupService.findTaxGroupsBySearchParamAndEffectiveDate( [searchParam: header.taxGroup], pagination )
        def vendor
        if (header.vendorPidm != null) {
            vendor = financeVendorService.fetchFinanceVendor( [vendorPidm: header.vendorPidm, vendorAddressType: header.vendorAddressType, vendorAddressTypeSequence: header.vendorAddressTypeSequence] )
        }
        def discountObj = financeDiscountService.findDiscountByDiscountCode( header.discount )
        def currencyObj = financeCurrencyService.findCurrencyByCurrencyCode( header.currency )
        return [header      : header, shipTo: [zipCode     : shipTo.zipCode, state: shipTo.state, city: shipTo.city, shipCode: shipTo.shipCode, addressLine1: shipTo.addressLine1,
                                               addressLine2: shipTo.addressLine2, addressLine3: shipTo.addressLine3, contact: shipTo.contact],
                organization: [coaCode: organization[0].coaCode, orgnCode: organization[0].orgnCode, orgnTitle: organization[0].orgnTitle],
                coa         : [title: coa.title, chartOfAccountsCode: coa.chartOfAccountsCode],
                taxGroup    : [taxGroupCode: taxGroup[0].taxGroupCode, taxGroupTitle: taxGroup[0].taxGroupTitle],
                vendor      : vendor,
                discount    : [discountCode: discountObj.discountCode, discountDescription: discountObj.discountDescription],
                currency    : [currencyCode: currencyObj.currencyCode, title: currencyObj.title]]
    }

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
            LoggerUtility.debug LOGGER, "Requisition Header created " + requisitionHeader
            def header = RequisitionHeader.read( requisitionHeader.id )
            return header.requestCode
        } else {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
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
            LoggerUtility.debug LOGGER, "Requisition Detail created " + requisitionDetail
            return [requestCode: requisitionDetail.requestCode, item: requisitionDetail.item]
        } else {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
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
                LoggerUtility.debug LOGGER, "Requisition Header updated " + requisitionHeader
                return requisitionHeader
            } else {
                LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
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
     */
    def updateRequisitionDetail( detailDomainModel ) {
        def requestCode = detailDomainModel.requisitionDetail.requestCode
        Integer item = detailDomainModel.requisitionDetail.item
        // Null or empty check for item.
        if (!item) {
            LoggerUtility.error LOGGER, 'Item is required to update the detail.'
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
                LoggerUtility.debug LOGGER, "Requisition Detail updated " + requisitionDetail
                return requisitionDetail
            } else {
                LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

    /**
     * Creates Requisition Accounting level.
     *
     * @param map Map which have all the required date to create requisition accounting.
     * @return map Map having requestCode, item and sequenceNumber of created requisition accounting.
     */
    def createPurchaseRequisitionAccounting( map ) {
        RequisitionAccounting requisitionAccountingRequest = map.requisitionAccounting
        def user = springSecurityService.getAuthentication()?.user
        if (user?.oracleUserName) {
            requisitionAccountingRequest.userId = user.oracleUserName
            requisitionAccountingRequest.sequenceNumber = requisitionAccountingService.getLastSequenceNumberByRequestCode( requisitionAccountingRequest.requestCode ).next()
            if (requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionAccountingRequest.requestCode ).isDocumentLevelAccounting) {
                requisitionAccountingRequest.item = 0
            } else {
                requisitionAccountingRequest.item = requisitionAccountingService.getLastItemNumberByRequestCode( requisitionAccountingRequest.requestCode ).next()
            }
            RequisitionAccounting requisitionAccounting = requisitionAccountingService.create( [domainModel: requisitionAccountingRequest] )
            LoggerUtility.debug LOGGER, 'Requisition Accounting created ' + requisitionAccounting
            return [requestCode: requisitionAccounting.requestCode,
                    item       : requisitionAccounting.item, sequenceNumber: requisitionAccounting.sequenceNumber]
        } else {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
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
     * Updates Purchase requisition Accounting information.
     *
     * @param map the requisition accounting map
     */
    def updateRequisitionAccounting( accountingDomainModel ) {
        // Null or empty check for item number and sequence number.
        Integer item = accountingDomainModel.requisitionAccounting.item
        Integer sequenceNumber = accountingDomainModel.requisitionAccounting.sequenceNumber
        def requestCode = accountingDomainModel.requisitionAccounting.requestCode
        if (item == null || sequenceNumber == null) {
            LoggerUtility.error LOGGER, 'Item and Sequence number are required to update the Requisition Accounting information.'
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
                LoggerUtility.debug LOGGER, "Requisition Accounting information updated " + requisitionAccounting
                return requisitionAccounting
            } else {
                LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

    /**
     * Returns list of Requisitions in defined data structure
     * @param buckets
     * @param pagingParams
     */
    def listRequisitionsByBucket( buckets, pagingParams ) {
        def user = springSecurityService.getAuthentication()?.user
        if (user == null || user.oracleUserName == null) {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
            throw new ApplicationException( PurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        if (buckets?.isEmpty()) {
            buckets = [FinanceProcurementConstants.REQUISITION_LIST_BUCKET_ALL]
        } else {
            buckets = new ArrayList( buckets ).unique()
        }
        def wrapperList = [];
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
                LoggerUtility.error LOGGER, 'Group Type not valid'
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
     * Gives derived formatted amount
     *
     * @param amount
     * @param currency
     * @param baseCcy
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public def deriveFormattedAmount( amount, currency, baseCcy ) {
        try {
            return amount == null ? amount :
                    currencyFormatService.format( currency != null ? currency : baseCcy, (amount).toBigDecimal() )
        }
        catch (CurrencyNotFoundException cnf) {
            return FinanceCommonUtility.formatAmountForLocale( (amount).toBigDecimal() )
        }
    }

    /**
     * Gets List of requisitions
     * @param pagingParams
     * @param status
     * @return
     */
    public listRequisitions( oracleUserName, pagingParams, status ) {
        def ret = requisitionInformationService.listRequisitionsByStatus( status, pagingParams, oracleUserName )
        def baseCcy = institutionalDescriptionService.findByKey().baseCurrCode
        ret.list = ret.list.collect() {
            [id        : it.id, version: it.version, amount: deriveFormattedAmount( it.amount, it.currency, baseCcy ),
             coasCode  : it.coasCode, requestDate: it.requestDate, requisitionCode: it.requisitionCode, status: it.status, transactionDate: it.transactionDate,
             vendorName: it.vendorName]
        }
        return ret
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

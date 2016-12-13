/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.CurrencyNotFoundException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.i18n.MessageHelper
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Class for Purchase Requisition Listing Composite
 */
class RequisitionListingCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    boolean transactional = true

    def springSecurityService
    def requisitionInformationService
    def currencyFormatService

    /**
     * Returns list of Requisitions in defined data structure
     * @param buckets
     * @param pagingParams
     */
    def listRequisitionsByBucket( buckets, pagingParams, baseCcy ) {
        def user = springSecurityService.getAuthentication().user
        if (!user.oracleUserName) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionListingCompositeService, new BusinessLogicValidationException(
                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        if (buckets) {
            buckets = new ArrayList( buckets ).unique()
        } else {
            buckets = [FinanceProcurementConstants.REQUISITION_LIST_BUCKET_ALL]
        }
        def wrapperList = [];
        buckets.each() {bucket ->
            processBucket( wrapperList, bucket, pagingParams, user.oracleUserName, baseCcy )
        }
        return wrapperList
    }

    /**
     * Returns count list of Requisitions in defined data structure
     */
    def listRequisitionsCounts() {
        def user = springSecurityService.getAuthentication().user
        def wrapperList = [];
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]

        def completedStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_COMPLETED,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO]

        def pendingStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING]
        def countMap = requisitionInformationService.fetchRequisitionsCountByStatus(draftStatus + completedStatus + pendingStatus, user.oracleUserName)

        wrapperList.add(groupCountResult(countMap, draftStatus, FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT))
        wrapperList.add(groupCountResult(countMap, pendingStatus, FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING))
        wrapperList.add(groupCountResult(countMap, completedStatus, FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE))
         wrapperList
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
     * Process each Bucket
     *
     * @param wrapperList
     * @param bucket
     * @param pagingParams
     * @return
     */
    private def processBucket( wrapperList, bucket, pagingParams, user, baseCcy ) {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]

        def completedStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_COMPLETED,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO]

        def pendingStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING]
        def countMap = [:]
        switch (bucket) {
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_ALL:
                countMap = requisitionInformationService.fetchRequisitionsCountByStatus( draftStatus + completedStatus + pendingStatus, user )
                wrapperList.add( groupResult( countMap, draftStatus, FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT,
                                              listRequisitions( user, pagingParams, draftStatus, baseCcy ) ) )
                wrapperList.add( groupResult( countMap, pendingStatus, FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING,
                                              listRequisitions( user, pagingParams, pendingStatus, baseCcy ) ) )
                wrapperList.add( groupResult( countMap, completedStatus, FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE,
                                              listRequisitions( user, pagingParams, completedStatus, baseCcy ) ) )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT:
                countMap = requisitionInformationService.fetchRequisitionsCountByStatus( draftStatus, user )
                wrapperList.add( groupResult( countMap, draftStatus, bucket, listRequisitions( user, pagingParams, draftStatus, baseCcy ) ) )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING:
                countMap = requisitionInformationService.fetchRequisitionsCountByStatus( pendingStatus, user )
                wrapperList.add( groupResult( countMap, pendingStatus, bucket, listRequisitions( user, pagingParams, pendingStatus, baseCcy ) ) )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE:
                countMap = requisitionInformationService.fetchRequisitionsCountByStatus( completedStatus, user )
                wrapperList.add( groupResult( countMap, completedStatus, bucket, listRequisitions( user, pagingParams, completedStatus, baseCcy ) ) )
                break
            default:
                LoggerUtility.error( LOGGER, 'Group Type not valid' )
                throw new ApplicationException( RequisitionListingCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_INVALID_BUCKET_TYPE, [bucket] ) )
        }
    }

    /**
     * Gets List of requisitions
     * @param pagingParams
     * @param status
     * @return
     */
    private listRequisitions( oracleUserName, pagingParams, status, baseCcy ) {
        def ret = requisitionInformationService.listRequisitionsByStatus( status, pagingParams, oracleUserName )
        ret.list = processResult( ret, baseCcy )
        ret
    }

    /**
     * Groups the records as per buckets
     * @param countMap
     * @param statusList
     * @param groupType
     * @param records
     * @return
     */
    private def groupResult( countMap, statusList, groupType, records ) {
        def getCount = 0;
        statusList.each() {
            getCount += countMap.get( it ) ? countMap.get( it ).intValue() : 0
        }
        [category: groupType, count: getCount, list: records.list]
    }

    /**
     * Groups the records as per buckets
     * @param countMap
     * @param statusList
     * @param groupType
     * @return
     */
    private def groupCountResult(countMap, statusList, groupType) {
        def getCount = 0;
        statusList.each() {
            getCount += countMap.get(it) ? countMap.get(it).intValue() : 0
        }
        [category: groupType, count: getCount]
    }

    /**
     * search requisitions as per search param or search param and bucket/ status
     * @param groupType
     * @param records
     * @return
     */
    def searchPurchaseRequisition( searchDataMap, bucketType, pagingParams, baseCcy ) {
        if (bucketType) {
            searchRequisitionsByStatusAndSearchParam( bucketType, searchDataMap?.convertValue, pagingParams, searchDataMap?.isDateString, baseCcy )
        } else {
            searchRequisitionsBySearchParam( searchDataMap?.convertValue, pagingParams, searchDataMap?.isDateString, baseCcy )
        }
    }

    /**
     * Returns list of Requisitions in defined data structure
     * @param searchParam as String
     */
    private def searchRequisitionsBySearchParam( searchParam, pagingParams, isDateString, baseCcy ) {
        def user = springSecurityService.getAuthentication().user
        if (!user.oracleUserName) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionListingCompositeService, new BusinessLogicValidationException(
                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        def inputMap = [searchParam: (isDateString) ? searchParam : searchParam?.toUpperCase()]
        if (!isDateString) {
            FinanceCommonUtility.applyWildCard( inputMap, true, true )
        }
        def searchResult = requisitionInformationService.searchRequisitionsBySearchParam( user.oracleUserName, inputMap.searchParam, pagingParams, isDateString )
        filterRequisitionDataMap( searchResult, baseCcy )
        return [searchResult: searchResult]
    }

    /**
     * Returns list of Requisitions in defined data structure w.r.t search param and status
     *
     * @param wrapperList
     * @param bucket
     * @param pagingParams
     * @return
     */
    def searchRequisitionsByStatusAndSearchParam( bucket, searchParam, pagingParams, isDateString, baseCcy ) {
        def dataMap = [:]
        def user = springSecurityService.getAuthentication().user
        if (!user.oracleUserName) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionListingCompositeService, new BusinessLogicValidationException(
                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]

        def completedStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_COMPLETED,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO]

        def pendingStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING]
        switch (bucket) {
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT:
                dataMap = fetchRequisitionsByStatusAndSearchParam( user.oracleUserName, searchParam, pagingParams, draftStatus, isDateString, baseCcy )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING:
                dataMap = fetchRequisitionsByStatusAndSearchParam( user.oracleUserName, searchParam, pagingParams, pendingStatus, isDateString, baseCcy )
                break
            case FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE:
                dataMap = fetchRequisitionsByStatusAndSearchParam( user.oracleUserName, searchParam, pagingParams, completedStatus, isDateString, baseCcy )
                break
            default:
                LoggerUtility.error( LOGGER, 'Group Type not valid' )
                throw new ApplicationException( RequisitionListingCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_INVALID_BUCKET_TYPE, [bucket] ) )
        }

        return dataMap;
    }

    /**
     * Returns list of Requisitions in defined data structure
     * @param searchParam as String
     */
    private
    def fetchRequisitionsByStatusAndSearchParam( user, searchParam, pagingParams, status, isDateString, baseCcy ) {
        def inputMap = [searchParam: (isDateString) ? searchParam : searchParam?.toUpperCase()]
        if (!isDateString) {
            FinanceCommonUtility.applyWildCard( inputMap, true, true )
        }
        def searchResult = requisitionInformationService.searchRequisitionsByStatusAndSearchParam( user, inputMap.searchParam, pagingParams, status, isDateString )
        filterRequisitionDataMap( searchResult, baseCcy )
        return [searchResult: searchResult]
    }

    /**
     * Returns list of filtered requisitions as per UI display needs
     * @param list of requisitions
     * @param institutionCcy
     */
    private void filterRequisitionDataMap( ret, institutionCcy ) {
        ret.list = processResult( ret, institutionCcy )
        ret
    }

    /**
     * process the list
     * @param ret
     * @param institutionCcy
     * @return
     */
    private def processResult( ret, institutionCcy ) {
        ret.list.collect() {
            [id             : it.id,
             version        : it.version,
             amount         : deriveFormattedAmount( it.amount, it.currency, institutionCcy ),
             baseAmount     : it.amount,
             requisitionCode: it.requisitionCode,
             transactionDate: it.transactionDate,
             vendorName     : it.vendorName,
             status         : MessageHelper.message( 'purchaseRequisition.status.' + it.status ),
             infoStatus     : it.status]
        }
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
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
    def institutionalDescriptionService
    def currencyFormatService

    /**
     * Returns list of Requisitions in defined data structure
     * @param buckets
     * @param pagingParams
     */
    def listRequisitionsByBucket( buckets, pagingParams ) {
        def user = springSecurityService.getAuthentication()?.user
        if (user == null || user.oracleUserName == null) {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
            throw new ApplicationException( RequisitionListingCompositeService, new BusinessLogicValidationException(
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
    private def processBucket( wrapperList, bucket, pagingParams, user ) {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]

        def completedStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_COMPLETED,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO]

        def pendingStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING]
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
    private listRequisitions( oracleUserName, pagingParams, status ) {
        def ret = requisitionInformationService.listRequisitionsByStatus( status, pagingParams, oracleUserName )
        def baseCcy = institutionalDescriptionService.findByKey().baseCurrCode
        ret.list = ret.list.collect() {
            [id             : it.id, version: it.version, amount: deriveFormattedAmount( it.amount, it.currency, baseCcy ),
             coasCode       : it.coasCode, requestDate: it.requestDate, requisitionCode: it.requisitionCode,
             transactionDate: it.transactionDate,
             vendorName     : it.vendorName,
             status         : MessageHelper.message( it.getStatus() )]
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

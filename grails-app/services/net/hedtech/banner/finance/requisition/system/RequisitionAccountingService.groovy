/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * Service class for RequisitionAccounting.
 */
class RequisitionAccountingService extends ServiceBase {
    static transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * This method is used to find RequisitionAccounting by requisition code, item number and sequence number.
     * @param requisitionCode Requisition code.
     * @param item Item number.
     * @param sequenceNumber Sequence number.
     * @return RequisitionAccounting.
     */
    def findByRequestCodeItemAndSeq( requisitionCode, Integer item, Integer sequenceNumber ) {
        LoggerUtility.debug( LOGGER, 'Input parameter for findByRequestCodeItemAndSeq :' + requisitionCode )
        def requisitionAccounting = RequisitionAccounting.fetchByRequestCodeItemAndSeq( requisitionCode, item, sequenceNumber ).list
        if (requisitionAccounting.isEmpty()) {
            LoggerUtility.error( LOGGER, 'Requisition Accounting Information are empty for requestCode='
                    + requisitionCode + ', Item: ' + item + ' and Sequence: ' + sequenceNumber )
            throw new ApplicationException(
                    RequisitionAccountingService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING, [] ) )
        }
        def allAccounting = findAccountingByRequestCode( requisitionCode )
        allAccounting = allAccounting.findAll() {
            it.item == item
        }
        def allAccountingAmount = 0
        def totalPercentage = 0
        allAccounting.each() {
            allAccountingAmount += it.requisitionAmount + it.additionalChargeAmount + it.taxAmount - it.discountAmount
            totalPercentage += it.percentage
        }

        //TODO need to pull this code out of this place
        def reqDetail = RequisitionDetail.fetchByRequestCode( requisitionCode ).list
        def commodityTotalExtendedAmount = 0
        def commodityTotalCommodityTaxAmount = 0
        def commodityTotalAdditionalChargeAmount = 0
        def commodityTotalDiscountAmount = 0

        if (item == 0) {
            reqDetail.each {
                commodityTotalExtendedAmount += it.unitPrice * it.quantity
                commodityTotalCommodityTaxAmount += it.taxAmount
                commodityTotalAdditionalChargeAmount += it.additionalChargeAmount
                commodityTotalDiscountAmount += it.discountAmount


            }
        } else {
            reqDetail.findAll() {it.item == item}.each {
                commodityTotalExtendedAmount += it.unitPrice * it.quantity
                commodityTotalCommodityTaxAmount += it.taxAmount
                commodityTotalAdditionalChargeAmount += it.additionalChargeAmount
                commodityTotalDiscountAmount += it.discountAmount
            }
        }

        return requisitionAccounting.collect() {
            [id                                  : it.id,
             version                             : it.version,
             requestCode                         : it.requestCode,
             item                                : it.item,
             sequenceNumber                      : sequenceNumber,
             chartOfAccount                      : it.chartOfAccount,
             accountIndex                        : it.accountIndex,
             fund                                : it.fund,
             organization                        : it.organization,
             account                             : it.account,
             program                             : it.program,
             activity                            : it.activity,
             location                            : it.location,
             project                             : it.project,
             percentage                          : it.percentage,
             requisitionAmount                   : it.requisitionAmount,
             additionalChargeAmount              : it.additionalChargeAmount,
             discountAmount                      : it.discountAmount,
             taxAmount                           : it.taxAmount,
             userId                              : it.userId,
             accountTotal                        : it.requisitionAmount + it.additionalChargeAmount + it.taxAmount - it.discountAmount,
             renamingAmount                      : (commodityTotalExtendedAmount + commodityTotalCommodityTaxAmount + commodityTotalAdditionalChargeAmount - commodityTotalDiscountAmount) - allAccountingAmount,
             remaingingPercentage                : 100 - totalPercentage,
             commodityTotalExtendedAmount        : commodityTotalExtendedAmount,
             commodityTotalCommodityTaxAmount    : commodityTotalCommodityTaxAmount,
             commodityTotalAdditionalChargeAmount: commodityTotalAdditionalChargeAmount,
             commodityTotalDiscountAmount        : commodityTotalDiscountAmount,
            ]
        }.getAt( 0 )
    }

    /**
     * This method is used to find RequisitionAccounting list for logged in user.
     * @param paginationParam Map containing Pagination parameters.
     * @return List of RequisitionAccounting.
     */
    def findRequisitionAccountingListByUser( paginationParam ) {
        def loggedInUser = springSecurityService.getAuthentication()?.user
        if (loggedInUser?.oracleUserName) {
            def requisitionAccountingList = RequisitionAccounting.fetchByUserId( loggedInUser.oracleUserName, paginationParam ).list
            if (requisitionAccountingList?.isEmpty()) {
                LoggerUtility.error( LOGGER, 'Requisition Accounting Information are empty for User : ' + loggedInUser.oracleUserName )
                throw new ApplicationException(
                        RequisitionAccountingService,
                        new BusinessLogicValidationException(
                                FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING, [] ) )
            }
            return requisitionAccountingList
        } else {
            LoggerUtility.error( LOGGER, 'User' + loggedInUser + ' is not valid' )
            throw new ApplicationException( RequisitionAccountingService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * This method is used get last inserted sequence number from RequisitionAccounting.
     * @param requestCode Requisition Code.
     * @param item
     * @return last sequence number.
     */
    def getLastSequenceNumberByRequestCode( requestCode, item ) {
        def lastSequence = RequisitionAccounting.fetchLastSequenceNumberByRequestCode( requestCode, item ).getAt( 0 )
        return lastSequence ? lastSequence : 0
    }

    /*def getSplittingPercentage( requestCode, item ) {
        def percentage = RequisitionAccounting.getSplittingPercentage( requestCode, item ).getAt( 0 )
        return percentage ? percentage : 0
    }*/

    /**
     * This method is used get last inserted item number from RequisitionAccounting.
     * @param requestCode Requisition Code.
     * @return last item number.
     */
    def getLastItemNumberByRequestCode( requestCode ) {
        def lastItem = RequisitionAccounting.fetchLastItemNumberByRequestCode( requestCode ).getAt( 0 )
        return lastItem ? lastItem : 0
    }

    /**
     * List size of  accounting for specified request Code
     * @param requestCode
     * @return
     */
    def findAccountingSizeByRequestCode( requestCode ) {
        RequisitionAccounting.findAccountingByRequestCode( requestCode )?.size()
    }

    /**
     * List accounting for specified request Code
     * @param requestCode
     * @return
     */
    def findAccountingByRequestCode( requestCode ) {
        RequisitionAccounting.findAccountingByRequestCode( requestCode )
    }
}

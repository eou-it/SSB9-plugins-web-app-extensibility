/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import net.hedtech.banner.finance.system.FinanceText

/**
 * The service class which is used to have methods for copy purchase requisition.
 */
class CopyPurchaseRequisitionCompositeService {
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    boolean transactional = true

    def sessionFactory
    def requisitionHeaderService
    def requisitionHeaderForCopyService
    def financeObjectSequenceService
    def springSecurityService
    def requisitionDetailForCopyService
    def requisitionAccountingForCopyService
    def requisitionTaxForCopyService
    def financeTextService

    /**
     * This method is used to copy the requisition.
     */
    def copyRequisition( requestCode ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (header?.completeIndicator) {
            def nextRequisitionNumber = financeObjectSequenceService.findNextSequenceNumber()
            copyRequisitionHeader( header, nextRequisitionNumber, requestCode )
            copyRequisitionDetail( nextRequisitionNumber, requestCode )
            copyFinanceText( nextRequisitionNumber, requestCode )
            copyRequisitionAccounting( nextRequisitionNumber, requestCode )
            copyRequisitionTax( nextRequisitionNumber, requestCode )
            nextRequisitionNumber
        } else {
            LoggerUtility.error( LOGGER, "Only completed requisition can be copied = $header.requestCode" )
            throw new ApplicationException(
                    CopyPurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED, [] ) )
        }
    }

    /**
     * Copy Header
     * @param header
     * @param nextRequisitionNumber
     * @param requestCode
     */
    private void copyRequisitionHeader( RequisitionHeader header, nextRequisitionNumber, requestCode ) {
        def headerForCopy = RequisitionHeaderForCopy.newInstance( header.properties )
        headerForCopy.requestCode = nextRequisitionNumber
        headerForCopy.documentCopiedFrom = requestCode // Old requisition number
        headerForCopy.transactionDate = new Date()   // default to System Date
        headerForCopy.deliveryDate = null
        headerForCopy.completeIndicator = FinanceProcurementConstants.FALSE
        headerForCopy.printIndicator = null
        headerForCopy.encumbranceIndicator = null
        headerForCopy.suspenseIndicator = FinanceProcurementConstants.TRUE
        headerForCopy.cancelIndicator = null
        headerForCopy.cancellationDate = null
        headerForCopy.postingDate = null
        headerForCopy.approvalIndicator = FinanceProcurementConstants.FALSE
        headerForCopy.deferEditIndicator = FinanceProcurementConstants.FALSE
        headerForCopy.nsfOnOffIndicator = FinanceProcurementConstants.TRUE
        headerForCopy.closedIndicator = null
        headerForCopy.closedDate = null
        requisitionHeaderForCopyService.create( [domainModel: headerForCopy] )
    }

    /**
     * Copy commodities
     * @param nextRequisitionNumber
     * @param requestCode
     * @return
     */
    private copyRequisitionDetail( nextRequisitionNumber, requestCode ) {
        def requisitionDetailList = RequisitionDetailForCopy.findAllByRequestCode( requestCode )
        def detailForCopy
        requisitionDetailList.each {RequisitionDetailForCopy requisitionDetail ->
            detailForCopy = RequisitionDetailForCopy.newInstance( requisitionDetail.properties )
            detailForCopy.requestCode = nextRequisitionNumber
            detailForCopy.purchaseOrder = null
            detailForCopy.purchaseOrderItem = null
            detailForCopy.bid = null
            detailForCopy.completeIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
            detailForCopy.suspenseIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_YES
            detailForCopy.cancellationIndicator = null
            detailForCopy.cancellationDate = null
            detailForCopy.closedIndicator = null
            detailForCopy.postDate = null
            detailForCopy.buyer = null
            requisitionDetailForCopyService.create( [domainModel: detailForCopy] )
        }

    }

    /**
     * Copy accounting
     * @param nextRequisitionNumber
     * @param requestCode
     * @return
     */
    private copyRequisitionAccounting( nextRequisitionNumber, requestCode ) {

        def requisitionAccountingList = RequisitionAccountingForCopy.findAllByRequestCode( requestCode )
        def accountingForCopy
        requisitionAccountingList.each {RequisitionAccountingForCopy requisitionAccounting ->
            accountingForCopy = RequisitionAccountingForCopy.newInstance( requisitionAccounting.properties )
            accountingForCopy.requestCode = nextRequisitionNumber
            accountingForCopy.suspenseIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_YES
            accountingForCopy.nsfSuspInd = FinanceProcurementConstants.DEFAULT_INDICATOR_YES
            accountingForCopy.cancelIndicator = null
            accountingForCopy.cancellationDate = null
            accountingForCopy.approvalIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
            accountingForCopy.insufficientFundsOverrideIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
            accountingForCopy.availableBudgetOverride = null
            accountingForCopy.closedIndicator = null
            accountingForCopy.fiscalYear = null
            accountingForCopy.period = null
            requisitionAccountingForCopyService.create( [domainModel: accountingForCopy] )
        }
    }

    /**
     * Copy Tax details
     * @param nextRequisitionNumber
     * @param requestCode
     * @return
     */
    private copyRequisitionTax( nextRequisitionNumber, requestCode ) {
        def requisitionTaxList = RequisitionTaxForCopy.findAllByRequestCode( requestCode )
        requisitionTaxList.each {RequisitionTaxForCopy requisitionTax ->
            def taxForCopy = RequisitionTaxForCopy.newInstance( requisitionTax.properties )
            taxForCopy.requestCode = nextRequisitionNumber
            requisitionTaxForCopyService.create( [domainModel: taxForCopy] )
        }
    }

    /**
     * Copy text information
     * @param nextRequisitionNumber
     * @param requestCode
     */
    private void copyFinanceText( nextRequisitionNumber, requestCode ) {
        def textList = financeTextService.listAllFinanceTextByCode( 1, requestCode )
        textList.each {FinanceText financeText ->
            def textForCopy = FinanceText.newInstance( financeText.properties )
            textForCopy.textCode = nextRequisitionNumber
            financeTextService.create( [domainModel: textForCopy] )
        }
    }
}

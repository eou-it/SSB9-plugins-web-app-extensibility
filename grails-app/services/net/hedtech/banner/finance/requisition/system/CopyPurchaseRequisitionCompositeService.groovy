/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import net.hedtech.banner.finance.system.FinanceText
import grails.gorm.transactions.Transactional
import grails.web.databinding.DataBinder
/**
 * The service class which is used to have methods for copy purchase requisition.
 */
 @Transactional
class CopyPurchaseRequisitionCompositeService implements DataBinder{
    private static final def LOGGER = Logger.getLogger( this.getClass() )
   

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
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        if (header?.completeIndicator) {
            def nextRequisitionNumber = financeObjectSequenceService.findNextSequenceNumber()
            copyRequisitionHeader(header, nextRequisitionNumber, requestCode)
            copyRequisitionDetail(nextRequisitionNumber, requestCode)
            copyFinanceText(nextRequisitionNumber, requestCode)
            copyRequisitionAccounting(nextRequisitionNumber, requestCode)
            copyRequisitionTax(nextRequisitionNumber, requestCode)
            nextRequisitionNumber
        } else {
            LoggerUtility.error(LOGGER, "Only completed requisition can be copied = $header.requestCode")
            throw new ApplicationException(
                    CopyPurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED, []))
        }
    }

    /**
     * Copy Header
     * @param header
     * @param nextRequisitionNumber
     * @param requestCode
     */
    private void copyRequisitionHeader( RequisitionHeader header, nextRequisitionNumber, requestCode ) {

        RequisitionHeaderForCopy headerForCopy = new RequisitionHeaderForCopy()
        def requisitionHeaderMap = [requisitionHeader: header]
        bindData(headerForCopy,requisitionHeaderMap.requisitionHeader,[exclude: ['Id','bypassNsfChkIndicator','dirtyPropertyNames','dirty','attached']])

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

        requisitionHeaderForCopyService.create(headerForCopy)
    }

    /**
     * Copy commodities
     * @param nextRequisitionNumber
     * @param requestCode
     * @return
     */
    private copyRequisitionDetail( nextRequisitionNumber, requestCode ) {
        def requisitionDetailList = RequisitionDetailForCopy.findAllByRequestCode(requestCode)
        RequisitionDetailForCopy detailForCopy
        requisitionDetailList.each { RequisitionDetailForCopy requisitionDetail ->
            detailForCopy  = new RequisitionDetailForCopy()

            def requisitionDetailMap = [requisitionDetail: requisitionDetail]
            bindData(detailForCopy,requisitionDetailMap.requisitionDetail,[exclude: ['Id','dirtyPropertyNames','dirty','attached']])

            detailForCopy.requestCode = nextRequisitionNumber
            detailForCopy.purchaseOrder = null
            detailForCopy.purchaseOrderItem = null
            detailForCopy.bid = null
            detailForCopy.id = null
            detailForCopy.completeIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
            detailForCopy.suspenseIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_YES
            detailForCopy.cancellationIndicator = null
            detailForCopy.cancellationDate = null
            detailForCopy.closedIndicator = null
            detailForCopy.postDate = null
            detailForCopy.buyer = null
            requisitionDetailForCopyService.create(  detailForCopy)


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
        RequisitionAccountingForCopy accountingForCopy
        requisitionAccountingList.each {RequisitionAccountingForCopy requisitionAccounting ->
          accountingForCopy = new RequisitionAccountingForCopy()
            def requisitionAccountingMap = [requisitionAccounting: requisitionAccounting]
            bindData(accountingForCopy,requisitionAccountingMap.requisitionAccounting,[exclude: ['id','dirtyPropertyNames','dirty','attached']])

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
            accountingForCopy.id = null
            requisitionAccountingForCopyService.create( accountingForCopy)
        }
    }

    /**
     * Copy Tax details
     * @param nextRequisitionNumber
     * @param requestCode
     * @return
     */
    private copyRequisitionTax( nextRequisitionNumber, requestCode ) {
        def requisitionTaxList = RequisitionTaxForCopy.findAllByRequestCode(requestCode)
        requisitionTaxList.each { RequisitionTaxForCopy requisitionTax ->
            RequisitionTaxForCopy taxForCopy = new RequisitionTaxForCopy()
            taxForCopy.requestCode = nextRequisitionNumber
            requisitionTaxForCopyService.create(taxForCopy)
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
            def textForCopy  = new FinanceText()
            def FinanceTextMap = [financeText: financeText]
            bindData(textForCopy,FinanceTextMap.financeText,[exclude: ['dirtyPropertyNames','dirty','attached']])
            textForCopy.textCode = nextRequisitionNumber
            financeTextService.create( textForCopy )
        }
    }
}

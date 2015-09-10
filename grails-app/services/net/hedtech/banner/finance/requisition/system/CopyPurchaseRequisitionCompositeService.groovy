/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementSQLConstants
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import org.hibernate.HibernateException
import org.hibernate.Session

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
    def copyRequisition( requestCode, defaultQuery = null ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (header.completeIndicator) {
            Session session
            try {
                session = sessionFactory.openSession()
                session.createSQLQuery( FinanceProcurementSQLConstants.QUERY_UPDATE_NEXT_REQ_SEQUENCE ).executeUpdate()
                def nextDocCode = session.createSQLQuery( FinanceProcurementSQLConstants.QUERY_NEXT_REQ_NUMBER ).list()[0]
                session.createSQLQuery( defaultQuery ? defaultQuery : FinanceProcurementSQLConstants.QUERY_COPY_REQUISITION )
                        .setParameter( FinanceProcurementConstants.NEXT_DOC_CODE, nextDocCode )
                        .setParameter( FinanceProcurementConstants.OLD_DOC_CODE, requestCode )
                        .executeUpdate()
                nextDocCode
            } catch (HibernateException e) {
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $header.requestCode" + e )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_ERROR_WHILE_COPY, [requestCode] ) )
            }
            finally {
                session?.close()
            }
        } else {
            LoggerUtility.error( LOGGER, "Only completed requisition can be copied = $header.requestCode" )
            throw new ApplicationException(
                    CopyPurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED, [] ) )
        }
    }

    /**
     * This method is used to copy the requisition.
     */
    def copyRequisitionNew( requestCode ) {
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


    private void copyRequisitionHeader( RequisitionHeader header, nextRequisitionNumber, requestCode ) {
        try {
            def headerForCopy = RequisitionHeaderForCopy.newInstance( header.properties )
            headerForCopy.requestCode = nextRequisitionNumber
            headerForCopy.documentCopiedFrom = requestCode // Old requisition number
            headerForCopy.transactionDate = new Date()
            headerForCopy.requestDate = new Date()
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
        catch (Exception e) {
            LoggerUtility.error( LOGGER, "Error While Copy Requisition $header.requestCode" + e )
            throw new ApplicationException( CopyPurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                    FinanceProcurementConstants.ERROR_MESSAGE_ERROR_WHILE_COPY, [requestCode] ) )
        }
    }


    private copyRequisitionDetail( nextRequisitionNumber, requestCode ) {
        def requisitionDetailList = RequisitionDetailForCopy.findAllByRequestCode( requestCode )
        requisitionDetailList.each {RequisitionDetailForCopy requisitionDetail ->
            try {
                def detailForCopy = RequisitionDetailForCopy.newInstance( requisitionDetail.properties )
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
                requisitionDetailForCopyService.create( [domainModel: detailForCopy] )
            } catch (Exception e) {
                e.printStackTrace(  )
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $requestCode" + e )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_ERROR_WHILE_COPY, [requestCode] ) )
            }
        }

    }


    private copyRequisitionAccounting( nextRequisitionNumber, requestCode ) {

        def requisitionAccountingList = RequisitionAccountingForCopy.findAllByRequestCode( requestCode )
        requisitionAccountingList.each {RequisitionAccountingForCopy requisitionAccounting ->
            try {
                def accountingForCopy = RequisitionAccountingForCopy.newInstance( requisitionAccounting.properties )
                accountingForCopy.requestCode = nextRequisitionNumber
                accountingForCopy.suspenseIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                accountingForCopy.nsfSuspInd = FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                accountingForCopy.cancelIndicator = null
                accountingForCopy.cancellationDate = null
                accountingForCopy.approvalIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
                accountingForCopy.insufficientFundsOverrideIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
                accountingForCopy.availableBudgetOverride = null
                accountingForCopy.closedIndicator = null
                requisitionAccountingForCopyService.create( [domainModel: accountingForCopy] )
            }
            catch (Exception e) {
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $requestCode" + e )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_ERROR_WHILE_COPY, [requestCode] ) )
            }
        }
    }


    private copyRequisitionTax( nextRequisitionNumber, requestCode ) {
        def requisitionTaxList = RequisitionTaxForCopy.findAllByRequestCode( requestCode )
        requisitionTaxList.each {RequisitionTaxForCopy requisitionTax ->
            try {
                def taxForCopy = RequisitionTaxForCopy.newInstance( requisitionTax.properties )
                taxForCopy.requestCode = nextRequisitionNumber
                requisitionTaxForCopyService.create( [domainModel: taxForCopy] )
            }
            catch (Exception e) {
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $requestCode" + e )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_ERROR_WHILE_COPY, [requestCode] ) )
            }
        }
    }


    private void copyFinanceText( nextRequisitionNumber, requestCode ) {
        def textList = financeTextService.listAllFinanceTextByCode( requestCode )
        textList.each {FinanceText financeText ->
            try {
                def textForCopy = FinanceText.newInstance( financeText.properties )
                textForCopy.textCode = nextRequisitionNumber
                financeTextService.create( [domainModel: textForCopy] )
            } catch (Exception e) {
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $requestCode" + e )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, new BusinessLogicValidationException(
                        FinanceProcurementConstants.ERROR_MESSAGE_ERROR_WHILE_COPY, [requestCode] ) )
            }
        }
    }
}

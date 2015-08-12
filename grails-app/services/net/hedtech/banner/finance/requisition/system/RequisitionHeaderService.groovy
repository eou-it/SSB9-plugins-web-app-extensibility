/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * Service class for RequisitionHeader.
 *
 */
class RequisitionHeaderService extends ServiceBase {
    boolean transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService
    def financeApprovalHistoryService
    def financeApprovalsInProcessService
    def financeUnapprovedDocumentService

    /**
     * Find the requisition Header for specified requestCode
     * @param requestCode
     */
    def findRequisitionHeaderByRequestCode( requestCode ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for findRequisitionHeaderByRequestCode :' + requestCode )
        def retRequisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode, springSecurityService.getAuthentication()?.user?.oracleUserName )

        if (!retRequisitionHeader) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        return retRequisitionHeader;
    }

    /**
     * Find the requisition Header for Logged-in user
     */
    def listRequisitionHeaderForLoggedInUser( pagingParams ) {
        def user = springSecurityService.getAuthentication()?.user
        if (user?.oracleUserName) {
            def requisitionHeaderList = RequisitionHeader.fetchByUser( user.oracleUserName, pagingParams )
            if (requisitionHeaderList?.list?.size() == 0) {
                throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
            }
            return requisitionHeaderList.list;
        } else {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Completes the purchase requisition
     * @param requestCode
     */
    def completeRequisition( requestCode, forceComplete ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for completeRequisition :' + requestCode )
        def requisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode, springSecurityService.getAuthentication()?.user?.oracleUserName )
        if (!requisitionHeader) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeader )
        requisitionHeader.completeIndicator = Boolean.TRUE
        requisitionHeader.deliveryComment = forceComplete // Custom comment used only for complete
        update( [domainModel: requisitionHeader] )
    }

    /**
     * Service Method to recall a purchase requisition.
     * @param requestCode request code.
     */
    def recallRequisition( requestCode ) {
        def requestHeader = findRequisitionHeaderByRequestCode( requestCode )
        def user = springSecurityService.getAuthentication()?.user.oracleUserName
        if (requestHeader && requestHeader.completeIndicator && !requestHeader.approvalIndicator) {
            requestHeader.completeIndicator = false
            RequisitionHeader requestHeaderUpdated = update( [domainModel: requestHeader], true )
            // Insert FOBAPPH Approval History table with queueId = DENY and queueLevel = 0.
            def approvalHistoryList = financeApprovalHistoryService.findByDocumentCode( requestHeaderUpdated.requestCode )
            if (approvalHistoryList.size() > 0) {
                approvalHistoryList.each {FinanceApprovalHistory approvalHistory ->
                    approvalHistory.documentCode = requestHeaderUpdated.requestCode
                    approvalHistory.queueId = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_ID_DENY
                    approvalHistory.queueLevel = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_LEVEL_ZERO
                    approvalHistory.lastModifiedBy = user
                    approvalHistory.activityDate = new Date()
                    approvalHistory.sequenceNumber = new BigDecimal( FinanceProcurementConstants.ONE )
                    financeApprovalHistoryService.update( [domainModel: approvalHistory] )
                }
            } else {
                FinanceApprovalHistory financeApprovalHistory = new FinanceApprovalHistory()
                financeApprovalHistory.documentCode = requestHeaderUpdated.requestCode
                financeApprovalHistory.queueId = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_ID_DENY
                financeApprovalHistory.queueLevel = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_LEVEL_ZERO
                financeApprovalHistory.lastModifiedBy = user
                financeApprovalHistory.activityDate = new Date()
                financeApprovalHistory.sequenceNumber = new BigDecimal( FinanceProcurementConstants.ONE )
                financeApprovalHistoryService.create( [domainModel: financeApprovalHistory] )
            }
            // Removing the row relating to this requisition in FOBUAPP FinanceUnapprovedDocument.
            financeUnapprovedDocumentService.findByDocumentCode( requestHeaderUpdated.requestCode ).each {
                FinanceUnapprovedDocument financeUnapprovedDocument ->
                    financeUnapprovedDocumentService.delete( [domainModel: financeUnapprovedDocument] )
            }
            // Removing the row relating to this requisition in FOBAINP FinanceApprovalsInProcess.
            financeApprovalsInProcessService.findByDocumentNumber( requestHeaderUpdated.requestCode ).each {
                FinanceApprovalsInProcess financeApprovalsInProcess ->
                    financeApprovalsInProcessService.delete( [domainModel: financeApprovalsInProcess] )
            }
            return requestHeaderUpdated.requestCode
        } else {
            LoggerUtility.error( LOGGER, 'Only pending requisition can be recalled=' + requestCode )
            throw new ApplicationException(
                    RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_RECALL_REQUISITION_PENDING_REQ_IS_REQUIRED, [] ) )
        }
    }
}

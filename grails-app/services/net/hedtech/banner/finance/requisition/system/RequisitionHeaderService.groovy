/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

import java.sql.SQLException

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
    def requisitionDetailService
    def requisitionAccountingService
    def financeSystemControlService

    /**
     * Find the requisition Header for specified requestCode
     * @param requestCode
     */
    def findRequisitionHeaderByRequestCode( requestCode, String oracleUserName = null ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for findRequisitionHeaderByRequestCode :' + requestCode )
        if(!oracleUserName){
            oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        }
        def retRequisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode, oracleUserName )

        if (!retRequisitionHeader) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        return retRequisitionHeader;
    }

    /**
     * Find the requisition Header for Logged-in user
     */
    def listRequisitionHeaderForLoggedInUser( pagingParams ) {
        def user = springSecurityService.getAuthentication().user
        if (user.oracleUserName) {
            def requisitionHeaderList = RequisitionHeader.fetchByUser( user.oracleUserName, pagingParams )
            if (!requisitionHeaderList?.list) {
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

    def completeRequisition( requestCode, forceComplete,bypassNsfChkIndicator, String oracleUserName = null) {    
        LoggerUtility.debug( LOGGER, 'Input parameters for completeRequisition :' + requestCode )
        if(!oracleUserName){
            oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        }
        def requisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode, oracleUserName )
        if (!requisitionHeader) {
            LoggerUtility.error( LOGGER, 'Header not found for ' + requestCode )
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeader )
        requisitionHeader.completeIndicator = Boolean.TRUE
        requisitionHeader.deliveryComment = forceComplete // Custom comment used only for complete
        requisitionHeader.bypassNsfChkIndicator = bypassNsfChkIndicator
        update( [domainModel: requisitionHeader] )
    }

    /**
     * Validate the purchase requisition before completing it
     * @param requestCode
     */
    def validateRequisitionBeforeComplete( requestCode ) {
        LoggerUtility.debug(LOGGER, 'Input parameters for validate Requisition :' + requestCode)
        def assignBuyerCode
        def autoBuyrInd = financeSystemControlService.findActiveFinanceSystemControl().autoBuyrInd
        def requisitionHeader = RequisitionHeader.fetchByRequestCode(requestCode, springSecurityService.getAuthentication().user.oracleUserName)
        update([domainModel: requisitionHeader])

        //Validate only if the Requisition is copied
        if (requisitionHeader.documentCopiedFrom)
        {
            def requisitionDetailsList = requisitionDetailService.findByRequestCode( requestCode )
            requisitionDetailsList.each () {
                it.bid = null
                if(autoBuyrInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES) {
                    assignBuyerCode = assignBuyer(it)
                    it.buyer = assignBuyerCode
                }
                requisitionDetailService.update ( [domainModel: it] )
                if(autoBuyrInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES) {
                    updateBuyerAssignDate(assignBuyerCode)
                }
            }
            def requisitionAccountingList = requisitionAccountingService.findAccountingByRequestCode(requestCode)
            requisitionAccountingList.each () {
                //Making model as dirty, the will be update by DB API
                it.fiscalYear="00"
                it.period="00"
                requisitionAccountingService.update( [domainModel: it] )
            }
        }
    }

    private def assignBuyer(requisitionDetail) {
        def effDate = new Date().format('yyyyMMdd')
        def effTime = '235959'
        def buyerCode = sessionFactory.currentSession
                            .createSQLQuery( """SELECT FTVBUYR_CODE FROM FTVBUYR WHERE (FTVBUYR_CODE) IN (( SELECT FTVBUYO_CODE FROM FTVBUYO WHERE FTVBUYO_COAS_CODE = :FPBREQH_COAS_CODE AND 
                                FTVBUYO_ORGN_CODE = :FPBREQH_ORGN_CODE UNION SELECT FTVBUYC_CODE FROM FTVBUYC WHERE FTVBUYC_COMM_CODE = :AUTO_COMM_CODE )) AND TRUNC(FTVBUYR_EFF_DATE) <= TO_DATE($effDate$effTime,'YYYYMMDDHH24MISS')
                                AND (TRUNC(FTVBUYR_TERM_DATE) > TO_DATE($effDate$effTime,'YYYYMMDDHH24MISS') OR FTVBUYR_TERM_DATE IS NULL) ORDER BY FTVBUYR_LAST_ASSIGN_DATE""" )
                            .setString( 'FPBREQH_COAS_CODE', requisitionDetail.chartOfAccount )
                            .setString( 'FPBREQH_ORGN_CODE', requisitionDetail.organization )
                            .setString( 'AUTO_COMM_CODE', requisitionDetail.commodity ).list()
        if(buyerCode == null) {
            buyerCode = sessionFactory.currentSession
                    .createSQLQuery( """SELECT FTVBUYR_CODE FROM FTVBUYR WHERE TRUNC(FTVBUYR_EFF_DATE) <= TO_DATE($effDate$effTime,'YYYYMMDDHH24MISS') 
                        AND (TRUNC(FTVBUYR_TERM_DATE) > TO_DATE($effDate$effTime,'YYYYMMDDHH24MISS') OR FTVBUYR_TERM_DATE IS NULL) ORDER BY FTVBUYR_LAST_ASSIGN_DATE""" ).list()
        }
        buyerCode[0]
    }

    private def updateBuyerAssignDate(assignBuyerCode) {
        def sql
        try {
            sessionFactory.currentSession.with { session ->
                sql = new Sql(session.connection())
                sql.executeUpdate("UPDATE FTVBUYR SET FTVBUYR_LAST_ASSIGN_DATE = SYSDATE WHERE FTVBUYR_CODE = ?", [assignBuyerCode])
            }
        } finally {
            sql?.close()
        }
    }

    /**
     * Service Method to recall a purchase requisition.
     * @param requestCode request code.
     */
    def recallRequisition( requestCode ) {
        def requestHeader = findRequisitionHeaderByRequestCode( requestCode )
        def user = springSecurityService.getAuthentication().user.oracleUserName
        if (requestHeader.completeIndicator && !requestHeader.approvalIndicator) {
            requestHeader.completeIndicator = false
            RequisitionHeader requestHeaderUpdated = update( [domainModel: requestHeader], true )
            // Insert FOBAPPH Approval History table with queueId = DENY and queueLevel = 0.
            def approvalHistoryList = financeApprovalHistoryService.findByDocumentCode( requestHeaderUpdated.requestCode )
            if (approvalHistoryList) {
                approvalHistoryList.each {approvalHistory ->
                    approvalHistory.documentCode = requestHeaderUpdated.requestCode
                    approvalHistory.queueId = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_ID_DENY
                    approvalHistory.queueLevel = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_LEVEL_ZERO
                    approvalHistory.lastModifiedBy = user
                    approvalHistory.activityDate = new Date()
                    approvalHistory.sequenceNumber = new BigDecimal( FinanceProcurementConstants.ONE )
                    financeApprovalHistoryService.update( [domainModel: approvalHistory] )
                }
            } else {
                def financeApprovalHistory = [:]
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
                 financeUnapprovedDocument ->
                    financeUnapprovedDocumentService.delete( [domainModel: financeUnapprovedDocument] )
            }
            // Removing the row relating to this requisition in FOBAINP FinanceApprovalsInProcess.
            financeApprovalsInProcessService.findByDocumentNumber( requestHeaderUpdated.requestCode ).each {
                 financeApprovalsInProcess ->
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

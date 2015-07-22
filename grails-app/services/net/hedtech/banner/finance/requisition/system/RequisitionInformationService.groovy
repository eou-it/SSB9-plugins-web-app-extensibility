/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

import java.sql.SQLException

class RequisitionInformationService extends ServiceBase {
    boolean transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * List requisition information by status
     * @param status
     * @param pagingParams
     * @return
     */

    def listRequisitionsByStatus( status, pagingParams, oracleUserName ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        def list = RequisitionInformation.listRequisitionsByStatus( oracleUserName, pagingParams, status )
        [list: list, count: pagingParams.offset == 0 ? fetchRequisitionsCountByStatus( status, oracleUserName ) : list?.size()]
    }

    /**
     * Fetch Requisition Count
     * @param status
     * @param oracleUserName
     * @return
     */
    def fetchRequisitionsCountByStatus( status, oracleUserName ) {
        def sql, result
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        try {
            sql = new Sql( sessionFactory.currentSession.connection() )
            result = sql.firstRow( "select count(reqInfo.surrogate_id) as numberOfRows from " +
                                           FinanceProcurementConstants.VIEW_FVQ_REQ_DASHBOARD_INFO +
                                           " reqInfo where reqInfo.status in ( " + status.collect() {it -> it = "'" + it + "'"}.join( ',' ) + ") and reqinfo.user_id = '" + oracleUserName + "'" ).numberOfRows
        }
        catch (SQLException sqe) {
            LoggerUtility.error( LOGGER, 'Error while getting requisition count ' + sqe )
            throw new ApplicationException( RequisitionInformationService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_REQ_RECORD_COUNT, [] ) )
        }
        finally {
            sql?.close()
        }
        result
    }

    /**
     * Fetch Requisition for specified requisition no
     * @param requestNo
     * @return
     */
    def fetchRequisitionsByReqNumber( requestNo ) {
        RequisitionInformation.fetchRequisitionsByReqNumber( getOracleUserNameForLoggedInUser(), requestNo )
    }

    /**
     * Gets oracle user name for Logged In user
     * @return
     */
    def private getOracleUserNameForLoggedInUser() {
        def user = springSecurityService.getAuthentication()?.user
        if (!user.oracleUserName) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionInformationService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        user.oracleUserName
    }

    /**
     * List requisition information by search param
     * @param searchParam
     * @param pagingParams
     * @return
     */

    def searchRequisitionsBySearchParam( oracleUserName, searchParam, pagingParams, isDateString ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        if (isDateString) {
            def list = RequisitionInformation.listRequisitionsByTransactionDate( oracleUserName, searchParam, pagingParams )
            [list: list, count: pagingParams.offset == 0 ? fetchRequisitionsCountByTransactionDate( searchParam, oracleUserName ) : list?.size()]
        } else {
            def list = RequisitionInformation.listRequisitionsBySearchParam( oracleUserName, searchParam, pagingParams )
            [list: list, count: pagingParams.offset == 0 ? fetchRequisitionsCountBySearchParam( searchParam, oracleUserName ) : list?.size()]
        }

    }

    /**
     * List requisition information by search param
     * @param searchParam
     * @param pagingParams
     * @return
     */

    def searchRequisitionsByStatusAndSearchParam( oracleUserName, searchParam, pagingParams, status, isDateString ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        if (isDateString) {
            def list = RequisitionInformation.listRequisitionsByStatusAndTransactionDate( oracleUserName, searchParam, pagingParams, status )
            [list: list, count: pagingParams.offset == 0 ? fetchRequisitionsCountByStatusAndTransactionDate( searchParam, oracleUserName, status ) : list?.size()]
        } else {
            def list = RequisitionInformation.listRequisitionsByStatusAndSearchParam( oracleUserName, searchParam, pagingParams, status )
            [list: list, count: pagingParams.offset == 0 ? fetchRequisitionsCountByBucketAndSearchParam( searchParam, oracleUserName, status ) : list?.size()]
        }
    }

    /**
     * Fetch Requisition Count by search param
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private def fetchRequisitionsCountBySearchParam( searchParam, oracleUserName ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        RequisitionInformation.fetchRequisitionsCountBySearchParam( searchParam, oracleUserName )
    }

    /**
     * Fetch Requisition Count by search param as date
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private def fetchRequisitionsCountByTransactionDate( searchParam, oracleUserName ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        RequisitionInformation.fetchRequisitionsCountByTransactionDate( searchParam, oracleUserName )
    }

    /**
     * Fetch Requisition Count by search param
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private def fetchRequisitionsCountByBucketAndSearchParam( searchParam, oracleUserName, status ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        RequisitionInformation.fetchRequisitionsCountByStatusAndSearchParam( searchParam, oracleUserName, status )
    }

    /**
     * Fetch Requisition Count by search param
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private def fetchRequisitionsCountByStatusAndTransactionDate( searchParam, oracleUserName, status ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        RequisitionInformation.fetchRequisitionsCountByStatusAndTransactionDate( searchParam, oracleUserName, status )
    }
}

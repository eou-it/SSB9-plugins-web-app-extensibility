/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
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
        if (!oracleUserName) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        def list = RequisitionInformation.listRequisitionsByStatus( oracleUserName, pagingParams, status )
        [list: list, count: list?.size()]
    }

    /**
     * Fetch Requisition Count
     * @param status
     * @param oracleUserName
     * @return
     */
    def fetchRequisitionsCountByStatus( status, oracleUserName ) {
        def countMap = [:]
        def sql
        if (!oracleUserName) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        try {
            sql = new Sql( sessionFactory.currentSession.connection() )
            sql.eachRow( FinanceProcurementConstants.REQUISITION_COUNT_QRY_1 +
                                 FinanceProcurementConstants.VIEW_FVQ_REQ_DASHBOARD_INFO +
                                 FinanceProcurementConstants.REQUISITION_COUNT_QRY_2
                                 + status.collect() {it ->
                FinanceProcurementConstants.SINGLE_QUOTES + it +
                        FinanceProcurementConstants.SINGLE_QUOTES
            }.join( FinanceProcurementConstants.COMMA ) +
                                 FinanceProcurementConstants.REQUISITION_COUNT_QRY_3 + oracleUserName +
                                 FinanceProcurementConstants.SINGLE_QUOTES +
                                 FinanceProcurementConstants.REQUISITION_COUNT_QRY_4
            ) {row ->
                countMap.put( row.status, row.numberOfRows )
            }
            countMap
        }
        catch (SQLException sqe) {
            LoggerUtility.error( LOGGER, 'Error while getting requisition count ' + sqe )
            throw new ApplicationException( RequisitionInformationService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_REQ_RECORD_COUNT, [] ) )
        }
        finally {
            sql?.close()
        }
        countMap
    }

    /**
     * Fetch Requisition for specified requisition no
     * @param requestNo
     * @return
     */
    def fetchRequisitionsByReqNumber( requestNo ) {
        fetchRequisitionsByReqNumber( requestNo, getOracleUserNameForLoggedInUser() )
    }

    /**
     * Fetch Requisition for specified requisition no and user id
     * @param requestNo
     * @param user
     * @return
     */
    def fetchRequisitionsByReqNumber( requestNo, user ) {
        RequisitionInformation.fetchRequisitionsByReqNumber( user, requestNo )
    }

    /**
     * Gets oracle user name for Logged In user
     * @return
     */
    private getOracleUserNameForLoggedInUser() {
        def user = springSecurityService.getAuthentication().user
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
        if (!oracleUserName) {
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
        if (!oracleUserName) {
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
    private fetchRequisitionsCountBySearchParam( searchParam, oracleUserName ) {
        RequisitionInformation.fetchRequisitionsCountBySearchParam( searchParam, oracleUserName )
    }

    /**
     * Fetch Requisition Count by search param as date
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private fetchRequisitionsCountByTransactionDate( searchParam, oracleUserName ) {
        RequisitionInformation.fetchRequisitionsCountByTransactionDate( searchParam, oracleUserName )
    }

    /**
     * Fetch Requisition Count by search param
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private fetchRequisitionsCountByBucketAndSearchParam( searchParam, oracleUserName, status ) {
        RequisitionInformation.fetchRequisitionsCountByStatusAndSearchParam( searchParam, oracleUserName, status )
    }

    /**
     * Fetch Requisition Count by search param
     * @param searchParam
     * @param oracleUserName
     * @return
     */
    private fetchRequisitionsCountByStatusAndTransactionDate( searchParam, oracleUserName, status ) {
        RequisitionInformation.fetchRequisitionsCountByStatusAndTransactionDate( searchParam, oracleUserName, status )
    }
}

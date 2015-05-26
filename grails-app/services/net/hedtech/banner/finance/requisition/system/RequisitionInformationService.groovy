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
        [list: RequisitionInformation.listRequisitionsByStatus( oracleUserName, pagingParams, status ), count: fetchRequisitionsCountByStatus( status, oracleUserName )]
    }

    /**
     * Fetch Requisition Count
     * @param status
     * @param oracleUserName
     * @return
     */
    def fetchRequisitionsCountByStatus( status, oracleUserName ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        RequisitionInformation.fetchRequisitionsCountByStatus( oracleUserName, status )
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
}
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
 * Service class for RequisitionInformation.
 */
class RequisitionInformationService extends ServiceBase {
    boolean transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * List requisition information by status
     * @param status
     * @param pagingParams
     * @return array which will have requisition list and count of the items in the requisition list.
     */

    def listRequisitionsByStatus( status, pagingParams, oracleUserName ) {
        if (oracleUserName == null) {
            oracleUserName = getOracleUserNameForLoggedInUser()
        }
        [
                list : RequisitionInformation.listRequisitionsByStatus( oracleUserName, pagingParams, status ),
                count: RequisitionInformation.fetchRequisitionsCountByStatus( oracleUserName, status )
        ]
    }

    /**
     * Gets oracle user name for Logged In user
     * @return
     */
    def private getOracleUserNameForLoggedInUser() {
        def user = springSecurityService.getAuthentication()?.user
        if (user == null || user.oracleUserName == null) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionInformationService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        user.oracleUserName
    }
}

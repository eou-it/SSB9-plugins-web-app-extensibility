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

class RequisitionHeaderService extends ServiceBase {
    boolean transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * Find the requisition Header for specified requestCode
     * @param requestCode
     */
    def findRequisitionHeaderByRequestCode( requestCode ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for findRequisitionHeaderByRequestCode :' + requestCode )
        def retRequisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode )

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
            def oracleUserName = user.oracleUserName
            def requisitionHeaderList = RequisitionHeader.fetchByUser( oracleUserName, pagingParams )
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
    def completeRequisition( requestCode ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for completeRequisition :' + requestCode )
        def requisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode )
        if (!requisitionHeader) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeader )
        requisitionHeader.completeIndicator = Boolean.TRUE
        update( [domainModel: requisitionHeader] )
    }
}

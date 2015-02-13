/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

class RequisitionHeaderService extends ServiceBase {
    boolean transactional = true
    def log = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * Find the requisition Header for specified requestCode
     * @param requestCode
     */
    def findRequisitionHeaderByRequestCode( requestCode ) {
        log.debug( 'Input parameters for findRequisitionHeaderByRequestCode :' + requestCode )
        def retRequisitionHeader = RequisitionHeader.fetchByRequestCode( requestCode )

        if (retRequisitionHeader == null) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( "missing.requisition.header", [] ) )
        }
        return retRequisitionHeader;
    }

    /**
     * Find the requisition Header for Logged-in user
     */
    def listRequisitionHeaderForLoggedInUser( pagingParams ) {
        def user = springSecurityService.getAuthentication()?.user
        if (user) {
            def oracleUserName = user?.oracleUserName
            def requisitionHeaderList = RequisitionHeader.fetchByUser( oracleUserName, pagingParams )
            if (requisitionHeaderList?.size() == 0) {
                throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( "missing.requisition.header", [] ) )
            }
            return requisitionHeaderList;
        } else {
            log.error( 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionHeaderCompositeService, new BusinessLogicValidationException( "user.not.valid", [] ) )
        }
    }


}

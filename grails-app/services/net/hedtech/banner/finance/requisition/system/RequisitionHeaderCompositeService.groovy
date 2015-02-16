/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import org.apache.log4j.Logger

class RequisitionHeaderCompositeService {
    def requisitionHeaderService
    boolean transactional = true
    def log = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * Create requisition Header
     * @param map
     * @return
     */
    def createOrUpdate( map ) {
        if (map?.requisitionHeader) {
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            def user = springSecurityService.getAuthentication()?.user
            if (user) {
                def oracleUserName = user?.oracleUserName
                requisitionHeaderRequest.userId = oracleUserName
                def requisitionHeader = requisitionHeaderService.create( [domainModel: requisitionHeaderRequest] )
                log.debug "Requisition Header created " + requisitionHeader
                def header = RequisitionHeader.read( requisitionHeader.id )
                return header.requestCode
            } else {
                log.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( RequisitionHeaderCompositeService, new BusinessLogicValidationException( "user.not.valid", [] ) )
            }
        }
    }

    /**
     * Delete requisition Header
     * @param requestCode
     */
    def deleteRequisitionHeader( requestCode ) {
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        requisitionHeaderService.delete( [domainModel: requisitionHeader] )
    }

    /**
     * Delete requisition Header
     *
     * @param map the header map
     * @param requestCode
     */
    def updateRequisitionHeader( map, requestCode ) {
        def existingHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (map?.requisitionHeader) {
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            requisitionHeaderRequest.id = existingHeader.id
            requisitionHeaderRequest.version = existingHeader.version
            requisitionHeaderRequest.requestCode = existingHeader.requestCode
            def user = springSecurityService.getAuthentication()?.user
            if (user) {
                def oracleUserName = user?.oracleUserName
                requisitionHeaderRequest.userId = oracleUserName
                def requisitionHeader = requisitionHeaderService.update( [domainModel: requisitionHeaderRequest] )
                log.debug "Requisition Header updated " + requisitionHeader
                def header = RequisitionHeader.read( requisitionHeader.id )
                return header
            } else {
                log.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( RequisitionHeaderCompositeService, new BusinessLogicValidationException( "user.not.valid", [] ) )
            }
        }
    }

}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Composite
 */
class PurchaseRequisitionCompositeService {
    def requisitionHeaderService
    boolean transactional = true
    def log = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * Create purchase requisition Header
     * @param map
     * @return
     */
    def createPurchaseRequisition( map ) {
        // Create Header
        if (map?.requisitionHeader) {
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            def user = springSecurityService.getAuthentication()?.user
            if (user) {
                def oracleUserName = user?.oracleUserName
                requisitionHeaderRequest.userId = oracleUserName
                def requisitionHeader
                try {
                    requisitionHeader = requisitionHeaderService.create( [domainModel: requisitionHeaderRequest] )
                } catch (ApplicationException e) {

                }
                log.debug "Requisition Header created " + requisitionHeader
                def header = RequisitionHeader.read( requisitionHeader.id )
                return header.requestCode
            } else {
                log.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID ) )
            }
        }
    }

    /**
     * Delete Purchase Requisition
     * @param requestCode
     */
    def deletePurchaseRequisition( requestCode ) {
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        requisitionHeaderService.delete( [domainModel: requisitionHeader] )
    }

    /**
     * Update Purchase requisition
     *
     * @param map the requisition map
     * @param requestCode
     */
    def updateRequisitionHeader( map, requestCode ) {
        // Update header
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
                throw new ApplicationException( PurchaseRequisitionCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, ) )
            }
        }
    }

}

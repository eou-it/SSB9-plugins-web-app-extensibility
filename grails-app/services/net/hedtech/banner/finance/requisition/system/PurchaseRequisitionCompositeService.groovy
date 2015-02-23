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
    def requisitionDetailService
    def financeSystemControlService
    def financeCommodityService

    /**
     * Create purchase requisition Header
     * @param map Map which contains the RequisitionHeader/RequisitionDetail domains with values.
     * @return Requisition code.
     */
    def createPurchaseRequisition( map ) {
        def resultMap = [:]
        // Create Header/Detail
        if (map?.requisitionHeader) {
            resultMap.headerReqCode = createRequisitionHeader( map )
        } else if (map?.requisitionDetail) {
            resultMap.detailReqCode = createRequisitionDetail( map )
        }
        return resultMap
    }

    /**
     * This private method is used to create Requisition Header.
     * @param map Map contains RequisitionHeader domain model.
     * @return Requisition Code.
     */
    private def createRequisitionHeader( map ) {
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
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID ), [] )
        }
    }

    /**
     * This private method is used to create Requisition Detail.
     * @param map Map contains RequisitionDetail domain model.
     * @return Reauisition Code.
     */
    private def createRequisitionDetail( map ) {
        RequisitionDetail requisitionDetailRequest = map.requisitionDetail
        def user = springSecurityService.getAuthentication()?.user
        if (user) {
            def oracleUserName = user?.oracleUserName
            def lastItem = requisitionDetailService.getLastItem(  )
            requisitionDetailRequest.userId = oracleUserName
            requisitionDetailRequest.item = lastItem + 1
            RequisitionDetail requisitionDetail = requisitionDetailService.create( [domainModel: requisitionDetailRequest] )
            log.debug "Requisition Detail created " + requisitionDetail
            return requisitionDetail.requestCode
        } else {
            log.error( 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID ), [] )
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
                throw new ApplicationException(
                        PurchaseRequisitionCompositeService,
                        new BusinessLogicValidationException(
                                FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

}
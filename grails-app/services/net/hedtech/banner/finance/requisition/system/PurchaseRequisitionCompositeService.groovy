/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.system.FinanceSystemControl
import net.hedtech.banner.finance.util.FinanceCommonUtility
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Composite
 */
class PurchaseRequisitionCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.getClass() )
    boolean transactional = true

    def requisitionHeaderService
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
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            requisitionHeaderRequest.userId = oracleUserName
            // Check for tax group
            FinanceSystemControl financeSystemControl = financeSystemControlService.findActiveFundOrgSecurityIndicator()
            if (financeSystemControl?.taxProcessingIndicator == FinanceValidationConstants.FUND_ORG_SECURITY_INDICATOR_NO) {
                requisitionHeaderRequest.taxGroup = null
            }
            def requisitionHeader = requisitionHeaderService.create( [domainModel: requisitionHeaderRequest] )
            LOGGER.debug "Requisition Header created " + requisitionHeader
            def header = RequisitionHeader.read( requisitionHeader.id )
            return header.requestCode
        } else {
            LOGGER.error( 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID ), [] )
        }
    }

    /**
     * This private method is used to create Requisition Detail.
     * @param map Map contains RequisitionDetail domain model.
     * @return Requisition Code.
     */
    private def createRequisitionDetail( map ) {
        RequisitionDetail requisitionDetailRequest = map.requisitionDetail
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            def requestCode = requisitionDetailRequest.requestCode
            def lastItem = requisitionDetailService.getLastItem( requestCode )
            requisitionDetailRequest.userId = oracleUserName
            requisitionDetailRequest.item = lastItem + 1
            // Set all data with business logic.
            requisitionDetailRequest = setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest )
            RequisitionDetail requisitionDetail = requisitionDetailService.create( [domainModel: requisitionDetailRequest] )
            LOGGER.debug "Requisition Detail created " + requisitionDetail
            return requisitionDetail.requestCode
        } else {
            LOGGER.error( 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    PurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * This method is used to set data for Create/Update requisition detail
     * @param requestCode Requisition Code.
     * @param requisitionDetailRequest Requisition details.
     * @return updated requisition details.
     */
    private def setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest ) {
        // Set all the required information from the Requisition Header.
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        requisitionDetailRequest.chartOfAccount = requisitionHeader.chartOfAccount
        requisitionDetailRequest.organization = requisitionHeader.organization
        requisitionDetailRequest.ship = requisitionHeader.ship
        requisitionDetailRequest.requisitionDate = requisitionHeader.requestDate
        // start check tax amount.
        FinanceSystemControl financeSystemControl = financeSystemControlService.findActiveFundOrgSecurityIndicator()
        if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.FUND_ORG_SECURITY_INDICATOR_NO) {
            requisitionDetailRequest.taxGroup = null
        } else if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.FUND_ORG_SECURITY_INDICATOR_YES) {
            if (StringUtils.isBlank( requisitionDetailRequest.taxGroup )) {
                requisitionDetailRequest.taxGroup = null
            }
        }
        // end check tax amount.
        // Check for Commodity
        if (requisitionDetailRequest.commodity) {
            def commodity = financeCommodityService.findCommodityByCode( requisitionDetailRequest.commodity )
            requisitionDetailRequest.commodityDescription = commodity.description
        }
        // If header don’t have discount code setup then remove the discountAmount value
        if (!requisitionHeader.discount == null) {
            requisitionDetailRequest.remove( 'discountAmount' )
        }
        return requisitionDetailRequest
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
            if (user.oracleUserName) {
                def oracleUserName = user?.oracleUserName
                requisitionHeaderRequest.userId = oracleUserName
                def requisitionHeader = requisitionHeaderService.update( [domainModel: requisitionHeaderRequest] )
                LOGGER.debug "Requisition Header updated " + requisitionHeader
                def header = RequisitionHeader.read( requisitionHeader.id )
                return header
            } else {
                LOGGER.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

    /**
     * Delete Purchase Requisition Detail.
     * @param requestCode Requisition Code.
     * @param item Item.
     */
    def deletePurchaseRequisitionDetail( requestCode, item ) {
        def requisitionDetail = requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( requestCode, item )
        requisitionDetailService.delete( [domainModel: requisitionDetail] )
    }

    /**
     * Update Purchase requisition detail commodity level.
     *
     * @param map the requisition detail map
     * @param requestCode
     */
    def updateRequisitionDetail( detailDomainModel, requestCode, item ) {
        // Null or empty check for item.
        if (StringUtils.isEmpty( item )) {
            LOGGER.error( 'Item is required to update the detail.' )
            throw new ApplicationException( PurchaseRequisitionCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED, [] ) )
        }
        def paginationParam = FinanceCommonUtility.getPagingParams( null, null )
        def existingDetail = requisitionDetailService.fetchByRequestCodeAndItem( requestCode, item, paginationParam ).getAt( 0 )
        if (detailDomainModel?.requisitionDetail) {
            RequisitionDetail requisitionDetailRequest = detailDomainModel.requisitionDetail
            requisitionDetailRequest.id = existingDetail.id
            requisitionDetailRequest.version = existingDetail.version
            requisitionDetailRequest.requestCode = existingDetail.requestCode
            def user = springSecurityService.getAuthentication()?.user
            if (user.oracleUserName) {
                requisitionDetailRequest = setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest )
                requisitionDetailRequest.lastModified = new Date()
                requisitionDetailRequest.item = existingDetail.item
                def requisitionDetail = requisitionDetailService.update( [domainModel: requisitionDetailRequest] )
                LOGGER.debug "Requisition Detail updated " + requisitionDetail
                def detail = RequisitionDetail.read( requisitionDetail.id )
                return detail
            } else {
                LOGGER.error( 'User' + user + ' is not valid' )
                throw new ApplicationException( PurchaseRequisitionCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
        }
    }

}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.system.FinanceCommodityService
import net.hedtech.banner.finance.system.FinanceSystemControl
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Details Composite Service
 */
class RequisitionDetailsCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    boolean transactional = true

    def requisitionHeaderService
    def springSecurityService
    def requisitionDetailService
    def financeSystemControlService
    def financeCommodityService

    /**
     * Create purchase requisition detail
     * @param map Map which contains the RequisitionDetail domain with values.
     * @return requestCode and item number.
     */
    def createPurchaseRequisitionDetail( map ) {
        RequisitionDetail requisitionDetailRequest = map.requisitionDetail
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            def requestCode = requisitionDetailRequest.requestCode
            def lastItem = requisitionDetailService.getLastItem( requestCode )
            requisitionDetailRequest.userId = oracleUserName
            requisitionDetailRequest.item = lastItem.next()
            // Set all data with business logic.
            requisitionDetailRequest = setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest )
            RequisitionDetail requisitionDetail = requisitionDetailService.create( [domainModel: requisitionDetailRequest] )
            LoggerUtility.debug LOGGER, "Requisition Detail created " + requisitionDetail
            return [requestCode: requisitionDetail.requestCode, item: requisitionDetail.item]
        } else {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
            throw new ApplicationException(
                    RequisitionDetailsCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Delete Purchase Requisition Detail.
     * @param requestCode Requisition Code.
     * @param item Item.
     */
    def deletePurchaseRequisitionDetail( requestCode, Integer item ) {
        def requisitionDetail = requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( requestCode, item )
        requisitionDetailService.delete( [domainModel: requisitionDetail] )
    }

    /**
     * Update Purchase requisition detail commodity level.
     *
     * @param map the requisition detail map
     */
    def updateRequisitionDetail( detailDomainModel ) {
        def requestCode = detailDomainModel.requisitionDetail.requestCode
        Integer item = detailDomainModel.requisitionDetail.item
        // Null or empty check for item.
        if (!item) {
            LoggerUtility.error LOGGER, 'Item is required to update the detail.'
            throw new ApplicationException( RequisitionDetailsCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED, [] ) )
        }
        def existingDetail = requisitionDetailService.findByRequestCodeAndItem( requestCode, item )
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
                requisitionDetailRequest.userId = user.oracleUserName
                def requisitionDetail = requisitionDetailService.update( [domainModel: requisitionDetailRequest] )
                LoggerUtility.debug LOGGER, "Requisition Detail updated " + requisitionDetail
                return requisitionDetail
            } else {
                LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
                throw new ApplicationException( RequisitionDetailsCompositeService,
                                                new BusinessLogicValidationException(
                                                        FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
            }
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
        requisitionDetailRequest.deliveryDate = requisitionHeader.deliveryDate
        // start check tax amount.
        FinanceSystemControl financeSystemControl = financeSystemControlService.findActiveFinanceSystemControl()
        if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO) {
            requisitionDetailRequest.taxGroup = null
        } else if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_YES
                && StringUtils.isBlank( requisitionDetailRequest.taxGroup )) {
            requisitionDetailRequest.taxGroup = null
        }
        // end check tax amount.
        // Check for Commodity
        if (requisitionDetailRequest.commodity) {
            def commodity = financeCommodityService.findCommodityByCode( requisitionDetailRequest.commodity )
            requisitionDetailRequest.commodityDescription = commodity.description
        }
        // If header have discount code setup then remove the discountAmount value from details
        if (requisitionHeader.discount != null) {
            requisitionDetailRequest.discountAmount = null
        }
        return requisitionDetailRequest
    }

    /**
     * This method is used to find RequisitionDetail list by requisition code.
     * @param requisitionCode Requisition code.
     * @return List of requisition code.
     */
    def findByRequestCode( requisitionCode ) {
        def requisitionDetails = requisitionDetailService.findByRequestCode( requisitionCode )
        def commodityCodes = requisitionDetails.collect() {
            it.commodity
        }
        def commodityList = financeCommodityService.findCommodityByCodeList( commodityCodes )
        Map commodityCodeDescMap = commodityList.collectEntries {[it.commodityCode, it.description]}
        def getDescription = {commodity ->
            commodityCodeDescMap.get( commodity )
        }
        requisitionDetails.each() {
            it.commodityDescription = getDescription( it.commodity )
        }
        requisitionDetails
    }
}

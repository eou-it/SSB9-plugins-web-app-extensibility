/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.system.FinanceSystemControl
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Transactional

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
    def financeUnitOfMeasureService
    def financeTaxCompositeService

    /**
     * Create purchase requisition detail
     * @param map Map which contains the RequisitionDetail domain with values.
     * @return requestCode and item number.
     */
    def createPurchaseRequisitionDetail( map ) {
        RequisitionDetail requisitionDetailRequest = map.requisitionDetail
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def requestCode = requisitionDetailRequest.requestCode
            FinanceProcurementHelper.checkCompleteRequisition( requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ) )
            requisitionDetailRequest.userId = user?.oracleUserName
            requisitionDetailRequest.item = requisitionDetailService.getLastItem( requestCode ).next()
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
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ) )
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
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ) )
        Integer item = detailDomainModel.requisitionDetail.item
        // Null or empty check for item.
        if (!item) {
            LoggerUtility.error LOGGER, 'Item is required to update the detail.'
            throw new ApplicationException( RequisitionDetailsCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED, [] ) )
        }
        def existingDetail = requisitionDetailService.findByRequestCodeAndItem( requestCode, item )
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
        if (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO || (financeSystemControl.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_YES
                && StringUtils.isBlank( requisitionDetailRequest.taxGroup ))) {
            requisitionDetailRequest.taxGroup = null
        }
        // Check for Commodity
        requisitionDetailRequest.commodityDescription = requisitionDetailRequest.commodity ? financeCommodityService.findCommodityByCode( requisitionDetailRequest.commodity ).description : null
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
    @Transactional(readOnly = true)
    def findByRequestCode( requisitionCode ) {
        def requisitionDetails = requisitionDetailService.findByRequestCode( requisitionCode )
        def commodityCodes = requisitionDetails.collect() {
            it.commodity
        }
        Map commodityCodeDescMap = financeCommodityService.findCommodityByCodeList( commodityCodes ).collectEntries {
            [it.commodityCode, it.description]
        }
        def getDescription = {commodity ->
            commodityCodeDescMap.get( commodity )
        }
        requisitionDetails.collect() {
            [id                    : it.id,
             requestCode           : it.requestCode,
             version               : it.version,
             additionalChargeAmount: it.additionalChargeAmount,
             amt                   : it.amt,
             commodity             : it.commodity,
             commodityDescription  : it.commodityDescription,
             currency              : it.currency,
             discountAmount        : it.discountAmount,
             item                  : it.item,
             quantity              : it.quantity,
             taxAmount             : it.taxAmount,
             taxGroup              : it.taxGroup,
             unitOfMeasure         : it.unitOfMeasure,
             unitPrice             : it.unitPrice]
        }.each() {
            it.commodityDescription = getDescription( it.commodity )
        }
    }

    /**
     * The method to find requisition code by item with all required data list taxGroup, commodity and unitOfMeasure.
     * @param requestCode requisition code.
     * @param item requisition item number.
     * @return map with all required data.
     */
    def findByRequestCodeAndItem( requestCode, Integer item ) {
        def taxGroup, unitOfMeasure, commodity = []
        def requisitionDetail = requisitionDetailService.findByRequestCodeAndItem( requestCode, item )
        if (requisitionDetail.unitOfMeasure) {
            unitOfMeasure = financeUnitOfMeasureService.findUnitOfMeasureByCode( requisitionDetail.unitOfMeasure )
        }
        if (requisitionDetail.taxGroup) {
            taxGroup = financeTaxCompositeService.getTaxGroupByCode( requisitionDetail.taxGroup )
        }
        if (requisitionDetail.commodity) {
            commodity = financeCommodityService.findCommodityByCode( requisitionDetail.commodity )
        }
        return [requisitionDetail: requisitionDetail,
                taxGroup         : taxGroup,
                unitOfMeasure    : unitOfMeasure,
                commodity        : commodity]
    }
}

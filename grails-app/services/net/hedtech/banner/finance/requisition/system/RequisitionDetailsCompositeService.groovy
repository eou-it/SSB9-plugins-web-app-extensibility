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
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
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
    def requisitionAccountingService
    def financeSystemControlService
    def financeCommodityService
    def financeUnitOfMeasureService
    def financeTaxCompositeService
    def financeCommodityRepeatService
    def chartOfAccountsService
    def financeAccountCompositeService
    def financeTextService
    def financeTextCompositeService
    def requisitionDetailsAcctCommonCompositeService
    def requisitionInformationService

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
            /** Re-balance associated accounting information*/
            reBalanceRequisitionAccounting requestCode, requisitionDetail.item, null
            financeTextCompositeService.saveTextForCommodity( requisitionDetail,
                                                              [privateComment: map.requisitionDetail.privateComment, publicComment: map.requisitionDetail.publicComment],
                                                              user.oracleUserName, requisitionDetail.item )
            return [requestCode: requisitionDetail.requestCode, item: requisitionDetail.item]
        } else {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
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
        /** Delete last accounting if present */
        deleteAccountingForCommodity requestCode, requisitionDetail.item
        requisitionDetailService.delete( [domainModel: requisitionDetail] )
        reBalanceRequisitionAccounting requestCode, item
        financeTextService.delete( financeTextService.getFinanceTextByCodeAndItemNumber( requestCode, item ) )
    }

    /**
     * Update Purchase requisition detail commodity level.
     *
     * @param map the requisition detail map
     */
    def updateRequisitionDetail( detailDomainModel ) {
        def requestCode = detailDomainModel.requisitionDetail.requestCode
        def user = springSecurityService.getAuthentication()?.user
        if (!user.oracleUserName) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionDetailsCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ) )
        Integer item = detailDomainModel.requisitionDetail.item
        // Null or empty check for item.
        if (!item) {
            LoggerUtility.error( LOGGER, 'Item is required to update the detail.' )
            throw new ApplicationException( RequisitionDetailsCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED, [] ) )
        }
        def existingDetail = requisitionDetailService.findByRequestCodeAndItem( requestCode, item )
        RequisitionDetail requisitionDetailRequest = detailDomainModel.requisitionDetail
        requisitionDetailRequest.id = existingDetail.id
        requisitionDetailRequest.version = existingDetail.version
        requisitionDetailRequest.requestCode = existingDetail.requestCode

        requisitionDetailRequest = setDataForCreateOrUpdateRequisitionDetail( requestCode, requisitionDetailRequest )
        requisitionDetailRequest.lastModified = new Date()
        requisitionDetailRequest.item = existingDetail.item
        requisitionDetailRequest.userId = user.oracleUserName
        RequisitionDetail requisitionDetail = requisitionDetailService.update( [domainModel: requisitionDetailRequest] )
        LoggerUtility.debug LOGGER, "Requisition Detail updated " + requisitionDetail
        /** Re-balance associated accounting information*/
        reBalanceRequisitionAccounting requestCode, requisitionDetail.item
        financeTextCompositeService.saveTextForCommodity( requisitionDetail,
                                                          [privateComment: detailDomainModel.requisitionDetail.privateComment, publicComment: detailDomainModel.requisitionDetail.publicComment],
                                                          user.oracleUserName, requisitionDetail.item )
        return requisitionDetail
    }

    /**
     * Delete Accounting for Document level accounting for last commodity detail
     * @param requestCode
     * @return
     */
    private def deleteAccountingForCommodity( requestCode, item ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (header.isDocumentLevelAccounting == FinanceProcurementConstants.TRUE && requisitionDetailService.findByRequestCode( requestCode )?.size() == 1) {
            requisitionAccountingService.findAccountingByRequestCode( requestCode )?.each() {
                requisitionAccountingService.delete( [domainModel: it] )
            }
        } else if (header.isDocumentLevelAccounting == FinanceProcurementConstants.FALSE) {
            requisitionAccountingService.findAccountingByRequestCode( requestCode ).findAll() {
                it.item == item
            }?.each() {
                requisitionAccountingService.delete( [domainModel: it] )
            }
        }
    }

    /**
     * Re-balance the accounting
     *
     * @param requestCode
     * @param item
     * @param isDocumentLevelAccounting
     * @return
     */
    private reBalanceRequisitionAccounting( requestCode, item, isDocumentLevelAccounting = null ) {
        if (!isDocumentLevelAccounting) {
            isDocumentLevelAccounting = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ).isDocumentLevelAccounting
        }
        def accountingList = requisitionAccountingService.findAccountingByRequestCode( requestCode )
        if (isDocumentLevelAccounting == FinanceProcurementConstants.TRUE) {
            adjustPercentageAndProcessAccounting( accountingList )

        } else {
            accountingList = accountingList.findAll() {it.item == item}
            adjustPercentageAndProcessAccounting( accountingList )
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
        // If details have discount code setup then remove the discountAmount value from details
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
        Map commodityCodeDescMap = financeCommodityService.findCommodityByCodeList( requisitionDetails.findAll() {
            it.commodityDescription != null
        }.collect() {
            it.commodity
        }, requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode ).transactionDate ).collectEntries {
            [it.commodityCode, it.description]
        }
        requisitionDetails.collect() {
            [id                    : it.id,
             requestCode           : it.requestCode,
             version               : it.version,
             additionalChargeAmount: it.additionalChargeAmount,
             amt                   : it.amt,
             commodity             : it.commodity,
             commodityDescription  : it.commodityDescription ? it.commodityDescription : getCommodityDescription( commodityCodeDescMap, it.commodity ),
             currency              : it.currency,
             discountAmount        : it.discountAmount,
             item                  : it.item,
             quantity              : it.quantity,
             taxAmount             : it.taxAmount,
             taxGroup              : it.taxGroup,
             unitOfMeasure         : it.unitOfMeasure,
             unitPrice             : it.unitPrice]
        }
    }

    /**Lists Commodity and accounting for specified requisition Code. If provided requisition is of document level accounting, it will first
     * list all commodities for requisition and then all accounting. If provided requisition is of commodity level accounting, it will first
     * list one commodity and its corresponding list of accounting.
     *
     * @param requisitionCode
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def listCommodityWithAccounting( requisitionCode ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode )
        def list = [:]
        if (header.isDocumentLevelAccounting == FinanceProcurementConstants.TRUE) {
            list = listCommodityWithDocumentLevelAccounting( requisitionCode, header.transactionDate )
        } else {
            list = listCommodityWithCommodityLevelAccounting( requisitionCode, header.transactionDate )
        }
        list
    }

    /**
     * The method to find requisition code by item with all required data list taxGroup, commodity and unitOfMeasure.
     * @param requestCode requisition code.
     * @param item requisition item number.
     * @return map with all required data.
     */
    def findByRequestCodeAndItem( requestCode, Integer item ) {
        def taxGroup, unitOfMeasure, commodity = []
        def reqHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        def requisitionDetail = requisitionDetailService.findByRequestCodeAndItem( requestCode, item )
        if (requisitionDetail.unitOfMeasure) {
            unitOfMeasure = financeUnitOfMeasureService.findUnitOfMeasureByCode( requisitionDetail.unitOfMeasure, reqHeader.transactionDate )
        }
        if (requisitionDetail.taxGroup) {
            taxGroup = financeTaxCompositeService.getTaxGroupByCode( requisitionDetail.taxGroup, reqHeader.transactionDate )
        }
        if (requisitionDetail.commodity) {
            commodity = financeCommodityService.findCommodityByCode( requisitionDetail.commodity, reqHeader.transactionDate )
        }
        boolean isCommodityLevelAccounting = !reqHeader.isDocumentLevelAccounting
        def privateComment = FinanceProcurementConstants.EMPTY_STRING
        def publicComment = FinanceProcurementConstants.EMPTY_STRING
        financeTextService.getFinanceTextByCodeAndItemAndPrintOption( requisitionDetail.requestCode,
                                                                      requisitionDetail.item,
                                                                      FinanceValidationConstants.REQUISITION_INDICATOR_NO ).each {
            privateComment = privateComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
        }
        financeTextService.getFinanceTextByCodeAndItemAndPrintOption( requisitionDetail.requestCode,
                                                                      requisitionDetail.item,
                                                                      FinanceValidationConstants.REQUISITION_INDICATOR_YES ).each {
            publicComment = publicComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
        }
        requisitionDetail.commodityDescription = requisitionDetail.commodityDescription ? requisitionDetail.commodityDescription : commodity.description
        return [requisitionDetail            : requisitionDetail,
                quantityDisplay              : FinanceProcurementHelper.getLocaleBasedFormattedNumber( requisitionDetail.quantity, FinanceValidationConstants.TWO ),
                unitPriceDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( requisitionDetail.unitPrice, FinanceValidationConstants.FOUR ),
                extendedAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (requisitionDetail.quantity * requisitionDetail.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ), FinanceValidationConstants.TWO ),
                discountAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( FinanceCommonUtility.nullToZero( requisitionDetail.discountAmount ), FinanceValidationConstants.TWO ),
                taxAmountDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( FinanceCommonUtility.nullToZero( requisitionDetail.taxAmount ), FinanceValidationConstants.TWO ),
                additionalChargeAmountDisplay: FinanceProcurementHelper.getLocaleBasedFormattedNumber( FinanceCommonUtility.nullToZero( requisitionDetail.additionalChargeAmount ), FinanceValidationConstants.TWO ),
                commodityTotalDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (requisitionDetail.quantity * requisitionDetail.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + FinanceCommonUtility.nullToZero( requisitionDetail.taxAmount ) + FinanceCommonUtility.nullToZero( requisitionDetail.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( requisitionDetail.discountAmount ), FinanceValidationConstants.TWO ),
                taxGroup                     : taxGroup,
                unitOfMeasure                : unitOfMeasure,
                hasAccount                   : isCommodityLevelAccounting ? fetchSumOfAccountingTotalPercentage( requisitionAccountingService.findAccountingByRequestCodeAndItem( requestCode, item ) ) >= FinanceValidationConstants.HUNDRED : fetchSumOfAccountingTotalPercentage( requisitionAccountingService.findAccountingByRequestCodeAndItem( requestCode, 0 ) ) >= FinanceValidationConstants.HUNDRED,
                privateComment               : privateComment,
                publicComment                : publicComment,
                status                       : requisitionInformationService.fetchRequisitionsByReqNumber( requestCode )?.status
        ]
    }

    /**
     *
     * @param requisitionCode
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    def private listCommodityWithDocumentLevelAccounting( requisitionCode, headerTnxDate ) {

        def requisitionDetails = []
        try {
            requisitionDetails = requisitionDetailService.findByRequestCode( requisitionCode )
        } catch (ApplicationException ae) {
            LoggerUtility.info( LOGGER, 'No requisition detail available for ' + requisitionCode + ': ' + ae )
        }
        def commodityCodes = requisitionDetails.findAll() {it.commodityDescription == null}.collect() {
            it.commodity
        }
        Map commodityCodeDescMap = financeCommodityService.findCommodityByCodeList( commodityCodes, headerTnxDate ).collectEntries {
            [it.commodityCode, it.description]
        }
        def accounting = requisitionAccountingService.findAccountingByRequestCode( requisitionCode )
        [commodities                  : requisitionDetails.collect() {
            [id                           : it.id,
             requestCode                  : it.requestCode,
             version                      : it.version,
             additionalChargeAmount       : it.additionalChargeAmount,
             additionalChargeAmountDisplay: FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.additionalChargeAmount, FinanceValidationConstants.TWO ),
             amt                          : it.amt,
             commodity                    : [commodity           : it.commodity,
                                             commodityDescription: it.commodityDescription ? it.commodityDescription : getCommodityDescription( commodityCodeDescMap, it.commodity )],
             currency                     : it.currency,
             discountAmount               : it.discountAmount,
             discountAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.discountAmount, FinanceValidationConstants.TWO ),
             item                         : it.item,
             quantity                     : it.quantity,
             quantityDisplay              : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.quantity, FinanceValidationConstants.TWO ),
             taxAmount                    : it.taxAmount,
             taxAmountDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.taxAmount, FinanceValidationConstants.TWO ),
             extendedAmount               : (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ),
             extendedAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ), FinanceValidationConstants.TWO ),
             commodityTotalAmount         : (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ),
             commodityTotalAmountDisplay  : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ), FinanceValidationConstants.TWO ),
             taxGroup                     : it.taxGroup,
             unitOfMeasure                : it.unitOfMeasure,
             unitPrice                    : it.unitPrice,
             unitPriceDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.unitPrice, FinanceValidationConstants.FOUR )]
        },
         accounting                   : accounting.collect() {
             [requestCode                  : it.requestCode,
              item                         : it.item,
              sequenceNumber               : it.sequenceNumber,
              percentage                   : it.percentage,
              requisitionAmount            : it.requisitionAmount,
              requisitionAmountDisplay     : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.requisitionAmount, FinanceValidationConstants.TWO ),
              chartOfAccount               : it.chartOfAccount,
              accountIndex                 : it.accountIndex,
              fund                         : it.fund,
              organization                 : it.organization,
              account                      : it.account,
              program                      : it.program,
              activity                     : it.activity,
              location                     : it.location,
              project                      : it.project,
              discountAmount               : it.discountAmount,
              discountAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.discountAmount, FinanceValidationConstants.TWO ),
              taxAmount                    : it.taxAmount,
              taxAmountDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.taxAmount, FinanceValidationConstants.TWO ),
              accountingTotalAmount        : it.requisitionAmount + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ),
              accountingTotalAmountDisplay : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.requisitionAmount + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ), FinanceValidationConstants.TWO ),
              additionalChargeAmount       : it.additionalChargeAmount,
              additionalChargeAmountDisplay: FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.additionalChargeAmount, FinanceValidationConstants.TWO ),
              discountAmountPercent        : it.discountAmountPercent,
              additionalChargeAmountPct    : it.additionalChargeAmountPct,
              taxAmountPercent             : it.taxAmountPercent]
         },
         commodityTotal               : fetchSumOfCommodityTotal( requisitionDetails ),
         commodityTotalDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( fetchSumOfCommodityTotal( requisitionDetails ), FinanceValidationConstants.TWO ),
         accountingTotal              : fetchSumOfAccountingTotal( accounting ),
         accountingTotalDisplay       : FinanceProcurementHelper.getLocaleBasedFormattedNumber( fetchSumOfAccountingTotal( accounting ), FinanceValidationConstants.TWO ),
         accountingTotalPercentage    : fetchSumOfAccountingTotalPercentage( accounting ),
         remainingAccountingPercentage: FinanceProcurementConstants.HUNDRED - fetchSumOfAccountingTotalPercentage( accounting ),
         status                       : requisitionInformationService.fetchRequisitionsByReqNumber( requisitionCode )?.status]
    }

/**
 * List commodities detail for Commodity level accounting
 * @param headerTxnDate
 * @param requisitionCode
 */
    @Transactional(propagation = Propagation.SUPPORTS)
    def private listCommodityWithCommodityLevelAccounting( requisitionCode, headerTxnDate ) {
        def requisitionDetails = []
        try {
            requisitionDetails = requisitionDetailService.findByRequestCode( requisitionCode )
        } catch (ApplicationException ae) {
            LoggerUtility.info( LOGGER, 'No requisition detail available for ' + requisitionCode + ': ' + ae )
        }
        def commodityCodes = requisitionDetails.findAll() {it.commodityDescription == null}.collect() {
            it.commodity
        }
        Map commodityCodeDescMap = financeCommodityService.findCommodityByCodeList( commodityCodes, headerTxnDate ).collectEntries {
            [it.commodityCode, it.description]
        }
        Map accountingMap = requisitionAccountingService.findAccountingByRequestCode( requisitionCode ).collectEntries {
            [it.item + FinanceValidationConstants.COLON + it.sequenceNumber, [requestCode                  : it.requestCode,
                                                                              item                         : it.item,
                                                                              sequenceNumber               : it.sequenceNumber,
                                                                              percentage                   : it.percentage,
                                                                              requisitionAmount            : it.requisitionAmount,
                                                                              requisitionAmountDisplay     : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.requisitionAmount, FinanceValidationConstants.TWO ),
                                                                              chartOfAccount               : it.chartOfAccount,
                                                                              accountIndex                 : it.accountIndex,
                                                                              fund                         : it.fund,
                                                                              organization                 : it.organization,
                                                                              account                      : it.account,
                                                                              program                      : it.program,
                                                                              activity                     : it.activity,
                                                                              location                     : it.location,
                                                                              project                      : it.project,
                                                                              discountAmount               : it.discountAmount,
                                                                              discountAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.discountAmount, FinanceValidationConstants.TWO ),
                                                                              taxAmount                    : it.taxAmount,
                                                                              taxAmountDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.taxAmount, FinanceValidationConstants.TWO ),
                                                                              accountingTotalAmount        : it.requisitionAmount + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ),
                                                                              accountingTotalAmountDisplay : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.requisitionAmount + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ), FinanceValidationConstants.TWO ),
                                                                              additionalChargeAmount       : it.additionalChargeAmount,
                                                                              additionalChargeAmountDisplay: FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.additionalChargeAmount, FinanceValidationConstants.TWO ),
                                                                              discountAmountPercent        : it.discountAmountPercent,
                                                                              additionalChargeAmountPct    : it.additionalChargeAmountPct,
                                                                              taxAmountPercent             : it.taxAmountPercent]]
        }

        def getAccountingForCommodityItem = {commodityItem ->
            accountingMap.findAll() {
                it.key.tokenize( FinanceValidationConstants.COLON )[0] == commodityItem.toString()
            }.collect() {it -> it.value}
        }
        def commodityRepeatMap = financeCommodityRepeatService.findCommodityRepeatByEffectiveDate( headerTxnDate )?.collectEntries() {
            [it.commodityCode, it]
        }
        def getCOACode = {
            commodityRepeatMap?.get( it )?.coaCode
        }
        def getAccountCode = {
            commodityRepeatMap?.get( it )?.accountCode
        }

        [status     : requisitionInformationService.fetchRequisitionsByReqNumber( requisitionCode )?.status,
         commodities: requisitionDetails.collect() {
             [id                           : it.id,
              requestCode                  : it.requestCode,
              version                      : it.version,
              additionalChargeAmount       : it.additionalChargeAmount,
              additionalChargeAmountDisplay: FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.additionalChargeAmount, FinanceValidationConstants.TWO ),
              amt                          : it.amt,
              commodity                    : [commodity           : it.commodity,
                                              commodityDescription: it.commodityDescription ? it.commodityDescription : getCommodityDescription( commodityCodeDescMap, it.commodity ),
                                              coaCode             : getCOACode( it.commodity ),
                                              coaDescription      : getCOACode( it.commodity )
                                                      ? chartOfAccountsService.getChartOfAccountByCode( getCOACode( it.commodity ), headerTxnDate )?.title
                                                      : null,
                                              accountCode         : getAccountCode( it.commodity ),
                                              accountDescription  : getAccountCode( it.commodity )
                                                      ? financeAccountCompositeService.getListByAccountOrChartOfAccAndEffectiveDate(
                                                      [searchParam  : getAccountCode( it.commodity ),
                                                       coaCode      : getCOACode( it.commodity ),
                                                       effectiveDate: headerTxnDate], [max: 1, offset: 0] )?.get( 0 )?.title
                                                      : null
              ],
              currency                     : it.currency,
              discountAmount               : it.discountAmount,
              discountAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.discountAmount, FinanceValidationConstants.TWO ),
              item                         : it.item,
              quantity                     : it.quantity,
              quantityDisplay              : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.quantity, FinanceValidationConstants.TWO ),
              taxAmount                    : it.taxAmount,
              taxAmountDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.taxAmount, FinanceValidationConstants.TWO ),
              extendedAmount               : (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ),
              extendedAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ), FinanceValidationConstants.TWO ),
              commodityTotalAmount         : (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ),
              commodityTotalAmountDisplay  : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount ), FinanceValidationConstants.TWO ),
              taxGroup                     : it.taxGroup,
              unitOfMeasure                : it.unitOfMeasure,
              unitPrice                    : it.unitPrice,
              unitPriceDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.unitPrice, FinanceValidationConstants.FOUR ),
              accounting                   : []
             ]
         }.each() {
             it.accounting = getAccountingForCommodityItem( it.item )
             it.commodityTotal = it.commodityTotalAmount
             it.commodityTotalDisplay = FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityTotalAmount, FinanceValidationConstants.TWO )
             it.accountingTotal = fetchSumOfAccountingTotal( getAccountingForCommodityItem( it.item ) )
             it.accountingTotalDisplay = FinanceProcurementHelper.getLocaleBasedFormattedNumber( fetchSumOfAccountingTotal( getAccountingForCommodityItem( it.item ) ), FinanceValidationConstants.TWO )
             it.accountingTotalPercentage = fetchSumOfAccountingTotalPercentage( getAccountingForCommodityItem( it.item ) )
             it.remainingAccountingPercentage = FinanceProcurementConstants.HUNDRED - fetchSumOfAccountingTotalPercentage( getAccountingForCommodityItem( it.item ) )
         }]
    }

    /**
     * Gets Sum of accounting percentage total. Need to provide accounting List
     */
    private final def fetchSumOfAccountingTotalPercentage = {accountingList ->
        def sum = 0.0
        accountingList.each {
            sum += it.percentage
        }
        sum
    }

    /**
     * Gets Sum of accounting total. Need to provide accounting List
     */
    private final def fetchSumOfAccountingTotal = {accountingList ->
        def sum = 0.0
        accountingList.each {it ->
            sum += it.requisitionAmount + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount )
        }
        sum
    }

    /**
     * Gets Sum of commodity total. Need to provide commodity List
     */
    private final def fetchSumOfCommodityTotal = {commodityList ->
        def sum = 0.0
        commodityList.each {it ->
            sum += (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + FinanceCommonUtility.nullToZero( it.taxAmount ) + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount )
        }
        sum
    }

    /**
     * Gets commodity description. Need to provide description Map and commodity code
     */
    private final def getCommodityDescription = {commodityCodeDescMap, commodity ->
        commodityCodeDescMap.get( commodity )
    }

    /**
     * Process accounting. Sets amount that is adjusted one
     */
    private final def processAccounting = {accounting ->
        accounting.requisitionAmount = null
        accounting.discountAmount = null
        accounting.taxAmount = null
        accounting.additionalChargeAmount = null
        requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( accounting )
        requisitionAccountingService.update( [domainModel: accounting] )
    }

    /**
     * Adjust accounting amount
     */
    private final def adjustPercentageAndProcessAccounting = {accountingList ->
        def percentageBeforeAdjustment = 0.0, percentageAfterAdjustment = 0.0, diff = 0.0
        accountingList?.each() {
            percentageBeforeAdjustment += it.percentage
            processAccounting( it )
            percentageAfterAdjustment += it.percentage
        }
        diff = percentageAfterAdjustment - percentageBeforeAdjustment
        if (accountingList.size > 0 && diff != 0.0) {
            def lastIndex = accountingList.size() - 1
            accountingList[lastIndex].percentage = accountingList[lastIndex].percentage - diff
            processAccounting( accountingList[lastIndex] )
        }
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.i18n.MessageHelper
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * Service class for RequisitionSummary.
 *
 */
class RequisitionSummaryService extends ServiceBase {
    boolean transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService
    def shipToCodeService
    def requisitionInformationService
    def financeUserProfileService
    def financeOrganizationCompositeService
    def financeTextService

    /**
     * Find the requisition summary for specified requestCode
     * @param requestCode
     */
    def fetchRequisitionSummaryForRequestCode( requestCode, baseCcy, doesNotNeedPdf = true ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for fetchRequisitionSummaryForRequestCode :' + requestCode )
        def requisitionSummary = RequisitionSummary.fetchRequisitionSummaryForRequestCode( requestCode )
        if (!requisitionSummary) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        processSummaryInformation( requisitionSummary, baseCcy, requestCode, doesNotNeedPdf )
    }

    /**
     * Process and topologies Requisition Summary
     * @param requisitionSummary
     * @param requestCode
     */
    private def processSummaryInformation( requisitionSummary, baseCcy, requestCode, boolean doesNotNeedPdf ) {
        def retJSON = [:]
        def processComment = {list ->
            def existingPublicComment = FinanceProcurementConstants.EMPTY_STRING
            list.each() {
                existingPublicComment = existingPublicComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
            }
            existingPublicComment
        }
        def headerRecord = requisitionSummary[0], shipToCodeMap = [:], userProfileMap = [:], orgMap = [:], headerTextMap = [:], statusMap = [:]
        if (!doesNotNeedPdf) {
            shipToCodeMap[headerRecord.requestCode] = shipToCodeService.findShipToCodesByCode( headerRecord.shipToCode, headerRecord.transactionDate ).collect() {
                [zipCode     : it.zipCode, state: it.state, city: it.city,
                 shipCode    : it.shipCode, addressLine1: it.addressLine1,
                 addressLine2: it.addressLine2, addressLine3: it.addressLine3,
                 contact     : it.contact]
            }
            userProfileMap[headerRecord.requestCode] = financeUserProfileService.getUserProfileByUserId( springSecurityService.getAuthentication()?.user?.oracleUserName ).collect() {userProfileObj ->
                [userId           : userProfileObj.userId, requesterName: userProfileObj.requesterName, requesterPhoneNumber: userProfileObj.requesterPhoneNumber,
                 requesterPhoneExt: userProfileObj.requesterPhoneExt, requesterEmailAddress: userProfileObj.requesterEmailAddress]
            }
            orgMap[headerRecord.requestCode] = financeOrganizationCompositeService.
                    findOrganizationListByEffectiveDateAndSearchParam( [searchParam: headerRecord.organizationCode, effectiveDate: headerRecord.transactionDate,
                                                                        coaCode    : headerRecord.chartOfAccountCode],
                                                                       [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] ).collect() {organization ->
                [orgnCode: organization.orgnCode, orgnTitle: organization.orgnTitle]
            }
            headerTextMap[headerRecord.requestCode] = processComment( financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd( headerRecord.requestCode,
                                                                                                                                     FinanceValidationConstants.REQUISITION_INDICATOR_YES ) )
            statusMap[headerRecord.requestCode] = MessageHelper.message( 'purchaseRequisition.status.' + requisitionInformationService.fetchRequisitionsByReqNumber( headerRecord.requestCode ).status )
        }
        requisitionSummary.collectEntries() {
            [it.requestCode, [
                    version                  : it.version,
                    isDocumentLevelAccounting: it.isDocumentLevelAccounting,
                    requestCode              : it.requestCode,
                    vendorPidm               : it.vendorPidm,
                    attentionTo              : it.attentionTo,
                    vendorCode               : it.vendorCode,
                    shipTo                   : shipToCodeMap[it.requestCode],
                    requester                : userProfileMap[it.requestCode],
                    organization             : orgMap[it.requestCode],
                    headerComment            : headerTextMap[it.requestCode],
                    transactionDate          : it.transactionDate,
                    ccy                      : doesNotNeedPdf ? null : it.ccyCode ? it.ccyCode : baseCcy,
                    deliveryDate             : it.deliveryDate,
                    status                   : statusMap[it.requestCode],
                    vendorAddressTypeSequence: it.vendorAddressTypeSequence,
                    vendorAddressTypeCode    : it.vendorAddressTypeCode,
                    vendorLastName           : it.vendorLastName,
                    vendorAddressLine1       : it.vendorAddressLine1,
                    vendorAddressLine2       : it.vendorAddressLine2,
                    vendorAddressLine3       : it.vendorAddressLine3,
                    vendorAddressZipCode     : it.vendorAddressZipCode,
                    vendorAddressStateCode   : it.vendorAddressStateCode,
                    vendorAddressCity        : it.vendorAddressCity,
                    vendorPhoneNumber        : it.vendorPhoneNumber,
                    vendorPhoneExtension     : it.vendorPhoneExtension,
                    commodityItem            : it.commodityItem,
                    accountingItem           : it.accountingItem]]
        }.each() {
            key, value ->
                retJSON['header'] = value
        }
        if (retJSON['header'].commodityItem) {
            def accountingList = []
            def accountingMap = [:]
            def commodityList = []
            if (retJSON['header'].accountingItem >= 0) {
                accountingMap = requisitionSummary.collectEntries() {
                    [it.accountingItem + ':' + it.accountingSequenceNumber, [
                            accountingItem                  : it.accountingItem,
                            accountingSequenceNumber        : it.accountingSequenceNumber,
                            accountingPercentage            : it.accountingPercentage,
                            accountingPercentageDisplay     : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.accountingPercentage, FinanceValidationConstants.FOUR ),
                            accountingAmount                : it.accountingAmount,
                            accountingCoaCode               : it.accountingCoaCode,
                            accountingIndexCode             : it.accountingIndexCode,
                            accountingFundCode              : it.accountingFundCode,
                            accountingOrgCode               : it.accountingOrgCode,
                            accountingAccountCode           : it.accountingAccountCode,
                            accountingActivityCode          : it.accountingActivityCode,
                            accountingProjectCode           : it.accountingProjectCode,
                            accountingProgramCode           : it.accountingProgramCode,
                            accountingDiscountAmount        : it.accountingDiscountAmount,
                            accountingAdditionalChargeAmount: it.accountingAdditionalChargeAmount,
                            accountingTaxAmount             : it.accountingTaxAmount,
                            accountingTotal                 : it.accountingAmount + it.accountingAdditionalChargeAmount + it.accountingTaxAmount
                                    - it.accountingDiscountAmount,
                            accountingTotalDisplay          : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.accountingAmount + it.accountingAdditionalChargeAmount + it.accountingTaxAmount
                                                                                                                              - it.accountingDiscountAmount, FinanceValidationConstants.TWO )]]
                }.each() {
                    key, value ->
                        accountingList.add( value )
                }
            }

            /** closure to get accounting for item*/
            def getAccountingForCommodityItem = {commodityItem ->
                accountingMap.findAll() {
                    it.key.tokenize( FinanceValidationConstants.COLON )[0] == commodityItem.toString()
                }.collect() {it -> it.value}
            }

            /** closure to get distribution percentage*/
            def getAccountingDistributionPercentage = {list ->
                def sum = 0;
                list.each() {
                    sum += it.accountingPercentage
                }
                sum
            }

            /** closure to get All accounting  total*/
            def getAllAccountingTotal = {list ->
                def sum = 0;
                list.each() {
                    sum += it.accountingTotal
                }
                sum
            }

            /** closure to get Commodity Total*/
            def getAllCommodityTotal = {list ->
                def sum = 0;
                list.each() {
                    sum += it.commodityTotal
                }
                sum
            }
            /** closure to check if all items are balanced*/
            def checkIfAllItemBalanced = {list, isLastItemBalanced ->
                list.each() {
                    isLastItemBalanced = isLastItemBalanced && it.itemBalanced
                }
                isLastItemBalanced
            }
            boolean isCommodityLevelAccounting = !retJSON['header'].isDocumentLevelAccounting
            def commodityTextMap = [:]
            if (!doesNotNeedPdf) {
                requisitionSummary.collectEntries() {
                    [it.commodityItem, it.commodityItem]
                }.each {key, value ->
                    commodityTextMap[key] = processComment( financeTextService.getFinanceTextByCodeAndItemAndPrintOption( requestCode, key.intValue(),
                                                                                                                          FinanceValidationConstants.REQUISITION_INDICATOR_YES ) )
                }
            }
            requisitionSummary.collectEntries() {
                [it.commodityItem, [
                        commodityItem                         : it.commodityItem,
                        commodityCode                         : it.commodityCode,
                        commodityDescription                  : it.commodityDescription,
                        commodityCodeDesc                     : it.commodityDescription ? it.commodityDescription : it.commodityCodeDesc,
                        commodityQuantityDisplay              : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityQuantity, FinanceValidationConstants.TWO ),
                        commodityQuantity                     : it.commodityQuantity,
                        commodityQuantityDisplay              : FinanceProcurementHelper.getLocaleBasedFormattedNumber(it.commodityQuantity, FinanceValidationConstants.TWO ),
                        unitOfMeasure                         : it.unitOfMeasure,
                        commodityDiscountAmount               : it.commodityDiscountAmount,
                        commodityDiscountAmountDisplay        : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityDiscountAmount, FinanceValidationConstants.TWO ),
                        othersDisplay                         : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityAdditionalChargeAmount + it.commodityTaxAmount - it.commodityDiscountAmount, FinanceValidationConstants.TWO ),
                        commodityAdditionalChargeAmount       : it.commodityAdditionalChargeAmount,
                        commodityAdditionalChargeAmountDisplay: FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityAdditionalChargeAmount, FinanceValidationConstants.TWO ),
                        commodityText                         : commodityTextMap[it.commodityItem],
                        commodityTaxAmount                    : it.commodityTaxAmount,
                        commodityTaxAmountDisplay :FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityTaxAmount, FinanceValidationConstants.TWO ),
                        commodityUnitPrice                    : it.commodityUnitPrice,
                        commodityUnitPriceDisplay             : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.commodityUnitPrice, FinanceValidationConstants.FOUR ),
                        commodityTotal                        : (it.commodityUnitPrice * it.commodityQuantity).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + it.commodityTaxAmount + it.commodityAdditionalChargeAmount
                                - it.commodityDiscountAmount,
                        commodityTotalDisplay                 : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (it.commodityUnitPrice * it.commodityQuantity).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + it.commodityTaxAmount + it.commodityAdditionalChargeAmount
                                                                                                                                - it.commodityDiscountAmount, FinanceValidationConstants.TWO ),
                        accounting                            : isCommodityLevelAccounting ? getAccountingForCommodityItem( it.commodityItem ) : null,
                        distributionPercentageDisplay         : isCommodityLevelAccounting ? FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAccountingDistributionPercentage( getAccountingForCommodityItem( it.commodityItem ) ), FinanceValidationConstants.FOUR ) : null,
                        distributionPercentage                : isCommodityLevelAccounting ? getAccountingDistributionPercentage( getAccountingForCommodityItem( it.commodityItem ) ) : null,
                        allAccountingTotalDisplay             : isCommodityLevelAccounting ? FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllAccountingTotal( getAccountingForCommodityItem( it.commodityItem ) ), FinanceValidationConstants.TWO ) : null,
                        allAccountingTotal                    : isCommodityLevelAccounting ? getAllAccountingTotal( getAccountingForCommodityItem( it.commodityItem ) ) : null,
                        itemBalanced                          : isCommodityLevelAccounting ? getAccountingDistributionPercentage( getAccountingForCommodityItem( it.commodityItem ) ) == FinanceValidationConstants.HUNDRED : null]]

            }.each() {
                key, value ->
                    commodityList.add( value )
            }
            retJSON['commodity'] = commodityList
            if (retJSON['header'].isDocumentLevelAccounting) {
                retJSON['accounting'] = accountingList
                retJSON['distributionPercentage'] = getAccountingDistributionPercentage( accountingList )
                retJSON['distributionPercentageDisplay'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAccountingDistributionPercentage( accountingList ), FinanceValidationConstants.FOUR )
                retJSON['allAccountingTotal'] = getAllAccountingTotal( accountingList )
                retJSON['allAccountingTotalDisplay'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllAccountingTotal( accountingList ), FinanceValidationConstants.TWO )
                retJSON['allCommodityTotal'] = getAllCommodityTotal( commodityList )
                retJSON['allCommodityTotalDisplay'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllCommodityTotal( commodityList ), FinanceValidationConstants.TWO )
                retJSON['balanced'] = retJSON['distributionPercentage'] == FinanceValidationConstants.HUNDRED
                retJSON['commodity'].each { // Clean unnecessary keys for commodity
                    it.remove( 'accounting' )
                    it.remove( 'itemBalanced' )
                }
            } else {
                retJSON['balanced'] = checkIfAllItemBalanced( commodityList, accountingList.size() > 0 )
                retJSON['grandCommodityTotal'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllCommodityTotal( commodityList ), FinanceValidationConstants.TWO )
                retJSON['grandAccountingTotal'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllAccountingTotal( accountingList ), FinanceValidationConstants.TWO )
                retJSON['grandCommodityTotalDisplay'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllCommodityTotal( commodityList ), FinanceValidationConstants.TWO )
                retJSON['grandAccountingTotalDisplay'] = FinanceProcurementHelper.getLocaleBasedFormattedNumber( getAllAccountingTotal( accountingList ), FinanceValidationConstants.TWO )
            }
            // Clean unnecessary keys for header
            retJSON['header'].remove( 'commodityItem' )
            retJSON['header'].remove( 'accountingItem' )
        }
        retJSON
    }
}

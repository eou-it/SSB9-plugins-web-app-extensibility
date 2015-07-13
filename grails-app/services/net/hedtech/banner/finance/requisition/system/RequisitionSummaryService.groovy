/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
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
    def fetchRequisitionSummaryForRequestCode( requestCode, needPdf = false ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for fetchRequisitionSummaryForRequestCode :' + requestCode )
        def requisitionSummary = RequisitionSummary.fetchRequisitionSummaryForRequestCode( requestCode )
        if (!requisitionSummary) {
            throw new ApplicationException( RequisitionHeaderService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        processSummaryInformation( requisitionSummary, requestCode, needPdf )
    }

    /**
     * Process and topologies Requisition Summary
     * @param requisitionSummary
     * @param requestCode
     */
    private def processSummaryInformation( requisitionSummary, requestCode, boolean needPdf ) {
        def retJSON = [:]
        def processComment = {list ->
            def existingPublicComment = FinanceProcurementConstants.EMPTY_STRING
            list.each() {
                existingPublicComment = existingPublicComment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
            }
            existingPublicComment
        }
        requisitionSummary.collectEntries() {
            [it.requestCode, [
                    version                  : it.version,
                    isDocumentLevelAccounting: it.isDocumentLevelAccounting,
                    requestCode              : it.requestCode,
                    vendorPidm               : it.vendorPidm,
                    attentionTo              : it.attentionTo,
                    vendorCode               : it.vendorCode,
                    shipTo                   : !needPdf ? null : shipToCodeService.findShipToCodesByCode( it.shipToCode, it.transactionDate ).collect() {
                        [zipCode     : it.zipCode, state: it.state, city: it.city,
                         shipCode    : it.shipCode, addressLine1: it.addressLine1,
                         addressLine2: it.addressLine2, addressLine3: it.addressLine3,
                         contact     : it.contact]

                    },
                    requester                : !needPdf ? null : financeUserProfileService.getUserProfileByUserId( springSecurityService.getAuthentication()?.user?.oracleUserName ).collect() {userProfileObj ->
                        [userId           : userProfileObj.userId, requesterName: userProfileObj.requesterName, requesterPhoneNumber: userProfileObj.requesterPhoneNumber,
                         requesterPhoneExt: userProfileObj.requesterPhoneExt, requesterEmailAddress: userProfileObj.requesterEmailAddress]
                    },
                    organization             : !needPdf ? null : financeOrganizationCompositeService.
                            findOrganizationListByEffectiveDateAndSearchParam( [searchParam: it.organizationCode, effectiveDate: it.transactionDate, coaCode: it.chartOfAccountCode],
                                                                               [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] ).collect() {organization ->
                        [orgnCode: organization.orgnCode, orgnTitle: organization.orgnTitle]
                    },
                    headerComment            : !needPdf ? null : processComment( financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd( it.requestCode,
                                                                                                                                                FinanceValidationConstants.REQUISITION_INDICATOR_YES ) ),
                    transactionDate          : it.transactionDate,
                    deliveryDate             : it.deliveryDate,
                    status                   : !needPdf ? null : requisitionInformationService.fetchRequisitionsByReqNumber( it.requestCode ).status,
                    vendorAddressTypeSequence: it.vendorAddressTypeSequence,
                    vendorAddressTypeCode    : it.vendorAddressTypeCode,
                    vendorLastName           : it.vendorLastName,
                    vendorAddressLine1       : it.vendorAddressLine1,
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
                                    - it.accountingDiscountAmount]]
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
            def getAllAccountingTotalForCommodityItem = {list ->
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
            requisitionSummary.collectEntries() {
                [it.commodityItem, [
                        commodityItem                  : it.commodityItem,
                        commodityCode                  : it.commodityCode,
                        commodityDescription           : it.commodityDescription,
                        commodityCodeDesc              : it.commodityDescription ? it.commodityDescription : it.commodityCodeDesc,
                        commodityQuantity              : it.commodityQuantity,
                        unitOfMeasure                  : it.unitOfMeasure,
                        commodityDiscountAmount        : it.commodityDiscountAmount,
                        commodityAdditionalChargeAmount: it.commodityAdditionalChargeAmount,
                        commodityText                  : !needPdf ? null : processComment( financeTextService.getFinanceTextByCodeAndItemAndPrintOption( requestCode, it.commodityItem.intValue(),
                                                                                                                                                         FinanceValidationConstants.REQUISITION_INDICATOR_YES ) ),
                        commodityTaxAmount             : it.commodityTaxAmount,
                        commodityUnitPrice             : it.commodityUnitPrice,
                        commodityTotal                 : (it.commodityUnitPrice * it.commodityQuantity).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + it.commodityTaxAmount + it.commodityAdditionalChargeAmount
                                - it.commodityDiscountAmount,
                        accounting                     : isCommodityLevelAccounting ? getAccountingForCommodityItem( it.commodityItem ) : null,
                        distributionPercentage         : isCommodityLevelAccounting ? getAccountingDistributionPercentage( getAccountingForCommodityItem( it.commodityItem ) ) : null,
                        allAccountingTotal             : isCommodityLevelAccounting ? getAllAccountingTotalForCommodityItem( getAccountingForCommodityItem( it.commodityItem ) ) : null,
                        itemBalanced                   : isCommodityLevelAccounting ? getAccountingDistributionPercentage( getAccountingForCommodityItem( it.commodityItem ) ) == FinanceValidationConstants.HUNDRED : null]]

            }.each() {
                key, value ->
                    commodityList.add( value )
            }
            retJSON['commodity'] = commodityList
            if (retJSON['header'].isDocumentLevelAccounting) {
                retJSON['accounting'] = accountingList
                retJSON['distributionPercentage'] = getAccountingDistributionPercentage( accountingList )
                retJSON['allAccountingTotal'] = getAllAccountingTotalForCommodityItem( accountingList )
                retJSON['allCommodityTotal'] = getAllCommodityTotal( commodityList )
                retJSON['balanced'] = retJSON['distributionPercentage'] == FinanceValidationConstants.HUNDRED
                retJSON['commodity'].each { // Clean unnecessary keys for commodity
                    it.remove( 'accounting' )
                    it.remove( 'itemBalanced' )
                }
            } else {
                retJSON['balanced'] = checkIfAllItemBalanced( commodityList, accountingList.size() > 0 )
            }
            // Clean unnecessary keys for header
            retJSON['header'].remove( 'commodityItem' )
            retJSON['header'].remove( 'accountingItem' )
        }
        retJSON
    }
}

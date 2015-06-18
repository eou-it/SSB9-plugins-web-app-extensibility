/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Accounting Composite
 */
class RequisitionAccountingCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    boolean transactional = true

    def requisitionHeaderService
    def springSecurityService
    def requisitionAccountingService
    def requisitionDetailService
    def chartOfAccountsService
    def financeOrganizationCompositeService
    def financeAccountCompositeService
    def programService
    def activityService
    def locationService
    def financeProjectCompositeService
    def financeAccountIndexService
    def financeFundCompositeService
    def financeUserProfileService

    /**
     * Creates Requisition Accounting level.
     *
     * @param map Map which have all the required date to create requisition accounting.
     * @return map Map having requestCode, item and sequenceNumber of created requisition accounting.
     */
    def createPurchaseRequisitionAccounting( map ) {
        RequisitionAccounting requisitionAccountingRequest = map.requisitionAccounting
        def user = springSecurityService.getAuthentication()?.user
        if (user?.oracleUserName) {
            requisitionAccountingRequest.userId = user.oracleUserName
            def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionAccountingRequest.requestCode )
            FinanceProcurementHelper.checkCompleteRequisition( header )
            if (header?.isDocumentLevelAccounting) {
                requisitionAccountingRequest.item = 0
            }
            requisitionAccountingRequest.sequenceNumber = requisitionAccountingService.getLastSequenceNumberByRequestCode( requisitionAccountingRequest.requestCode, requisitionAccountingRequest.item ).next()
            setNSFOverride( requisitionAccountingRequest, user.oracleUserName )
            adjustAccountPercentageAndAmount( requisitionAccountingRequest )
            RequisitionAccounting requisitionAccounting = requisitionAccountingService.create( [domainModel: requisitionAccountingRequest] )
            LoggerUtility.debug( LOGGER, 'Requisition Accounting created ' + requisitionAccounting )
            return [requestCode: requisitionAccounting.requestCode,
                    item       : requisitionAccounting.item, sequenceNumber: requisitionAccounting.sequenceNumber]
        } else {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException(
                    RequisitionAccountingCompositeService,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * Delete Purchase Requisition Accounting Information.
     * @param requestCode Requisition Code.
     * @param item Item.
     * @param sequenceNumber Sequence number.
     */
    def deletePurchaseRequisitionAccountingInformation( requestCode, Integer item, Integer sequenceNumber ) {
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ) )
        def requisitionAccounting = requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequenceNumber )
        requisitionAccountingService.delete( [domainModel: requisitionAccounting] )
    }

    /**
     * Updates Purchase requisition Accounting information.
     *
     * @param map the requisition accounting map
     */
    def updateRequisitionAccounting( accountingDomainModel ) {
        // Null or empty check for item number and sequence number.
        FinanceProcurementHelper.checkCompleteRequisition(
                requisitionHeaderService.findRequisitionHeaderByRequestCode( accountingDomainModel.requisitionAccounting.requestCode ) )

        if (accountingDomainModel.requisitionAccounting.item == null || accountingDomainModel.requisitionAccounting.sequenceNumber == null) {
            LoggerUtility.error( LOGGER, 'Item and Sequence number are required to update the Requisition Accounting information.' )
            throw new ApplicationException( RequisitionAccountingCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED, [] ) )
        }
        Integer item = 0
        if (!accountingDomainModel.requisitionAccounting.item instanceof Integer) {
            item = Integer.parseInt( accountingDomainModel.requisitionAccounting.item )
        } else {
            item = accountingDomainModel.requisitionAccounting.item
        }
        Integer sequenceNumber = 0
        if (!accountingDomainModel.requisitionAccounting.sequenceNumber instanceof Integer) {
            sequenceNumber = Integer.parseInt( accountingDomainModel.requisitionAccounting.sequenceNumber )
        } else {
            sequenceNumber = accountingDomainModel.requisitionAccounting.sequenceNumber
        }

        def existingAccountingInfo = requisitionAccountingService.findByRequestCodeItemAndSeq( accountingDomainModel.requisitionAccounting.requestCode, item, sequenceNumber )
        RequisitionAccounting requisitionAccountingRequest = accountingDomainModel.requisitionAccounting
        requisitionAccountingRequest.id = existingAccountingInfo.id
        requisitionAccountingRequest.version = existingAccountingInfo.version
        requisitionAccountingRequest.requestCode = existingAccountingInfo.requestCode
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            requisitionAccountingRequest.lastModified = new Date()
            requisitionAccountingRequest.item = existingAccountingInfo.item
            requisitionAccountingRequest.sequenceNumber = existingAccountingInfo.sequenceNumber
            requisitionAccountingRequest.userId = user.oracleUserName
            setNSFOverride( requisitionAccountingRequest, user.oracleUserName )
            adjustAccountPercentageAndAmount( requisitionAccountingRequest )
            def requisitionAccounting = requisitionAccountingService.update( [domainModel: requisitionAccountingRequest] )
            LoggerUtility.debug( LOGGER, "Requisition Accounting information updated " + requisitionAccounting )
            return requisitionAccounting
        } else {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionAccountingCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * This method is used to find RequisitionAccounting and related details by requisition code, item number and sequence number.
     * @param requisitionCode Requisition code.
     * @param item Item number.
     * @param sequenceNumber Sequence number.
     * @return RequisitionAccounting information.
     */
    def findByRequestCodeItemAndSeq( requisitionCode, Integer item, Integer sequenceNumber ) {
        LoggerUtility.debug( LOGGER, String.format( 'Input parameter for findByRequestCodeItemAndSeq :%1s , %2d ,%3d', requisitionCode, item, sequenceNumber ) )
        def dummyPaginationParam = [max: 1, offset: 0]
        def requisitionAccounting = findCompleteAccountingByRequestCodeItemAndSeq( requisitionCode, item, sequenceNumber )
        def financeAccountIndex = financeAccountIndexService.listByIndexCodeOrTitleAndEffectiveDate( [coaCode: requisitionAccounting.chartOfAccount, indexCodeTitle: requisitionAccounting.accountIndex], dummyPaginationParam )?.get( 0 )
        def financeFund = financeFundCompositeService.findFundListByEffectiveDateAndFundCode( [effectiveDate: null, codeTitle: requisitionAccounting.fund, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )
        def financeOrganization = financeOrganizationCompositeService.findOrganizationListByEffectiveDateAndSearchParam( [searchParam  : requisitionAccounting.organization,
                                                                                                                          effectiveDate: requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode ).transactionDate,
                                                                                                                          coaCode      : requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )

        [accounting: requisitionAccounting, cifoapalp: [
                chartOfAccount: [code: requisitionAccounting.chartOfAccount, title: requisitionAccounting.chartOfAccount ? chartOfAccountsService.getChartOfAccountByCode( requisitionAccounting.chartOfAccount )?.title : null],
                index         : [
                        code                       : requisitionAccounting.accountIndex,
                        title                      : requisitionAccounting.accountIndex ? financeAccountIndex?.title : null,
                        accountIndexCode           : financeAccountIndex?.accountIndexCode,
                        defaultFundOverride        : financeAccountIndex?.defaultFundOverride,
                        defaultOrganizationOverride: financeAccountIndex?.defaultOrganizationOverride,
                        defaultAccountOverride     : financeAccountIndex?.defaultAccountOverride,
                        defaultProgramOverride     : financeAccountIndex?.defaultProgramOverride,
                        defaultActivityOverride    : financeAccountIndex?.defaultActivityOverride,
                        defaultLocationOverride    : financeAccountIndex?.defaultLocationOverride,
                        defaultFundCode            : financeAccountIndex?.defaultFundCode,
                        defaultFundTitle           : financeAccountIndex?.defaultFundTitle,
                        defaultOrganizationCode    : financeAccountIndex?.defaultOrganizationCode,
                        defaultOrganizationTitle   : financeAccountIndex?.defaultOrganizationTitle,
                        defaultAccountCode         : financeAccountIndex?.defaultAccountCode,
                        defaultAccountTitle        : financeAccountIndex?.defaultAccountTitle,
                        defaultProgramCode         : financeAccountIndex?.defaultProgramCode,
                        defaultProgramTitle        : financeAccountIndex?.defaultProgramTitle,
                        defaultActivityCode        : financeAccountIndex?.defaultActivityCode,
                        defaultActivityTitle       : financeAccountIndex?.defaultActivityTitle,
                        defaultLocationCode        : financeAccountIndex?.defaultLocationCode,
                        defaultLocationTitle       : financeAccountIndex?.defaultLocationTitle
                ],
                fund          : [
                        code                            : requisitionAccounting.fund,
                        title                           : requisitionAccounting.fund ? financeFund?.fundTitle : null,
                        fundCode                        : financeFund?.fundCode,
                        fundTitle                       : financeFund?.fundTitle,
                        defaultOrgnCode                 : financeFund?.defaultOrgnCode,
                        defaultOrgnTitle                : financeFund?.defaultOrgnTitle,
                        defaultProgCode                 : financeFund?.defaultProgCode,
                        defaultProgTitle                : financeFund?.defaultProgTitle,
                        defaultActiveCode               : financeFund?.defaultActiveCode,
                        defaultActiveTitle              : financeFund?.defaultActiveTitle,
                        defaultLocationCode             : financeFund?.defaultLocationCode,
                        defaultLocationTitle            : financeFund?.defaultLocationTitle,
                        fundTypeDefaultOverrideIndicator: financeFund?.fundTypeDefaultOverrideIndicator
                ],
                organization  : [
                        code                : requisitionAccounting.organization,
                        title               : requisitionAccounting.organization ? financeOrganization?.orgnTitle : null,
                        orgnCode            : financeOrganization?.orgnCode,
                        orgnTitle           : financeOrganization?.orgnTitle,
                        defaultFundCode     : financeOrganization?.defaultFundCode,
                        defaultFundTitle    : financeOrganization?.defaultFundTitle,
                        defaultProgramCode  : financeOrganization?.defaultProgramCode,
                        defaultProgramTitle : financeOrganization?.defaultProgramTitle,
                        defaultActivityCode : financeOrganization?.defaultActivityCode,
                        defaultActivityTitle: financeOrganization?.defaultActivityTitle,
                        defaultLocationCode : financeOrganization?.defaultLocationCode,
                        defaultLocationTitle: financeOrganization?.defaultLocationTitle
                ],
                account       : [code: requisitionAccounting.account, title: requisitionAccounting.account ? financeAccountCompositeService.getListByAccountOrChartOfAccAndEffectiveDate(
                        [searchParam: requisitionAccounting.account, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )?.title : null],
                program       : [code: requisitionAccounting.program, title: requisitionAccounting.program ? programService.findByCoaProgramAndEffectiveDate( [coa: requisitionAccounting.chartOfAccount, programCodeDesc: requisitionAccounting.program], dummyPaginationParam )?.get( 0 )?.title : null],
                activity      : [code: requisitionAccounting.activity, title: requisitionAccounting.activity ? activityService.getListByActivityCodeTitleAndEffectiveDate( [activityCodeTitle: requisitionAccounting.activity, coaCode: requisitionAccounting.chartOfAccount], null, dummyPaginationParam )?.get( 0 )?.title : null],
                location      : [code: requisitionAccounting.location, title: requisitionAccounting.location ? locationService.getLocationByCodeTitleAndEffectiveDate( [codeTitle: requisitionAccounting.location, coaCode: requisitionAccounting.chartOfAccount], null, dummyPaginationParam )?.get( 0 )?.title : null],
                project       : [code: requisitionAccounting.project, title: requisitionAccounting.project ? financeProjectCompositeService.getListByProjectAndEffectiveDate( [projectCodeDesc: requisitionAccounting.project, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )?.longDescription : null]]]
    }

    /**
     * Find the requisition Accounting information
     * @param requisitionCode
     * @param item
     * @param sequenceNumber
     * @return
     */
    private def findCompleteAccountingByRequestCodeItemAndSeq( requisitionCode, Integer item, Integer sequenceNumber ) {
        LoggerUtility.debug( LOGGER, 'Input parameter for findCompleteAccountingByRequestCodeItemAndSeq :' + requisitionCode )
        def requisitionAccounting = requisitionAccountingService.findBasicAccountingByRequestCodeItemAndSeq( requisitionCode, item, sequenceNumber )
        if (requisitionAccounting.isEmpty()) {
            LoggerUtility.error( LOGGER, 'Requisition Accounting Information are empty for requestCode='
                    + requisitionCode + ', Item: ' + item + ' and Sequence: ' + sequenceNumber )
            throw new ApplicationException(
                    RequisitionAccountingService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING, [] ) )
        }
        def allAccounting = requisitionAccountingService.findAccountingByRequestCode( requisitionCode )
        allAccounting = allAccounting.findAll() {
            it.item == item
        }
        def allAccountingAmount = 0
        def totalPercentage = 0
        allAccounting.each() {
            allAccountingAmount += it.requisitionAmount + it.additionalChargeAmount + it.taxAmount - it.discountAmount
            totalPercentage += it.percentage
        }

        def reqDetail = requisitionDetailService.findByRequestCode( requisitionCode )
        def commodityTotalExtendedAmount = 0
        def commodityTotalCommodityTaxAmount = 0
        def commodityTotalAdditionalChargeAmount = 0
        def commodityTotalDiscountAmount = 0

        def processAmount = {it ->
            commodityTotalExtendedAmount += (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            commodityTotalCommodityTaxAmount += it.taxAmount ? it.taxAmount : 0
            commodityTotalAdditionalChargeAmount += it.additionalChargeAmount ? it.additionalChargeAmount : 0
            commodityTotalDiscountAmount += it.discountAmount ? it.discountAmount : 0
        }
        if (item == 0) {
            reqDetail.each {
                processAmount( it )
            }
        } else {
            reqDetail.findAll() {it.item == item}.each {
                processAmount( it )
            }
        }
        return requisitionAccounting.collect() {
            [id                                  : it.id,
             version                             : it.version,
             requestCode                         : it.requestCode,
             item                                : it.item,
             sequenceNumber                      : sequenceNumber,
             chartOfAccount                      : it.chartOfAccount,
             accountIndex                        : it.accountIndex,
             fund                                : it.fund,
             organization                        : it.organization,
             account                             : it.account,
             program                             : it.program,
             activity                            : it.activity,
             location                            : it.location,
             project                             : it.project,
             percentage                          : it.percentage,
             requisitionAmount                   : it.requisitionAmount,
             additionalChargeAmount              : it.additionalChargeAmount,
             discountAmount                      : it.discountAmount,
             taxAmount                           : it.taxAmount,
             userId                              : it.userId,
             accountTotal                        : it.requisitionAmount + it.additionalChargeAmount + it.taxAmount - it.discountAmount,
             renamingAmount                      : (commodityTotalExtendedAmount + commodityTotalCommodityTaxAmount + commodityTotalAdditionalChargeAmount - commodityTotalDiscountAmount) - allAccountingAmount,
             remaingingPercentage                : FinanceProcurementConstants.HUNDRED - totalPercentage,
             commodityTotalExtendedAmount        : commodityTotalExtendedAmount,
             commodityTotalCommodityTaxAmount    : commodityTotalCommodityTaxAmount,
             commodityTotalAdditionalChargeAmount: commodityTotalAdditionalChargeAmount,
             commodityTotalDiscountAmount        : commodityTotalDiscountAmount,
            ]
        }.getAt( 0 )
    }

    /**
     * Set NSF Overrider to requisition accounting from user profile
     *
     * @param requisitionAccounting
     * @param oracleUserName
     * @return
     */
    private def setNSFOverride( requisitionAccounting, oracleUserName ) {
        requisitionAccounting.insufficientFundsOverrideIndicator = financeUserProfileService.getUserProfileByUserId( oracleUserName )
                .nsfOverrider
    }

    /**
     *
     * @param requisitionAccountingRequest
     * @return
     */
    private def adjustAccountPercentageAndAmount( RequisitionAccounting requisitionAccountingRequest ) {
        def requisitionDetail
        if (requisitionAccountingRequest.item == FinanceProcurementConstants.ZERO) {//DLA
            requisitionDetail = requisitionDetailService.findByRequestCode( requisitionAccountingRequest.requestCode )
        } else {
            requisitionDetail = requisitionDetailService.findByRequestCodeAndItem( requisitionAccountingRequest.requestCode, requisitionAccountingRequest.item )
        }
        def totalExtendedCommodity = 0.0, totalTax = 0.0, totalDiscount = 0.0, totalAdditionalCharge = 0.0
        requisitionDetail.each {
            totalExtendedCommodity += (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            BigDecimal taxAmount = it.taxAmount ? it.taxAmount : new BigDecimal('0')
            totalTax += taxAmount.setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            totalDiscount += it.discountAmount.setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            totalAdditionalCharge += it.additionalChargeAmount.setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
        }


        def orgPercentage = requisitionAccountingRequest.percentage

        boolean isAdjustmentNeededForTax = totalTax != ((totalTax * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalTax * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))
        boolean isAdjustmentNeededForDiscount = totalDiscount != ((totalDiscount * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalDiscount * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))

        boolean isAdjustmentNeededForAdditionalCharge = totalAdditionalCharge != ((totalAdditionalCharge * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalAdditionalCharge * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))

        boolean isAdjustmentNeededForExtendedAmount = totalExtendedCommodity != ((totalExtendedCommodity * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalExtendedCommodity * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))


        def adjustedPercentage = orgPercentage;

        // Check If at least one of them is true, and also find out what the adjusted % value is going to be.
        if (isAdjustmentNeededForExtendedAmount) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalExtendedCommodity * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )) / totalExtendedCommodity
        } else if (isAdjustmentNeededForTax) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalTax * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )) / totalTax
        } else if (isAdjustmentNeededForDiscount) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalDiscount * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )) / totalDiscount
        } else if (isAdjustmentNeededForAdditionalCharge) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalAdditionalCharge * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )) / totalAdditionalCharge
        }
        adjustedPercentage = adjustedPercentage.setScale( FinanceProcurementConstants.DECIMAL_PRECISION_UNIT_PRICE, BigDecimal.ROUND_HALF_UP )
        requisitionAccountingRequest.percentage = adjustedPercentage
        requisitionAccountingRequest.discountAmountPercent = adjustedPercentage
        requisitionAccountingRequest.additionalChargeAmountPct = adjustedPercentage
        requisitionAccountingRequest.taxAmountPercent = adjustedPercentage
    }

}

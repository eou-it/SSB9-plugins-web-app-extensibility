/*******************************************************************************
 Copyright 2015-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation

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
    def requisitionDetailsAcctCommonCompositeService
    def requisitionInformationService

    /**
     * Creates Requisition Accounting level.
     *
     * @param map Map which have all the required date to create requisition accounting.
     * @return map Map having requestCode, item and sequenceNumber of created requisition accounting.
     */
    def createPurchaseRequisitionAccounting( map ) {
        RequisitionAccounting requisitionAccountingRequest = map.requisitionAccounting
        def user = springSecurityService.getAuthentication().user
        String oracleUsername = map.oracleUsername
        if (!oracleUsername) {
            oracleUsername = user.oracleUserName
        }
        if (oracleUsername) {
            requisitionAccountingRequest.userId = oracleUsername
            def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionAccountingRequest.requestCode )
            FinanceProcurementHelper.checkCompleteRequisition( header )
            reValidateAccountingFOAP( requisitionAccountingRequest, header.transactionDate )
            if (header.isDocumentLevelAccounting) {
                requisitionAccountingRequest.item = 0
            }
            requisitionAccountingRequest.sequenceNumber = requisitionAccountingService.getLastSequenceNumberByRequestCode( requisitionAccountingRequest.requestCode, requisitionAccountingRequest.item ).next()
            setNSFOverride( requisitionAccountingRequest )
            requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( requisitionAccountingRequest )
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
        def user = springSecurityService.getAuthentication().user
        String oracleUsername = accountingDomainModel.oracleUsername
        if (!oracleUsername) {
            oracleUsername = user.oracleUserName
        }
        if (!oracleUsername) {
            LoggerUtility.error( LOGGER, 'User' + user + ' is not valid' )
            throw new ApplicationException( RequisitionAccountingCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( accountingDomainModel.requisitionAccounting.requestCode )
        FinanceProcurementHelper.checkCompleteRequisition( header )

        if (accountingDomainModel.requisitionAccounting.item == null || accountingDomainModel.requisitionAccounting.sequenceNumber == null) {
            LoggerUtility.error( LOGGER, 'Item and Sequence number are required to update the Requisition Accounting information.' )
            throw new ApplicationException( RequisitionAccountingCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED, [] ) )
        }
        reValidateAccountingFOAP( accountingDomainModel.requisitionAccounting, header.transactionDate )
        def existingAccountingInfo = requisitionAccountingService.findByRequestCodeItemAndSeq( accountingDomainModel.requisitionAccounting.requestCode,
                                                                                               accountingDomainModel.requisitionAccounting.item, accountingDomainModel.requisitionAccounting.sequenceNumber )
        RequisitionAccounting requisitionAccountingRequest = accountingDomainModel.requisitionAccounting
        requisitionAccountingRequest.id = existingAccountingInfo.id
        requisitionAccountingRequest.version = existingAccountingInfo.version
        requisitionAccountingRequest.requestCode = existingAccountingInfo.requestCode
        requisitionAccountingRequest.lastModified = new Date()
        requisitionAccountingRequest.item = existingAccountingInfo.item
        requisitionAccountingRequest.sequenceNumber = existingAccountingInfo.sequenceNumber
        requisitionAccountingRequest.userId = oracleUsername
        setNSFOverride( requisitionAccountingRequest )
        requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( requisitionAccountingRequest )
        def requisitionAccounting = requisitionAccountingService.update( [domainModel: requisitionAccountingRequest] )
        LoggerUtility.debug( LOGGER, "Requisition Accounting information updated " + requisitionAccounting )
        return requisitionAccounting
    }

    /**
     * This method is used to find RequisitionAccounting and related details by requisition code, item number and sequence number.
     * @param requisitionCode Requisition code.
     * @param item Item number.
     * @param sequenceNumber Sequence number.
     * @return RequisitionAccounting information.
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def findByRequestCodeItemAndSeq( requisitionCode, Integer item, Integer sequenceNumber ) {
        LoggerUtility.debug( LOGGER, String.format( 'Input parameter for findByRequestCodeItemAndSeq :%1s , %2d ,%3d', requisitionCode, item, sequenceNumber ) )
        def dummyPaginationParam = [max: 1, offset: 0]
        def headerTnxDate = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode ).transactionDate
        def requisitionAccounting = findCompleteAccountingByRequestCodeItemAndSeq( requisitionCode, item, sequenceNumber )
        def financeAccountIndex
        try {
            financeAccountIndex = requisitionAccounting.accountIndex ? financeAccountIndexService.findIndexByIndexCodeAndEffectiveDate( [coaCode: requisitionAccounting.chartOfAccount, effectiveDate: headerTnxDate, indexCodeTitle: requisitionAccounting.accountIndex], dummyPaginationParam )?.get( 0 ) : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }

        def financeFund
        try {
            financeFund = financeFundCompositeService.findFundByCoaFundCodeAndEffectiveDate( [effectiveDate: headerTnxDate, codeTitle: requisitionAccounting.fund, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }

        def financeOrganization
        try {
            financeOrganization = financeOrganizationCompositeService.findOrganizationByEffectiveDateAndCode( [searchParam  : requisitionAccounting.organization,
                                                                                                                          effectiveDate: headerTnxDate,
                                                                                                                          coaCode      : requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )

        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }

        def accountingTitle
        try {
            accountingTitle = requisitionAccounting.account ? financeAccountCompositeService.getAccountByAccountOrChartOfAccAndEffectiveDate(
                    [searchParam: requisitionAccounting.account, effectiveDate: headerTnxDate, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )?.title : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }

        def programTitle
        try {
            programTitle = requisitionAccounting.program ? programService.fetchProgramByCoaProgramAndEffectiveDate( [coa: requisitionAccounting.chartOfAccount, effectiveDate: headerTnxDate, programCodeDesc: requisitionAccounting.program], dummyPaginationParam )?.get( 0 )?.title : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }
        def activityTitle
        try {
            activityTitle = requisitionAccounting.activity ? activityService.getListByActivityCodeTitleAndEffectiveDate( [activityCodeTitle: requisitionAccounting.activity, coaCode: requisitionAccounting.chartOfAccount], headerTnxDate, dummyPaginationParam )?.get( 0 )?.title : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }
        def locationTitle
        try {
            locationTitle = requisitionAccounting.location ? locationService.getLocationByCodeTitleAndEffectiveDate( [codeTitle: requisitionAccounting.location, coaCode: requisitionAccounting.chartOfAccount], headerTnxDate, dummyPaginationParam )?.get( 0 )?.title : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }
        def projectTitle
        try {
            projectTitle = requisitionAccounting.project ? financeProjectCompositeService.getListByProjectAndEffectiveDate( [projectCodeDesc: requisitionAccounting.project, coaCode: requisitionAccounting.chartOfAccount, effectiveDate: headerTnxDate], dummyPaginationParam )?.get( 0 )?.longDescription : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }
        def chartOfAccountTitle
        try {
            chartOfAccountTitle = requisitionAccounting.chartOfAccount ? chartOfAccountsService.getChartOfAccountByCode( requisitionAccounting.chartOfAccount, headerTnxDate )?.title : null
        } catch (ApplicationException e) {
            LoggerUtility.warn( LOGGER, e.getMessage() )
        }

        [status    : requisitionInformationService.fetchRequisitionsByReqNumber( requisitionCode )?.status,
         accounting: requisitionAccounting, cifoapalp: [
                chartOfAccount: [code: requisitionAccounting.chartOfAccount, title: chartOfAccountTitle],
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
                account       : [code: requisitionAccounting.account, title: accountingTitle],
                program       : [code: requisitionAccounting.program, title: programTitle],
                activity      : [code: requisitionAccounting.activity, title: activityTitle],
                location      : [code: requisitionAccounting.location, title: locationTitle],
                project       : [code: requisitionAccounting.project, title: projectTitle]]]
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
        if (!requisitionAccounting) {
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
            allAccountingAmount += it.requisitionAmount + FinanceCommonUtility.nullToZero( it.additionalChargeAmount ) + FinanceCommonUtility.nullToZero( it.taxAmount ) - FinanceCommonUtility.nullToZero( it.discountAmount )
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
            [
                    renamingAmount                      : (commodityTotalExtendedAmount + commodityTotalCommodityTaxAmount + commodityTotalAdditionalChargeAmount - commodityTotalDiscountAmount) - allAccountingAmount,
                    remaingingPercentage                : FinanceProcurementConstants.HUNDRED - totalPercentage,
                    commodityTotalExtendedAmount        : commodityTotalExtendedAmount,
                    commodityTotalCommodityTaxAmount    : commodityTotalCommodityTaxAmount,
                    commodityTotalAdditionalChargeAmount: commodityTotalAdditionalChargeAmount,
                    commodityTotalDiscountAmount        : commodityTotalDiscountAmount,
                    percentageDisplay                   : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.percentage, FinanceValidationConstants.TWO ),
                    requisitionAmountDisplay            : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.requisitionAmount, FinanceValidationConstants.TWO ),
                    additionalChargeAmountDisplay       : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.additionalChargeAmount, FinanceValidationConstants.TWO ),
                    discountAmountDisplay               : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.discountAmount, FinanceValidationConstants.TWO ),
                    taxAmountDisplay                    : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.taxAmount, FinanceValidationConstants.TWO ),
                    accountTotalDisplay                 : FinanceProcurementHelper.getLocaleBasedFormattedNumber( it.requisitionAmount + it.additionalChargeAmount + it.taxAmount - it.discountAmount, FinanceValidationConstants.TWO ),
                    renamingAmountDisplay               : FinanceProcurementHelper.getLocaleBasedFormattedNumber( (commodityTotalExtendedAmount + commodityTotalCommodityTaxAmount + commodityTotalAdditionalChargeAmount - commodityTotalDiscountAmount) - allAccountingAmount, FinanceValidationConstants.TWO )
            ] << requisitionAccountingService.getBaseAccountingInformation( it, sequenceNumber )
        }.getAt( 0 )
    }

    /**
     * Set NSF Overrider to requisition accounting from user profile
     *
     * @param requisitionAccounting
     * @return
     */
    private def setNSFOverride( requisitionAccounting ) {
        requisitionAccounting.insufficientFundsOverrideIndicator = FinanceProcurementConstants.FALSE
        // Populate always false from XE as Requested by BA Mark/Kumar
    }

    /**
     * Re-validates FOAP
     * @param requisitionAccount
     * @param tnxDate
     * @return
     */
    private def reValidateAccountingFOAP( requisitionAccount, tnxDate ) {
        financeOrganizationCompositeService.findOrganizationListByEffectiveDateAndSearchParam( [searchParam  : requisitionAccount.organization,
                                                                                                effectiveDate: tnxDate, coaCode: requisitionAccount.chartOfAccount],
                                                                                               [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] )

        financeFundCompositeService.findFundListByEffectiveDateAndFundCode( [codeTitle    : requisitionAccount.fund,
                                                                             effectiveDate: tnxDate, coaCode: requisitionAccount.chartOfAccount],
                                                                            [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] )


        financeAccountCompositeService.getListByAccountOrChartOfAccAndEffectiveDate( [searchParam  : requisitionAccount.account,
                                                                                      effectiveDate: tnxDate, coaCode: requisitionAccount.chartOfAccount],
                                                                                     [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] )

        programService.findByCoaProgramAndEffectiveDate( [coa: requisitionAccount.chartOfAccount, effectiveDate: tnxDate, programCodeDesc: requisitionAccount.program],
                                                         [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] )

    }
}

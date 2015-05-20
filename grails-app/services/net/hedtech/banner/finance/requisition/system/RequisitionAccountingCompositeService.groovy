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
    def chartOfAccountsService
    def accountIndexService
    def financeFundService
    def financeOrganizationCompositeService
    def financeAccountCompositeService
    def programService
    def activityService
    def locationService
    def financeProjectCompositeService

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
            // TODO need to restrict additional accounting creation
            /*if (100 <= requisitionAccountingService.getSplittingPercentage( requisitionAccountingRequest.requestCode, requisitionAccountingRequest.item )) {
                throw new ApplicationException(
                        RequisitionAccountingCompositeService,
                        new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_NO_MORE_ACCOUNTING, [] ) )
            }*/
            requisitionAccountingRequest.sequenceNumber = requisitionAccountingService.getLastSequenceNumberByRequestCode( requisitionAccountingRequest.requestCode, requisitionAccountingRequest.item ).next()
            RequisitionAccounting requisitionAccounting = requisitionAccountingService.create( [domainModel: requisitionAccountingRequest] )
            LoggerUtility.debug LOGGER, 'Requisition Accounting created ' + requisitionAccounting
            return [requestCode: requisitionAccounting.requestCode,
                    item       : requisitionAccounting.item, sequenceNumber: requisitionAccounting.sequenceNumber]
        } else {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
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
        def requestCode = accountingDomainModel.requisitionAccounting.requestCode
        FinanceProcurementHelper.checkCompleteRequisition( requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode ) )

        if (accountingDomainModel.requisitionAccounting.item == null || accountingDomainModel.requisitionAccounting.sequenceNumber == null) {
            LoggerUtility.error LOGGER, 'Item and Sequence number are required to update the Requisition Accounting information.'
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

        RequisitionAccounting existingAccountingInfo = requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequenceNumber )
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
            def requisitionAccounting = requisitionAccountingService.update( [domainModel: requisitionAccountingRequest] )
            LoggerUtility.debug LOGGER, "Requisition Accounting information updated " + requisitionAccounting
            return requisitionAccounting
        } else {
            LoggerUtility.error LOGGER, 'User' + user + ' is not valid'
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
        def requisitionAccounting = requisitionAccountingService.findByRequestCodeItemAndSeq( requisitionCode, item, sequenceNumber )
        [accounting: requisitionAccounting, cifoapalp: [
                chartOfAccount: [code: requisitionAccounting.chartOfAccount, title: requisitionAccounting.chartOfAccount ? chartOfAccountsService.getChartOfAccountByCode( requisitionAccounting.chartOfAccount )?.title : null],
                index         : [code: requisitionAccounting.accountIndex, title: requisitionAccounting.accountIndex ? accountIndexService.getListByIndexTitleAndEffectiveDate( [coaCode: requisitionAccounting.chartOfAccount, indexCodeTitle: requisitionAccounting.accountIndex], dummyPaginationParam )?.get( 0 )?.title : null],
                fund          : [code: requisitionAccounting.fund, title: requisitionAccounting.fund ? financeFundService.findFundByEffectiveDateAndFundCode( null, requisitionAccounting.fund, requisitionAccounting.chartOfAccount, dummyPaginationParam )?.get( 0 )?.fundTitle : null],
                organization  : [code: requisitionAccounting.organization, title: requisitionAccounting.organization ? financeOrganizationCompositeService.
                        findOrganizationListByEffectiveDateAndSearchParam( [searchParam: requisitionAccounting.organization, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )?.orgnTitle : null],
                account       : [code: requisitionAccounting.account, title: requisitionAccounting.account ? financeAccountCompositeService.getListByAccountOrChartOfAccAndEffectiveDate(
                        [searchParam: requisitionAccounting.account, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )?.title : null],
                program       : [code: requisitionAccounting.program, title: requisitionAccounting.program ? programService.findByCoaProgramAndEffectiveDate( [coa: requisitionAccounting.chartOfAccount, programCodeDesc: requisitionAccounting.program], dummyPaginationParam )?.get( 0 )?.title : null],
                activity      : [code: requisitionAccounting.activity, title: requisitionAccounting.activity ? activityService.getListByActivityCodeTitleAndEffectiveDate( [activityCodeTitle: requisitionAccounting.activity, coaCode: requisitionAccounting.chartOfAccount], null, dummyPaginationParam )?.get( 0 )?.title : null],
                location      : [code: requisitionAccounting.location, title: requisitionAccounting.location ? locationService.getLocationByCodeTitleAndEffectiveDate( [codeTitle: requisitionAccounting.location, coaCode: requisitionAccounting.chartOfAccount], null, dummyPaginationParam )?.get( 0 )?.title : null],
                project       : [code: requisitionAccounting.project, title: requisitionAccounting.project ? financeProjectCompositeService.getListByProjectAndEffectiveDate( [projectCodeDesc: requisitionAccounting.project, coaCode: requisitionAccounting.chartOfAccount], dummyPaginationParam )?.get( 0 )?.longDescription : null]]]
    }
}

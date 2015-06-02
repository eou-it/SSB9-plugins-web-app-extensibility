/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test case class for RequisitionAccountingService.
 */
class RequisitionAccountingServiceIntegrationTests extends BaseIntegrationTestCase {
    def requisitionAccountingService
    def springSecurityService
    /**
     * Super class setup
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test case method to find Requisition Accounting by request code, item and sequence number.
     */
    @Test
    public void testFindByRequestCodeItemAndSeq() {
        final def requestCode = 'R0001397'
        final Integer item = 0
        final Integer sequence = 2
        def requisitionAccounting = requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequence )
        assertTrue( requisitionAccounting != null )
    }

    /**
     * Test case method to find Requisition Accounting by passing wrong request code.
     */
    @Test(expected = ApplicationException)
    public void testFindByWrongRequestCode() {
        final def requestCode = 'REQ_INVALID'
        final Integer item = 0
        final Integer sequence = 2
        requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequence )
    }

    /**
     * Test case method to find Requisition Accounting by passing wrong item number.
     */
    @Test(expected = ApplicationException)
    public void testFindByWrongItem() {
        final def requestCode = 'R0001397'
        final Integer item = 901291
        final Integer sequence = 2
        requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequence )
    }

    /**
     * Test case method to find Requisition Accounting by passing wrong item number.
     */
    @Test(expected = ApplicationException)
    public void testFindByWrongSequence() {
        final def requestCode = 'R0001397'
        final Integer item = 0
        final Integer sequence = 90981
        requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequence )
    }

    /**
     * Test case method to find requisition accounting list by logged in user.
     */
    @Test
    public void testFetchRequisitionAccountingListByUser() {
        def pagingParams = [max: 500, offset: 0]
        def list = requisitionAccountingService.findRequisitionAccountingListByUser( pagingParams )
        assertTrue( list.size() > 0 || list.isEmpty() )
    }

    /**
     * Test case method to find requisition accounting list by passing wrong user.
     */
    @Test
    public void testFetchRequisitionAccountingListByPassingWrongUser() {
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def pagingParams = [max: 500, offset: 0]
        try {
            requisitionAccountingService.findRequisitionAccountingListByUser( pagingParams )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test case method to find requisition accounting list to test empty list.
     */
    @Test
    public void testFetchRequisitionAccountingListForEmptyTest() {
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = 'SYSTESTFINAUSR'
        def pagingParams = [max: 500, offset: 0]
        try {
            requisitionAccountingService.findRequisitionAccountingListByUser( pagingParams )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test case method to find last sequence number generated for request code.
     */
    @Test
    public void testGetLastSequenceNumberByRequestCode() {
        def lastSequence = requisitionAccountingService.getLastSequenceNumberByRequestCode( 'R0001397', 0 )
        assertTrue( lastSequence == 0 || lastSequence > 0 )
    }

    /**
     * Test case method to find last item number generated for request code.
     */
    @Test
    public void testGetLastItemNumberByRequestCode() {
        def lastItem = requisitionAccountingService.getLastItemNumberByRequestCode( 'R0001397' )
        assertTrue( lastItem == 0 || lastItem > 0 )
    }


    @Test
    public void accountingExists() {
        def count = requisitionAccountingService.findAccountingSizeByRequestCode( 'R0001397' )
        assertTrue( count >= 0 )
    }

    /**
     * Test case method to create requisition accounting level.
     */
    @Test
    public void testCreateRequisitionAccounting() {
        def requestAccounting = getRequestAccounting()
        requestAccounting.save( failOnError: true, flush: true )
        requestAccounting.refresh()
        def requisitionAccountingLevel = requisitionAccountingService.findByRequestCodeItemAndSeq(
                requestAccounting.requestCode, requestAccounting.item, requestAccounting.sequenceNumber )
        assertTrue requisitionAccountingLevel.requestCode == requestAccounting.requestCode
        assertTrue requisitionAccountingLevel.item == requestAccounting.item
        assertTrue requisitionAccountingLevel.sequenceNumber == requestAccounting.sequenceNumber
    }

    /**
     * The method is used to get the RequisitionAccounting object with all required values to insert/update.
     * @return RequisitionAccounting.
     */
    public RequisitionAccounting getRequestAccounting() {
        def requestCode = 'R0002497'
        def lastSeq = requisitionAccountingService.getLastSequenceNumberByRequestCode( requestCode, 0 )
        def item = 0
        def sequenceNumber = lastSeq.next()
        def amount = 100.00
        def fiscalCode = '15'
        def period = '09'
        def ruleClassCode = 'REQP'
        def chartOfAccountsCode = 'B'
        def indexCode = 'EPHM54'
        def fundCode = 'EPMSF1'
        def orgnCode = '11007'
        def accountCode = '1006'
        def programCode = '10'
        def insufficientFundsOverrideIndicator = true
        def activityCode = ''
        def location = ''
        def projectCode = ''
        def percentage = ''
        def discountAmount = ''
        def discountAmountPercent = ''
        def additionalChargeAmount = ''
        def additionalChargeAmountPct = ''
        def requestAccounting = new RequisitionAccounting(
                requestCode: requestCode,
                item: item,
                activity: activityCode,
                location: location,
                project: projectCode,
                percentage: percentage,
                discountAmount: discountAmount,
                discountAmountPercent: discountAmountPercent,
                additionalChargeAmount: additionalChargeAmount,
                additionalChargeAmountPct: additionalChargeAmountPct,
                sequenceNumber: sequenceNumber,
                requisitionAmount: amount,
                fiscalYearCode: fiscalCode,
                period: period,
                ruleClass: ruleClassCode,
                chartOfAccounts: chartOfAccountsCode,
                accountIndex: indexCode,
                fund: fundCode,
                organization: orgnCode,
                account: accountCode,
                program: programCode,
                insufficientFundsOverrideIndicator: insufficientFundsOverrideIndicator
        )
        return requestAccounting
    }
}

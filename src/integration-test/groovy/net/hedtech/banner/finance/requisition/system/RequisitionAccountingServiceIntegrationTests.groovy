/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import static groovy.test.GroovyAssert.*

/**
 * Test case class for RequisitionAccountingService.
 */

@Integration
@Rollback
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
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
        logout()
    }

    /**
     * Test case method to find Requisition Accounting by request code, item and sequence number.
     */
    @Test
    public void testFindByRequestCodeItemAndSeq() {
        final def requestCode = 'RSED0003'
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
        final def requestCode = 'RSED0003'
        final Integer item = 901291
        final Integer sequence = 2
        requisitionAccountingService.findByRequestCodeItemAndSeq( requestCode, item, sequence )
    }

    /**
     * Test case method to find Requisition Accounting by passing wrong item number.
     */
    @Test(expected = ApplicationException)
    public void testFindByWrongSequence() {
        final def requestCode = 'RSED0003'
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
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def list = requisitionAccountingService.findRequisitionAccountingListByUser( pagingParams, 'GRAILS' )
        assertTrue( list.size() > 0 )
    }

    /**
     * Test case method to find requisition accounting list by passing wrong user.
     */
    @Test
    public void testFetchRequisitionAccountingListByPassingWrongUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
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
        super.login 'FORSED23',
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        try {
            requisitionAccountingService.findRequisitionAccountingListByUser( pagingParams )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING )
        }
    }

    /**
     * Test case method to find requisition accounting list to test empty list.
     */
    @Test
    public void testFetchRequisitionAccountingListWithProvider() {
        super.login 'FORSED23',
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def providedUser = 'testProvider'
        try {
            requisitionAccountingService.findRequisitionAccountingListByUser( pagingParams, providedUser )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING )
        }
    }

    /**
     * Test case method to find requisition accounting list to test empty list.
     */
    @Test
    public void testFetchRequisitionAccountingListWithWrongProvider() {

        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = 'TESTUSER'
        println ">>>>>>>>>springSecurityService.getAuthentication().user is ::"+springSecurityService.getAuthentication().user

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
        def lastSequence = requisitionAccountingService.getLastSequenceNumberByRequestCode( 'RSED0003', 0 )
        assertTrue( lastSequence > 0 )
    }

    /**
     * Test case method to find last sequence number generated for request code.
     */
    @Test
    public void testGetLastSequenceNumberByEmptyRequestCode() {
        def lastSequence = requisitionAccountingService.getLastSequenceNumberByRequestCode( null, 0 )
        assertTrue( lastSequence == 0 )
    }

    /**
     * Test case method to find last item number generated for request code.
     */
    @Test
    public void testGetLastItemNumberByRequestCode() {
        def lastItem = requisitionAccountingService.getLastItemNumberByRequestCode( 'RSED0005' )
        assertTrue( lastItem > 0 )
    }

    /**
     * Test case method to find last item number generated for request code.
     */
    @Test
    public void testGetLastItemNumberByEmptyRequestCode() {
        def lastItem = requisitionAccountingService.getLastItemNumberByRequestCode( null )
        assertTrue( lastItem == 0 )
    }


    @Test
    public void accountingExists() {
        def count = requisitionAccountingService.findAccountingSizeByRequestCode( 'RSED0003' )
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
        def requestCode = 'RSED0003'
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
        def accountCode = '2320'
        def programCode = '10'
        def insufficientFundsOverrideIndicator = true
        def activityCode = ''
        def location = ''
        def projectCode = ''
        def percentage = null
        def discountAmount = null
        def discountAmountPercent = null
        def additionalChargeAmount = null
        def additionalChargeAmountPct = null
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
                fiscalYear: fiscalCode,
                period: period,
                ruleClass: ruleClassCode,
                chartOfAccount: chartOfAccountsCode,
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

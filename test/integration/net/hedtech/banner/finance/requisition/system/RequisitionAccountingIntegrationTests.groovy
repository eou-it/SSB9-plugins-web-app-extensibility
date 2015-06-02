package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequisitionAccountingIntegrationTests extends BaseIntegrationTestCase {

    //def i_success_requestCode = "R0000047"
    def requestCode = "R0002497" // "R0000053"
    def item = 0
    def sequenceNumber = 3
    def amount = 100.00
    def fiscalCode = "15"
    def period = "09"
    def ruleClassCode = "REQP"
    def chartOfAccountsCode = "B"
    def indexCode = "EPHM54"
    def fundCode = "EPMSF1"
    def orgnCode = "11007"
    def accountCode = "1006"
    def programCode = "10"
    def insufficientFundsOverrideIndicator = true
    def activityCode = ''
    def location = ''
    def projectCode = ''
    def percentage = ''
    def discountAmount = ''
    def discountAmountPercent = ''
    def additionalChargeAmount = ''
    def additionalChargeAmountPct = ''

    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test to create requisition accounting.
     */
    @Test
    void testCreate() {
        def requestAcc = RequisitionAccounting.fetchByRequestCodeItemAndSeq( requestCode,
                                                                             item,
                                                                             sequenceNumber )
        if (requestAcc?.requestCode) {
            def id = requestAcc.id
            requestAcc.delete()
            assertNull requestAcc.get( id )
        }
        def requestAccounting = getRequestAccounting()
        requestAccounting.save( failOnError: true, flush: true )
        assertNotNull requestAccounting.id
    }

    /**
     * Test case to fetch last sequence number by the request code in the RequisitionAccounting.
     */
    @Test
    void testFetchLastSequenceNumberByRequestCode() {
        Integer lastGeneratedSequence = RequisitionAccounting.fetchLastSequenceNumberByRequestCode( requestCode, 0 ).getAt( 0 )
        assertTrue( lastGeneratedSequence != null )
    }

    /**
     * Test case to fetch last item number by the request code in the RequisitionAccounting.
     */
    @Test
    void testFetchLastItemNumberByRequestCode() {
        Integer lastGeneratedItem = RequisitionAccounting.fetchLastItemNumberByRequestCode( requestCode ).getAt( 0 )
        assertTrue( lastGeneratedItem != null )
    }

    /**
     * Test case to fetch last item number by the request code in the RequisitionAccounting.
     */
    @Test
    void findAccountingByRequestCode() {
        def accountingList = RequisitionAccounting.findAccountingByRequestCode( 'R0001474' )
        assertTrue( accountingList.size() > 0 )
    }

    /**
     * Test Fetch Request Accounting by user.
     */
    @Test
    void testFetchRequisitionAccountingByUserId() {
        def pagingParams = [max: 500, offset: 0]
        def requestAccountingList = RequisitionAccounting.fetchByUserId(
                FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams ).list;
        assertTrue( requestAccountingList.size() > 0 || requestAccountingList.isEmpty() )
    }

    /**
     * The method is used to get the RequisitionAccounting object with all required values to insert/update.
     * @return RequisitionAccounting.
     */
    public RequisitionAccounting getRequestAccounting() {
        def lastSeq = RequisitionAccounting.fetchLastSequenceNumberByRequestCode( requestCode, 0 ).getAt( 0 )
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
                sequenceNumber: lastSeq.next(),
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

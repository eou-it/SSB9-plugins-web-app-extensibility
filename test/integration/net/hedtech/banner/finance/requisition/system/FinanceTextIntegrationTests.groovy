/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system


import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration Test case class for FinanceText.
 */
class FinanceTextIntegrationTests extends BaseIntegrationTestCase {
    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Test case method to test save FinanceText.
     */
    @Test
    public void testSaveFinanceText() {
        FinanceText financeText = getFinanceText()
        financeText.save(failOnError: true, flush: true)
        assertNotNull(financeText.id)
    }

    /**
     * Test case method to test listing finance text by text code.
     */
    @Test
    public void testListFinanceTextByCode() {
        def list = FinanceText.listAllFinanceTextByCode('R0000002')
        assert (list.size() > 0)
    }

    /**
     * Test case method to test get finance text by text code and sequence number.
     */
    @Test
    public void testGetFinanceTextByCodeAndSeqNumber() {
        def financeText = FinanceText.getFinanceTextByCodeAndItemNumber('R0000002', 10)[0]
        assertNotNull(financeText)
    }

    /**
     * Test case to test update finance text.
     */
    @Test
    public void testUpdateFinanceText() {
        FinanceText financeText = getFinanceText()
        financeText.save(failOnError: true, flush: true)
        def id = financeText.id
        financeText.textCode = 'UPDATE_TEST'
        if (financeText.isDirty()) {
            financeText.save(failOnError: true, flush: true)
            assertTrue(id == financeText.id)
        } else {
            assertNotNull(financeText.id)
        }
    }

    /**
     * Test Case to test delete FinanceText.
     */
    @Test
    public void testDeleteFinanceText() {
        FinanceText financeText = getFinanceText()
        financeText.save(failOnError: true, flush: true)
        def textCode = financeText.textCode
        def item = financeText.textItem
        financeText.delete(failOnError: true, flush: true)
        assertTrue(FinanceText.getFinanceTextByCodeAndItemNumber(textCode, item).size() == 0)
    }

    /**
     * Private method to get FinanceText for Save, Update and delete test cases.
     * @return
     */
    private FinanceText getFinanceText() {
        return new FinanceText(
                activityDate: new Date(),
                changeSequenceNumber: 1,
                classNumber: 1,
                textCode: 'TEST',
                dataOrigin: 'INTEGRATION_TEST',
                documentTypeSequenceNumber: 1,
                textItem: 1,
                pidm: 1001,
                printOptionIndicator: 'Y',
                sequenceNumber: 1,
                text: 'Text from Domain Integration test',
                lastModifiedBy: 'TEST_USER',
                vpdiCode: 'TEST'
        )
    }
}

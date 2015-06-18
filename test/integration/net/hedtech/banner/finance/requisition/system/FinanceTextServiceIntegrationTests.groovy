/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system


import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case class for FinanceTextService.
 */
class FinanceTextServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeTextService

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
     * Test case method to test list finance text by text code
     */
    @Test
    public void testListFinanceTextByCodeByTextCode() {
//        assert (financeTextService.listAllFinanceTextByCode('R0000002').size() > 0)
    }

    /**
     * Test case method to test get FinanceText by text code and sequence number.
     */
    @Test
    public void testGetFinanceTextByCodeAndSeqNumber() {
        //assertNotNull(financeTextService.getFinanceTextByCodeAndItemNumber('R0000002', 10))
    }

    /**
     * Test case method to test save text.
     */
    @Test
    public void testSaveText() {
        //def financeText = getFinanceText();
        //assertTrue(financeTextService.saveText(financeText) == financeText.textCode)
    }

    /**
     * Test case method to test update text.
     */
    @Test
    public void testUpdateText() {
//        def financeText = getFinanceText();
//        def savedFinanceTextCode = financeTextService.saveText(financeText)
//        def financeTextToUpdate = getFinanceText()
//        financeTextToUpdate.text = 'New Text For Update'
//        financeTextService.updateText(financeTextToUpdate, financeTextToUpdate.textItem, financeTextToUpdate.printOptionIndicator)
//        assertTrue(financeTextService.listAllFinanceTextByCode(savedFinanceTextCode).size() == 1)
    }

    /**
     * Test case method to test delete text.
     */
    @Test
    public void testDeleteText() {
//        def financeText = getFinanceText();
//        def savedFinanceTextCode = financeTextService.saveText(financeText)
//        FinanceText financeTextToDelete = financeTextService.getFinanceTextByCodeAndItemAndPrintOption(savedFinanceTextCode, financeText.textItem, financeText.printOptionIndicator)[0]
//        financeTextService.deleteText(financeTextToDelete.textCode, null, financeTextToDelete.printOptionIndicator)
//        assert (financeTextService.listAllFinanceTextByCode(savedFinanceTextCode).size() == 0)
    }

    /**
     * Private method to get FinanceText for Save, Update and delete test cases.
     * @return FinanceText.
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
                text: 'Text from Domain Integration test Text from Domain Integration test',
                lastModifiedBy: 'TEST_USER',
                vpdiCode: 'TEST'
        )
    }
}

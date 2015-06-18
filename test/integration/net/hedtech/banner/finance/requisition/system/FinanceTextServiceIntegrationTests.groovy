/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
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
    public void testListAllFinanceTextByCode() {
        assert (financeTextService.listAllFinanceTextByCode('R0000002').size() > 0)
    }

    /**
     * Test case method to test get FinanceText by text code and sequence number.
     */
    @Test
    public void testGetFinanceTextByCodeAndItemNumber() {
        assertNotNull(financeTextService.getFinanceTextByCodeAndItemNumber('R0000002', 10))
    }

    /**
     * Test case method to test save text.
     */
    @Test
    public void testSaveText() {
        def financeText = getFinanceText();
        assertTrue(financeTextService.create([domainModel: financeText]).textCode == financeText.textCode)
    }

    /**
     * Test case method to test update text.
     */
    @Test
    public void testUpdateText() {
        FinanceText financeText = financeTextService.getFinanceTextByCodeAndItemNumber('R0000002', 1)[0];
        financeText.text = 'New Text For Update'
        def updated = financeTextService.update([domainModel: financeText])
        assertTrue(financeTextService.listAllFinanceTextByCode(updated.textCode).size() == 1)
    }

    /**
     * Test case method to test delete text.
     */
    @Test
    public void testDeleteText() {
        def financeText = getFinanceText();
        FinanceText financeTextToDelete = financeTextService.getFinanceTextByCodeAndItemNumber('R0000002', 1)[0];
        financeTextService.delete([domainModel: financeTextToDelete])
        assert (financeTextService.listAllFinanceTextByCode(financeText.textCode).size() == 0)
    }

    /**
     * Test case to test get finance text by code, item and print option indicator.
     */
    @Test
    public void testGetFinanceTextByCodeAndItemAndPrintOption() {
        FinanceText financeText = financeTextService.getFinanceTextByCodeAndItemAndPrintOption('R0000002', 1,
                FinanceProcurementConstants.DEFAULT_INDICATOR_YES)[0]
        assertTrue(financeText.textCode == 'R0000002')
    }

    /**
     * Test case to test listing header level text by text code and print option indicator.
     */
    @Test
    public void testListHeaderLevelTextByCodeAndPrintOptionInd() {
        def list = financeTextService.listHeaderLevelTextByCodeAndPrintOptionInd('R0000072',
                FinanceProcurementConstants.DEFAULT_INDICATOR_YES)
        assertTrue(list.size() > 1)
    }

    /**
     * Test case to test listing header level text by text code.
     */
    @Test
    public void testListHeaderLevelTextByCode() {
        def list = financeTextService.listHeaderLevelTextByCode('R0000072')
        assertTrue(list.size() > 1)
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
                text: 'Text for test',
                lastModifiedBy: 'TEST_USER',
                vpdiCode: 'TEST'
        )
    }
}

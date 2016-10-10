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
        financeText.save( failOnError: true, flush: true )
        assertNotNull( financeText.id )
    }

    /**
     * Test case method to test listing finance text by text code.
     */
    @Test
    public void testListAllFinanceTextByCode() {
        def list = FinanceText.listAllFinanceTextByCode( 'RSED0003' )
        assert (list.size() > 0)
    }

    /**
     * Test case method to test get finance text by text code and sequence number.
     */
    @Test
    public void testGetFinanceTextByCodeAndItemNumber() {
        def financeText = FinanceText.getFinanceTextByCodeAndItemNumber( 'RSED0003', 1 )[0]
        assertNotNull( financeText )
    }

    /**
     * Test case method to test get finance text by document type for commodity, text code and print option.
     */
    @Test
    public void testGetFinanceTextByDocumentTypeAndCodeAndPrintInd() {
        def financeText = FinanceText.getFinanceTextByDocumentTypeAndCodeAndPrintInd( 1, 'R0000010', FinanceProcurementConstants.DEFAULT_INDICATOR_YES  )[0]
        assertNotNull(financeText.text)
    }


    /**
     * Test case to test update finance text.
     */
    @Test
    public void testUpdateFinanceText() {
        FinanceText financeText = FinanceText.getFinanceTextByCodeAndItemNumber( 'RSED0003', 1 )[0]
        def id = financeText.id
        financeText.textCode = 'UPDATE_TEST'
        financeText.version = null
        if (financeText.isDirty()) {
            financeText.save( failOnError: true, flush: true )
            assertTrue( id == financeText.id )
        } else {
            assertNotNull( financeText.id )
        }
    }

    /**
     * Test Case to test delete FinanceText.
     */
    @Test
    public void testDeleteFinanceText() {
        def rgSize = FinanceText.getFinanceTextByCodeAndItemNumber( 'RSED0003', 1 ).size()
        FinanceText financeText = FinanceText.getFinanceTextByCodeAndItemNumber( 'RSED0003', 1 )[0]
        def textCode = financeText.textCode
        def item = financeText.textItem
        financeText.delete( failOnError: true, flush: true )
        assertTrue( rgSize == FinanceText.getFinanceTextByCodeAndItemNumber( textCode, item ).size() + 1 )
    }

    /**
     * Test case to test get finance text by code, item number and print option indicator.
     */
    @Test
    public void testGetFinanceTextByCodeAndItemNumberAndPrintInd() {
        FinanceText financeText = FinanceText.getFinanceTextByCodeAndItemNumberAndPrintInd( 'RSED0003', 1,
                                                                                            FinanceProcurementConstants.DEFAULT_INDICATOR_YES )[0]
        assertTrue( financeText.textCode == 'RSED0003' && financeText.textItem == 1 )
    }

    /**
     * Test case to test get list of header level text by code and print option indicator.
     */
    @Test
    public void testListHeaderLevelTextByCodeAndPrintOptionInd() {
        def financeTextList = FinanceText.listHeaderLevelTextByCodeAndPrintOptionInd( 'RSED0003',
                                                                                      FinanceProcurementConstants.DEFAULT_INDICATOR_YES )
        assertTrue( financeTextList.size() > 0 )
    }

    /**
     * Test case to test get list of header level text by code.
     */
    public void testListHeaderLevelTextByCode() {
        def financeTextList = FinanceText.listHeaderLevelTextByCode( 'RSED0003' )
        assertTrue( financeTextList.size() > 1 )
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

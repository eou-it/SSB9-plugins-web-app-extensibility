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
 * Integration test case class for service FinanceUnapprovedDocumentServiceI.
 */
class FinanceUnapprovedDocumentServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeUnapprovedDocumentService
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
     * Test case to test find FinanceUnapprovedDocument by document code.
     */
    @Test
    public void testFindByDocumentCode() {
        assertNotNull(financeUnapprovedDocumentService.findByDocumentCode('RSED0007')[0])
    }
}

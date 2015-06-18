/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test class for FinanceApprovalsInProcess.
 */
class FinanceApprovalsInProcessIntegrationTests extends BaseIntegrationTestCase {
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
     * Test case to test find FinanceApprovalsInProcess by document number.
     */
    @Test
    public void testFindByDocumentNumber() {
        assertNotNull(FinanceApprovalsInProcess.findByDocumentNumber('R0000315')[0])
    }
}
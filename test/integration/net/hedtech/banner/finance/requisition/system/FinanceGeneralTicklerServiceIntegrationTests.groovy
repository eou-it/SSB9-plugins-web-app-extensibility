/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case class for FinanceGeneralTicklerService.
 */
class FinanceGeneralTicklerServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeGeneralTicklerService

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
     * Test case to test find general tickler by item sequence number.
     */
    @Test
    public void testFindByReferenceNumber() {
        assertNotNull(financeGeneralTicklerService.findByReferenceNumber('RSED0006')[0])
    }
}

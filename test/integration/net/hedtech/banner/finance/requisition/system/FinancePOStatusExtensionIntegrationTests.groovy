/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class FinancePOStatusExtensionIntegrationTests  extends BaseIntegrationTestCase {
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
     * Test case to test finding FinanceRequestPOVerification by request code.
     */
    @Test
    public void testFindByPOHDCode() {
        assert (!FinanceRequestPOVerification.fetchByRequestCode('R0001113').isEmpty())
    }

    /**
     * Test case to test finding FinanceRequestPOVerification by invalid request code.
     */
    @Test
    public void testFindByInvalidRequestCode() {
        assert (FinanceRequestPOVerification.fetchByRequestCode('INVALID_REQ').isEmpty())
    }

}

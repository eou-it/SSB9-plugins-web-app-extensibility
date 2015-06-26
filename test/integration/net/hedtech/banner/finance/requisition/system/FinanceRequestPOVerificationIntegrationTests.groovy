/** *****************************************************************************
 © 2015 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case class for domain FinanceRequestPOVerification.
 */
class FinanceRequestPOVerificationIntegrationTests extends BaseIntegrationTestCase {
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
    public void testFindByRequestCode() {
        assert (!FinanceRequestPOVerification.findByRequestCode('R0000174').isEmpty())
    }

    /**
     * Test case to test finding FinanceRequestPOVerification by invalid request code.
     */
    @Test
    public void testFindByInvalidRequestCode() {
        assert (FinanceRequestPOVerification.findByRequestCode('INVALID_REQ').isEmpty())
    }
}

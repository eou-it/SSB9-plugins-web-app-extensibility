/** *****************************************************************************
 © 2015 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case class for FinanceBuyerVerificationService.
 */
class FinanceBuyerVerificationServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeBuyerVerificationService

    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    public void setUp() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
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
     * Integration test case to test finding finance buyer verification by document code.
     */
    @Test
    public void testFindByDocumentCode() {
        assert (!financeBuyerVerificationService.findByDocumentCode('R0000057').isEmpty())
    }

    /**
     * Integration test case to test finding finance buyer verification by invalid document code.
     */
    @Test
    public void testFindByInvalidDocumentCode() {
        assert (financeBuyerVerificationService.findByDocumentCode('INVALID_DOCUMENT').isEmpty())
    }
}

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
 * Integration test class for the service FinanceApprovalsInProcessService.
 */
class FinanceApprovalsInProcessServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeApprovalsInProcessService

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
     * Test case to test find FinanceApprovalsInProcess by document code.
     */
    @Test
    public void testFindByDocumentNumber() {
        assertNotNull(financeApprovalsInProcessService.findByDocumentNumber('RSED0007')[0])
    }

}

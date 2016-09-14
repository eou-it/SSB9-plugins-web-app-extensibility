/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.junit.After
import org.junit.Before
import org.junit.Test


class FinancePOStatusExtensionServiceIntegrationTests {
    def financePOStatusExtensionService

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
     * Service integration test case to find Request PO verification by requisition code.
     */
    @Test
    public void testFindByPOHDCode() {
        assert (!financePOStatusExtensionService.findByPOHDCode('R0001113').isEmpty())
    }

    /**
     * Service integration test case to find Request PO verification by invalid requisition code.
     */
    @Test
    public void testFindByInvalidRequestCode() {
        assert (financePOStatusExtensionService.findByPOHDCode('INVALID_CODE').isEmpty())
    }

}

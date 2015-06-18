/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case for service CopyPurchaseRequisitionCompositeService.
 */
class CopyPurchaseRequisitionCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    def copyPurchaseRequisitionCompositeService
    /**
     * Super class setup
     */
    @Before
    void setUp() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test case to test copy requisition.
     */
    @Test
    public void testCopyRequisition() {
        assertNotNull(copyPurchaseRequisitionCompositeService.copyRequisition('R0002230', 'USD'))
    }

    /**
     * Test case to test fail case for copy requisition.
     */
    @Test
    public void testCopyRequisitionFailCase() {
        try {
            copyPurchaseRequisitionCompositeService.copyRequisition('R0002210', 'USD')
        } catch (ApplicationException e) {
            assertApplicationException e, (FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED)
        }
    }
}

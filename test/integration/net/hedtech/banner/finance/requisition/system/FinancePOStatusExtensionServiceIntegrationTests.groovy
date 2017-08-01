/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class FinancePOStatusExtensionServiceIntegrationTests extends BaseIntegrationTestCase {
    def financePOStatusExtensionService

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
     * Service integration test case to find Request PO verification by requisition code.
     */
    @Test
    public void testFindByPOHDCode() {
        assert (!financePOStatusExtensionService.findByPOHDCode('P0000001').isEmpty())
    }

    /**
     * Service integration test case to find Request PO verification by invalid requisition code.
     */
    @Test
    public void testFindByInvalidRequestCode() {
        assert (financePOStatusExtensionService.findByPOHDCode('INVALID_CODE').isEmpty())
    }

}
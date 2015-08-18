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
 * Integration test case class for FinanceInformationPanelCompositeService.
 */
class FinanceInformationPanelCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeInformationPanelCompositeService

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
     * Get information panel data for disapproved status.
     */
    @Test
    public void testGetInformationPanelDataForDisapproved() {
        assert (financeInformationPanelCompositeService.getInformationPanelData(
                FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED, 'RSED0006') != null)
    }

    /**
     * Get information panel data for pending status.
     */
    @Test
    public void testGetInformationPanelDataForPending() {
        assert (financeInformationPanelCompositeService.getInformationPanelData(
                FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING, 'R0000128').isEmpty())
    }

    /**
     * Get information panel data for assigned to buyer status.
     */
    @Test
    public void testGetInformationPanelDataForAssignedToBuyer() {
        assert (!financeInformationPanelCompositeService.getInformationPanelData(
                FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER, 'R0000057').isEmpty())
    }

    /**
     * Get information panel data for converted to PO status.
     */
    @Test
    public void testGetInformationPanelDataForConvertedToPO() {
        assert (!financeInformationPanelCompositeService.getInformationPanelData(
                FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO, 'R0000174').isEmpty())
    }
}

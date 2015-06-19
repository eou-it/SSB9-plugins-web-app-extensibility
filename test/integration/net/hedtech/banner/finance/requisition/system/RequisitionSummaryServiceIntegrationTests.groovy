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
 * Test class for RequisitionSummaryService
 */
class RequisitionSummaryServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionSummaryService
    def springSecurityService

    /**
     * Super class setup
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * test Listing Summary
     */
    @Test
    void testFetchRequisitionSummaryForRequestCode() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode('R0000124')
        assertTrue headers.size() > 0
    }

    /**
     * test Listing Summary
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeForCommodityLevelAccounting() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode('R0001033')
        assertTrue headers.size() > 0
    }

    /**
     * test Listing Summary With Invalid Code
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeInvalidCode() {
        try {
            requisitionSummaryService.fetchRequisitionSummaryForRequestCode('INVALID')
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }
}

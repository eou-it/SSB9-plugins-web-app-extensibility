/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration tests class for RequisitionSummary.
 *
 */
class RequisitionSummaryIntegrationTests extends BaseIntegrationTestCase {

    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()

    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test Fetch Requisition Summary for specified requestCode
     */
    @Test
    void fetchRequisitionSummaryForRequestCode() {
        def requestSummary = RequisitionSummary.fetchRequisitionSummaryForRequestCode 'R0000124';
        assertTrue requestSummary.size() > 0
    }
}

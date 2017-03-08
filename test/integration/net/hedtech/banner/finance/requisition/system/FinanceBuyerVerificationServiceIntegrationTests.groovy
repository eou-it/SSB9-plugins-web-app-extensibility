/***************************************************************
 Copyright 2015 Ellucian company L.P and its affiliates.
***************************************************************/
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
        assert (!financeBuyerVerificationService.findByDocumentCode('RSED0007').isEmpty())
    }

    /**
     * Integration test case to test finding finance buyer verification by invalid document code.
     */
    @Test
    public void testFindByInvalidDocumentCode() {
        assert (financeBuyerVerificationService.findByDocumentCode('INVALID_DOCUMENT').isEmpty())
    }
}

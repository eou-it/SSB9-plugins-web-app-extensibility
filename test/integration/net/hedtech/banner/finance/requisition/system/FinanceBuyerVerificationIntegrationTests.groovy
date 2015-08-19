/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case class for domain FinanceBuyerVerification.
 */
class FinanceBuyerVerificationIntegrationTests extends BaseIntegrationTestCase {
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
     * Integration test case to test finding Buyer Verification by request code.
     */
    @Test
    public void testFindByDocumentCode() {
        assert (!FinanceBuyerVerification.findByDocumentCode('RSED0007').isEmpty())
    }

    /**
     * Integration test case to test finding Buyer Verification by invalid request code.
     */
    @Test
    public void testFindByInvalidDocumentCode() {
        assert (FinanceBuyerVerification.findByDocumentCode('INVALID_DOCUMENT').isEmpty())
    }
}

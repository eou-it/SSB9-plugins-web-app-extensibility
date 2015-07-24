/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test case class for FinancePendingApproverListService.
 */
class FinancePendingApproverListServiceIntegrationTests extends BaseIntegrationTestCase {
    def financePendingApproverListService

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
     * Test case to test find FinancePendingApproverList by document code.
     */
    @Test
    public void testFindByDocumentCode() {
        assert (financePendingApproverListService.findByDocumentCode('R0000128').isEmpty())
    }

    /**
     * Test case to test find FinancePendingApproverList by Invalid document code.
     */
    @Test
    public void testFindByInvalidDocumentCode() {
        assert (financePendingApproverListService.findByDocumentCode('INVALID_DOC').isEmpty())
    }
}
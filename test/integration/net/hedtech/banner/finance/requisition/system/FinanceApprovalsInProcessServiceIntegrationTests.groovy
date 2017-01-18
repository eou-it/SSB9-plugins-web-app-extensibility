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
        assertNotNull( financeApprovalsInProcessService.findByDocumentNumber( 'RSED0007' )[0] )
    }

    /**
     * Test find by document number and document type
     */
    @Test
    public void testFindByDocumentNumberAndDocumentType() {
        def ret = financeApprovalsInProcessService.findByDocumentNumberAndType( 'RSED0007', 1 )
        assert ret[0].documentNumber == 'RSED0007'

    }

}

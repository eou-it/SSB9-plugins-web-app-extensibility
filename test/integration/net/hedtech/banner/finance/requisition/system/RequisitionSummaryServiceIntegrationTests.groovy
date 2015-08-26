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
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0003', 'USD' )
        assertTrue headers.size() > 0
    }


    /**
     * test Listing Summary
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeWithPublicComment() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0009', 'USD' ,true )
        assertTrue headers.size() > 0
    }

    /**
     * test Listing Summary
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeWithCommodityDescription() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0013', 'USD' ,true )
        assertTrue headers.size() > 0
    }

    /**
     * test Listing Summary
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeWithDocumentLevelAccounting() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0001', 'USD' ,true )
        assertTrue headers.size() > 0
    }

    /**
     * test Listing Summary with PDF
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeWithPdf() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0005', 'USD', false )
        assertTrue headers.size() > 0
    }

    /**
         * test Listing Summary with PDF and Text
         */
        @Test
        void testFetchRequisitionSummaryForRequestCodeWithPdfAndText() {
            def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0003', 'USD', false )
            assertTrue headers.size() > 0
        }


    /**
     * test Listing Summary
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeForCommodityLevelAccounting() {
        def headers = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'RSED0005', 'USD' )
        assertTrue headers.size() > 0
    }

    /**
     * test Listing Summary With Invalid Code
     */
    @Test
    void testFetchRequisitionSummaryForRequestCodeInvalidCode() {
        try {
            requisitionSummaryService.fetchRequisitionSummaryForRequestCode( 'INVALID', 'USD' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }
}

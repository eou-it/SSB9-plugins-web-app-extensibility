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
 * Test class for FinancePurchaseRequisitionPDFService
 */
class FinancePurchaseRequisitionPDFServiceIntegrationTests extends BaseIntegrationTestCase {


    def financePurchaseRequisitionPDFService
    def springSecurityService

    /**
     * Super class setup
     */
    @Before
    void setUp() {
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
     * test getPdfFileName
     */
    @Test
    void getPdfFileName() {
        def fileName = financePurchaseRequisitionPDFService.getPdfFileName( 'RSED0001' )
        assertTrue fileName == 'Purchase_Requisition_RSED0001.pdf'
    }

    /**
     * test generate Pdf Stream
     */
    @Test
    void generatePdfStream() {
        login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pdfStream = financePurchaseRequisitionPDFService.generatePdfStream( 'RSED0001','USD')
        assertTrue pdfStream != null
    }
}

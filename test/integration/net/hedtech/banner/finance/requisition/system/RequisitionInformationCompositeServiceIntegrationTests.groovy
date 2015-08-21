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
 * Test class for Requisition Details Composite Service
 */
class RequisitionInformationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionInformationCompositeService
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
     * Test fetchPurchaseRequisition
     */
    @Test
    void fetchPurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionInformationCompositeService.fetchPurchaseRequisition('RSED0009', 'USD').header.requestCode == 'RSED0001'
    }

    /**
     * Test fetchPurchaseRequisition
     */
    @Test
    void fetchPurchaseRequisitionHeaderCurrCheck() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionInformationCompositeService.fetchPurchaseRequisition('RSED0001', 'USD').header.requestCode == 'RSED0001'
    }

}

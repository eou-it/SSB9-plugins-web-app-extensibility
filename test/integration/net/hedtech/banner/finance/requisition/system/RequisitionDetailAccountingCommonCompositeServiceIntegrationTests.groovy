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
 * Test class for Requisition Details And Accounting Common Composite Service
 */
class RequisitionDetailAccountingCommonCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionDetailsAcctCommonCompositeService
    def requisitionAccountingService
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
     * Test adjustAccountPercentageAndAmount for DLA
     */
    @Test
    void adjustAccountPercentageAndAmountDLA() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( requisitionAccountingService.findAccountingByRequestCode( 'RSD00003' )[0] ) > 0.0
    }

    /**
     * Test adjustAccountPercentageAndAmount for CLA
     */
    @Test
    void adjustAccountPercentageAndAmountCLA() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( requisitionAccountingService.findAccountingByRequestCode( 'RSD00005' )[0] ) > 0.0
    }
}

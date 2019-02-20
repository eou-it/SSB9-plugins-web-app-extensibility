/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
/**
 * Test class for Requisition Details And Accounting Common Composite Service
 */

@Integration
@Rollback
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
        logout()
    }

    /**
     * Test adjustAccountPercentageAndAmount for DLA
     */
    @Test
    void adjustAccountPercentageAndAmountDLA() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( requisitionAccountingService.findAccountingByRequestCode( 'RSED0003' )[0] ) > 0.0
    }

    /**
     * Test adjustAccountPercentageAndAmount for CLA
     */
    @Test
    void adjustAccountPercentageAndAmountCLA() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionDetailsAcctCommonCompositeService.adjustAccountPercentageAndAmount( requisitionAccountingService.findAccountingByRequestCode( 'RSED0005' )[0] ) > 0.0
    }
}

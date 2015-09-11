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

class FinanceObjectSequenceServiceIntegrationTests extends BaseIntegrationTestCase {

    def financeObjectSequenceService

    /**
     * Super class setup
     */
    @Before
    void setUp() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
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
     * Test case to test pre create method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreCreateFailCase() {
        def map = [:]
        financeObjectSequenceService.create( map )
    }

    /**
     * Test case to test pre delete method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
     void testPreDeleteFailCase() {
        def map = [:]
        financeObjectSequenceService.delete( map )
    }

    /**
     * Test findNextSequenceNumber
     */
    @Test
    void findNextSequenceNumber() {
        def record = financeObjectSequenceService.findNextSequenceNumber()
        assertNotNull record
    }

}

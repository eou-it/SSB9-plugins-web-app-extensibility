/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * The test case class for RequisitionConfigurationCompositeService.
 *
 */
class RequisitionConfigurationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    def requisitionConfigurationCompositeService
    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        super.login FinanceValidationConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceValidationConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * The test case to test the getInstitutionBasedCurrency method which will return the Map.
     */
    @Test
    public void testGetInstitutionBasedCurrency() {
        def map = requisitionConfigurationCompositeService.getInstitutionBasedCurrency()
        assertNotNull( map )
    }
}

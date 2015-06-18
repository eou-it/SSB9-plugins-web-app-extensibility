/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package profile

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class for UserProfileCompositeService
 */
class UserProfileCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def userProfileCompositeService
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
     * Test getProfiles
     */
    @Test
    void getUserProfile() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def profile = userProfileCompositeService.getUserProfileDetail( springSecurityService.getAuthentication()?.user, new Date() )
        assertTrue profile.baseUserProfile.userId == FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME
    }
}

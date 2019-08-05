/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package profile

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
/**
 * Test class for UserProfileCompositeService
 */

@Integration
@Rollback
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

    @Test
    void getUserProfileForParam() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def profile = userProfileCompositeService.getUserProfileDetail( springSecurityService.getAuthentication()?.user, new Date() )
        assertNotNull profile.baseUserProfile.requesterShipCode
        assertNotNull profile.baseUserProfile.requesterOrgCode
        assertNotNull profile.baseUserProfile.requesterEmailAddress
        assertNotNull profile.shipTo.zipCode
        assertNotNull profile.shipTo.addressLine2
        assertNotNull profile.organization
        assertNotNull profile.coa
        assertNotNull profile.coa.title
        assertNotNull profile.coa.chartOfAccountsCode
    }

    /**
     * Test  get User Profile With Invalid Data (COA, Ship code and Org code)
     */
    @Test
    void getUserProfileWithInvalidData() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        useTransactions = true
        Session session = sessionFactory.getCurrentSession()
        session.createSQLQuery( "UPDATE FOBPROF set FOBPROF_REQUESTOR_SHIP_CODE = 'INVAL', FOBPROF_REQUESTER_ORGN_CODE= 'INVAL' , FOBPROF_COAS_CODE='Z' WHERE FOBPROF_USER_ID = '" +
                                        FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME + "'" ).executeUpdate()
        def profile = userProfileCompositeService.getUserProfileDetail( springSecurityService.getAuthentication()?.user, new Date() )
        assertTrue profile.baseUserProfile.userId == FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME
    }

    /**
     * Test  get User Profile With null Data (COA, Ship code and Org code)
     */
    @Test
    void getUserProfileWithNullData() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        useTransactions = true
        Session session = sessionFactory.getCurrentSession()
        session.createSQLQuery( "UPDATE FOBPROF set FOBPROF_REQUESTOR_SHIP_CODE = null , FOBPROF_REQUESTER_ORGN_CODE= null WHERE FOBPROF_USER_ID = '" +
                                        FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME + "'" ).executeUpdate()
        def profile = userProfileCompositeService.getUserProfileDetail( springSecurityService.getAuthentication()?.user, new Date() )
        assertTrue profile.baseUserProfile.userId == FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME
    }
}

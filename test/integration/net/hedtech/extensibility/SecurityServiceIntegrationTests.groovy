/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class SecurityServiceIntegrationTests extends BaseIntegrationTestCase {
    def selfServiceBannerAuthenticationProvider

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
        //logout()
    }

    // These tests require Banner CATALOG seed data, and only ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M configured as admin for web extensibility

    @Test
    void testPrerequisites() {
        assert grails.util.Holders.getConfig().webAppExtensibility.adminRoles == "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
    }


    @Test
    void testUserWithAdminRole() {
        def auth = selfServiceBannerAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken('CBUNTE3', '111111'))
        SecurityContextHolder.getContext().setAuthentication(auth)
        assertNotNull auth
        def hasAdminRole = SecurityService.userHasAdminRole()
        logout()
        assert hasAdminRole == true
    }


    @Test
    void testUserWithoutAdminRole() {
        def auth = selfServiceBannerAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken('HOF00760', '111111'))
        SecurityContextHolder.getContext().setAuthentication(auth)
        assertNotNull auth
        def hasAdminRole = SecurityService.userHasAdminRole()
        logout()
        assert hasAdminRole == false
    }
}

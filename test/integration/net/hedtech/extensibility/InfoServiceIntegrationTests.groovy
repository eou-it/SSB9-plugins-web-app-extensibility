/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class InfoServiceIntegrationTests extends BaseIntegrationTestCase {
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
    void testUserWithoutAdminRole() {
        def res = InfoService.getJSON(null,"web-app-extensibility")
        assert res.contains("web-app-extensibility")
    }
}

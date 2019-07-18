/*******************************************************************************
 Copyright 2018-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


@Integration
@Rollback
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

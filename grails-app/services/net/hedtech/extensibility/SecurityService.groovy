/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import grails.plugin.springsecurity.SpringSecurityUtils

class SecurityService {
    //static transactional = false

    static def userHasAdminRole() {
        def adminRoles = grails.util.Holders.config.webAppExtensibility.adminRoles?: ''
        return SpringSecurityUtils.ifAnyGranted(adminRoles)
    }
}

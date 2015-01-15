/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class SecurityService {
    static transactional = false
    static def adminRoles = grails.util.Holders.getConfig().webAppExtensibility.adminRoles

    static def userHasAdminRole() {
        return  SpringSecurityUtils.ifAnyGranted(adminRoles)
    }
}

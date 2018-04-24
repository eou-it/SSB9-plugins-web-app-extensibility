/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

import grails.util.Environment

class WebAppExtensibilityConfig extends Script {
    def run() {
        if (formControllerMap['restfulapi']) {
            println "*WARNING* restfulapi is already configured in the formControllerMap, please make sure ['SELFSERVICE'] is included"
        } else {
            formControllerMap << ['restfulapi': ['SELFSERVICE']]
        }

        if (!webAppExtensibility.adminRoles) {
            //When in production do not use a default admin role
            webAppExtensibility.adminRoles = Environment.current == Environment.PRODUCTION ? "" : "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
        }
        def adminRoles = webAppExtensibility.adminRoles.tokenize(',')  // List of adminRoles for Spring security

        // Spring security
        // Make sure to add the extensibility security at the start (odd, a Map should have no order, but spring security appears to consider order)
        def interceptUrlMap = [
                '/internal/**' : ['IS_AUTHENTICATED_ANONYMOUSLY'],
                '/templates/**': ['IS_AUTHENTICATED_ANONYMOUSLY']
        ]

        if(adminRoles){

            interceptUrlMap << ['/webadmin/**' : adminRoles ]
        }

        interceptUrlMap << grails.plugin.springsecurity.interceptUrlMap
        grails.plugin.springsecurity.interceptUrlMap = interceptUrlMap

        //Add Rest configuration if not already added in the main config file.
        if (!restfulApi) {
            // ******************************************************************************
            //             RESTful API Custom Response Header Name Configuration
            // ******************************************************************************
            restfulApi.header.totalCount = 'X-hedtech-totalCount'
            restfulApi.header.pageOffset = 'X-hedtech-pageOffset'
            restfulApi.header.pageMaxSize = 'X-hedtech-pageMaxSize'
            restfulApi.header.message = 'X-hedtech-message'
            restfulApi.header.mediaType = 'X-hedtech-Media-Type'
            // ******************************************************************************
            //             RESTful API 'Paging' Query Parameter Name Configuration
            // ******************************************************************************
            restfulApi.page.max = 'max'
            restfulApi.page.offset = 'offset'
        }
    }
}


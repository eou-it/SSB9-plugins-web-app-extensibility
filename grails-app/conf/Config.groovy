/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

// configuration for plugin testing - will not be included in the plugin zip

import net.hedtech.banner.configuration.ApplicationConfigurationUtils as ConfigFinder
import grails.plugins.springsecurity.SecurityConfigType

// ******************************************************************************
//
//                       +++ EXTERNALIZED CONFIGURATION +++
//
// ******************************************************************************
//
// Config locations should be added to the map used below. They will be loaded based upon this search order:
// 1. Load the configuration file if its location was specified on the command line using -DmyEnvName=myConfigLocation
// 2. Load the configuration file if it exists within the user's .grails directory (i.e., convenient for developers)
// 3. Load the configuration file if its location was specified as a system environment variable
//
// Map [ environment variable or -D command line argument name : file path ]

grails.config.locations = [] // leave this initialized to an empty list, and add your locations in the map below.

def locationAdder = ConfigFinder.&addLocation.curry(grails.config.locations)

[
        BANNER_APP_CONFIG:                 "banner_configuration.groovy",
        WEB_APP_EXTENSIBILITY_CONFIG:      "WebAppExtensibilityConfig.class"
].each { envName, defaultFileName -> locationAdder( envName, defaultFileName ) }

grails.config.locations.each {
    println "configuration: " + it
}

grails.plugins.springsecurity.useRequestMapDomainClass = false
grails.plugins.springsecurity.providerNames = ['casBannerAuthenticationProvider', 'selfServiceBannerAuthenticationProvider', 'bannerAuthenticationProvider']
//grails.plugins.springsecurity.rejectIfNoRule = true

grails.plugins.springsecurity.filterChain.chainMap = [
        '/**': 'securityContextPersistenceFilter,logoutFilter,authenticationProcessingFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,exceptionTranslationFilter,filterInvocationInterceptor'
]

grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap

// Configuration for a Banner self service application
ssbEnabled = true
ssbOracleUsersProxied = true


log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    root {
        off 'stdout'
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

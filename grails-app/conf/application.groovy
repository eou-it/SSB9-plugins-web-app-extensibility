/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

// configuration for plugin testing - will not be included in the plugin zip

//TODO Add Dependency
//import net.hedtech.banner.configuration.ApplicationConfigurationUtils as ConfigFinder
//import grails.plugin.springsecurity.SecurityConfigType

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

grails.plugin.springsecurity.useRequestMapDomainClass = false
grails.plugin.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap

// Configuration for a Banner self service application
ssbEnabled = true
ssbOracleUsersProxied = true


dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
    //show_sql = true
}

// environment specific settings
environments {
    development {
        dataSource {
        }
    }
    test {
        dataSource {
        }
    }
    production {
        dataSource {
        }
    }
}

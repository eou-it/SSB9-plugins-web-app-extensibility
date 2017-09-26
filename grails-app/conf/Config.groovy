/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

import net.hedtech.banner.configuration.ApplicationConfigurationUtils as ConfigFinder
import grails.plugin.springsecurity.SecurityConfigType

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
grails.config.locations = [] // leave this initialized to an empty list, and add your locations
// in the APPLICATION CONFIGURATION section below.
def locationAdder = ConfigFinder.&addLocation.curry(grails.config.locations)

[BANNER_APP_CONFIG:                      "banner_configuration.groovy",
        BANNER_FINANCE_SSB_CONFIG:  "${userHome}/.grails/FinanceSelfService_configuration.groovy"
].each { envName, defaultFileName -> locationAdder(envName, defaultFileName) }

grails.databinding.useSpringBinder=true

// You must create a small configuration file named 'banner_on_grails-local-config.groovy' (as referenced
// in the above map defining externalized configuration files) that contains your own specific
// configuration (e.g., URIs, usernames, etc.).  Following is an example of this file:

/* ***************************** EXAMPLE local file ******************************
myDataSource.username = "banproxy"
myDataSource.password = "u_pick_it"
myDataSource.driver = "oracle.jdbc.OracleDriver"
myDataSource.url = "jdbc:oracle:thin:@{YOUR_HOST}:1521:ban83"
myDataSource.jndiName = "jdbc/horizonDataSource"
********************************************************************************* */

/* *************** ANOTHER EXAMPLE of the same local file ***********************
def username = "banproxy"
def password = "u_pick_it"
def url      ="jdbc:oracle:thin:@MAL0600026.corp.sct.com:1521:ban83"
def driver   = "oracle.jdbc.OracleDriver"

// Note: When using the com.elvyx.Driver, you may run the standalone elvyx client to see the actual SQL being executed.
// You must download the elvyx-1.0.24_beta.zip from http://sourceforge.net/projects/elvyx/files and unzip where you want to keep it.
// Note: You do NOT need to add the jar file to the project -- it is already present.
// Next, Update the url below in this file for your environment, then
//       Run the elvyz.bat or elvyz.sh file to launch the swing UI, and lastly
//       Rrun your tests or the grails application.

myDataSource.username = username
myDataSource.password = password

myDataSource.driver = driver
// myDataSource.driver = "com.elvyx.Driver"

myDataSource.url = url
// myDataSource.url = "jdbc:elvyx://localhost:4448/?elvyx.real_driver=$driver&elvyx.real_jdbc=$url&user=$username&password=$password"

myDataSource.jndiName = "jdbc/horizonDataSource"
********************************************************************************* */

grails.project.groupId = "net.hedtech" // used when deploying to a maven repo

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64  **** Charlie note: Setting this to html will ensure html is escaped, to prevent XSS attack ****
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"

grails.converters.domain.include.version = true
grails.converters.json.date = "javascript"
grails.converters.json.pretty.print = true

banner {
    sso {
        authenticationProvider = 'default'
        authenticationAssertionAttribute = 'udcId'
    }
}

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = false

// enable GSP preprocessing: replace head -> g:captureHead, title -> g:captureTitle, meta -> g:captureMeta, body -> g:captureBody
grails.views.gsp.sitemesh.preprocess = true

// set per-environment serverURL stem for creating absolute links
environments {
    production {
//        grails.serverURL = "http://localhost:8080/${appName}"
    }
    development {
//        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
//        grails.serverURL = "http://localhost:8080/${appName}"
    }
}

// ******************************************************************************
//
//                       +++ DATA ORIGIN CONFIGURATION +++
//
// ******************************************************************************
// This field is a Banner standard, along with 'lastModifiedBy' and lastModified.
// These properties are populated automatically before an entity is inserted or updated
// within the database. The lastModifiedBy uses the username of the logged in user,
// the lastModified uses the current timestamp, and the dataOrigin uses the value
// specified here:
dataOrigin = "Banner"

grails.plugin.springsecurity.useRequestMapDomainClass = false

grails.plugin.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap


// ******************************************************************************
//
//                       +++ INTERCEPT-URL MAP +++
//
// ******************************************************************************

grails.plugin.springsecurity.interceptUrlMap = [
        '/': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/ssb/menu/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/login/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/mainPage**': ['ROLE_SCACRSE_BAN_DEFAULT_M'],
        '/menu/**': ['ROLE_SCACRSE_BAN_DEFAULT_M'],
        '/index**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/js/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/plugins/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/errors/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],

        // ALL URIs specified with the BannerAccessDecisionVoter.ROLE_DETERMINED_DYNAMICALLY
        // 'role' (it's not a real role) will result in authorization being determined based
        // upon a user's role assignments to the corresponding form (see 'formControllerMap' above).
        // Note: This 'dynamic form-based authorization' is performed by the BannerAccessDecisionVoter
        // registered as the 'roleVoter' within Spring Security.
        //
        // Only '/name_used_in_formControllerMap/' and '/api/name_used_in_formControllerMap/'
        // URL formats are supported.  That is, the name_used_in_formControllerMap must be first, or
        // immediately after 'api' -- but it cannot be otherwise nested. URIs may be protected
        // by explicitly specifying true roles instead -- as long as ROLE_DETERMINED_DYNAMICALLY
        // is NOT specified.
        //
       '/**': [ 'ROLE_DETERMINED_DYNAMICALLY' ]
]

// This is just used to provide a place that we can drive those controllers that map to existing forms in Banner.
// This is purely temporary, and is used to highlight these pages within the main page.
convertedPages = ["medicalCondition", "disability", "medicalEquipment", "disabilityAssistance", "medicalInformation"]


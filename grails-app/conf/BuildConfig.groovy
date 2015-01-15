/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

//grails.project.war.file = "target/${appName}-${appVersion}.war"

if (Environment.current == Environment.TEST) {
    grails.plugin.location.'banner-core' = "../banner_core.git"
}

//grails.project.dependency.resolver = "ivy"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {

        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        } else {
            grailsPlugins()
            grailsHome()
            grailsCentral()
            
            mavenCentral()
            mavenRepo "http://repository.jboss.org/maven2/"
            mavenRepo "http://repository.codehaus.org"
        }
    }

    plugins {
        compile ":hibernate:3.6.10.10"
        compile ":tomcat:7.0.52.1"
        test ':code-coverage:1.2.5'
    }

    dependencies {

    }
}

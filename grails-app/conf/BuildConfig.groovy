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

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        }
        mavenRepo "http://repo.grails.org/grails/repo"
            grailsCentral()
            mavenCentral()
            mavenRepo "https://code.lds.org/nexus/content/groups/main-repo"
            mavenRepo "http://repository.jboss.org/maven2/"
    }

    plugins {
        runtime  ":hibernate:3.6.10.19"
        build ":tomcat:7.0.55.2"
        test ':code-coverage:2.0.3-3'
    }

    dependencies {

    }
}

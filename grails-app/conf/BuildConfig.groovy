/*******************************************************************************
  Copyright 2015 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

tomcat.deploy.username = "manager"
tomcat.deploy.password = "manager!"
grails.project.class.dir = "target/classes"
grails.project.lib.dir = "lib"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

// When deploying a war it is important to exclude the Oracle database drivers.  Not doing so will
// result in the all-too-familiar exception:
// "Cannot cast object 'oracle.jdbc.driver.T4CConnection@6469adc7'... to class 'oracle.jdbc.OracleConnection'

grails.plugin.location.'banner-core' = "../banner_core.git"
grails.plugin.location.'banner-codenarc' = "../banner_codenarc.git"
grails.plugin.location.'i18n-core' = "../i18n_core.git"
grails.plugin.location.'banner-seeddata-catalog'="../banner_seeddata_catalog.git"
grails.plugin.location.'banner-finance-validation'="../banner_finance_validation.git"
grails.plugin.location.'banner-finance-validation_common'="../banner_finance_validation_common.git"
grails.plugin.location.'banner_general_validation_common'="../banner_general_validation_common.git"

grails.war.resources = { stagingDir ->
    delete(file: "${stagingDir}/WEB-INF/lib/ojdbc6.jar")
}

grails.project.dependency.resolver = "ivy"

grails.project.dependency.resolution = {

    inherits "global" // inherit Grails' default dependencies
    log "warn"        // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
            mavenRepo "${System.properties['RELEASE_REPO_NAME']}"
            print ">>>>>>>  nnnnnnnn Lokee ${System.properties['RELEASE_REPO_NAME']}"
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
        compile ":spring-security-core:1.2.7.3"
        test ':code-coverage:1.2.5'
        runtime  ":hibernate:3.6.10.10"
        compile ":tomcat:7.0.52.1"
        compile ':resources:1.2.7' // If the functional-test plugin is being used
        compile ":functional-test:2.0.0" // If the functional-test plugin is being used
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // Note: elvyx-1.0.24_beta.jar remains in the lib/ directory of the project as it is not available in a public repo due to licensing issues.
        test (
                "net.hedtech.banner.pdf:banner_pdf_generator:1.0",
                "org.apache.avalon.framework:avalon-framework-api:4.3.1",
                "org.apache.avalon.framework:avalon-framework-impl:4.3.1",
                "org.json:json:20090211",
                "org.apache.xmlgraphics:fop:1.1"
        )
    }

}

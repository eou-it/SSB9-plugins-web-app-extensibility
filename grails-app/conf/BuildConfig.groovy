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

grails.plugin.location.'banner-codenarc' = "../banner_codenarc.git"
grails.plugin.location.'banner-finance-validation' = "../banner_finance_validation.git"
grails.plugin.location.'banner-finance-validation_common' = "../banner_finance_validation_common.git"
grails.plugin.location.'banner_general_validation_common' = "../banner_general_validation_common.git"
grails.plugin.location.'banner_general_person' = "../banner_general_person.git"
grails.plugin.location.'banner_document_management' = "../banner_document_management.git"

grails.war.resources = {stagingDir ->
    delete( file: "${stagingDir}/WEB-INF/lib/ojdbc6.jar" )
}

grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {

    inherits "global" // inherit Grails' default dependencies
    log "warn"        // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
            mavenRepo "${System.properties['RELEASE_REPO_NAME']}"
        }
        grailsCentral()
        mavenCentral()
        mavenRepo "http://repository.jboss.org/maven2/"
    }

    plugins {
    }


    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // Note: elvyx-1.0.24_beta.jar remains in the lib/ directory of the project as it is not available in a public repo due to licensing issues.
        test(
                "net.hedtech.banner.pdf:banner_pdf_generator:1.0",
                "org.apache.avalon.framework:avalon-framework-api:4.3.1",
                "org.apache.avalon.framework:avalon-framework-impl:4.3.1",
                "org.json:json:20090211",
                "org.apache.xmlgraphics:batik-ext:1.7",
                "org.apache.xmlgraphics:fop:1.1"

        ) {
            excludes 'xml-apis'
        }
        /*test (  "net.hedtech.banner.pdf:banner_pdf_generator:1.0",
              "org.apache.xmlgraphics:fop:1.1",
              "org.apache.xmlgraphics:batik-transcoder:1.8",
              "org.apache.xmlgraphics:batik-codec:1.8",
              "org.apache.xmlgraphics:batik-awt-util:1.8",
              "org.apache.xmlgraphics:batik-bridge:1.8",
              "org.apache.xmlgraphics:batik-dom:1.8",
              "org.apache.xmlgraphics:batik-gvt:1.8",
              "org.apache.xmlgraphics:batik-svg-dom:1.8",
              "org.apache.xmlgraphics:batik-svggen:1.8",
              "org.apache.xmlgraphics:batik-util:1.8",
              "org.apache.xmlgraphics:batik-xml:1.8",
              "org.apache.xmlgraphics:batik-anim:1.8",
              "org.apache.xmlgraphics:batik-css:1.8",
              "org.apache.xmlgraphics:batik-ext:1.8",
              "org.apache.xmlgraphics:batik-parser:1.8",
              "org.apache.xmlgraphics:batik-script:1.8",
              "org.apache.xmlgraphics:xmlgraphics-commons:1.5",
              "commons-logging:commons-logging:1.0.4",
              "org.apache.avalon.framework:avalon-framework-api:4.3.1",
              "org.apache.avalon.framework:avalon-framework-impl:4.3.1",
              "xml-apis:xml-apis-ext:1.3.04",
              "org.json:json:20090211" )
                {transitive = false}*/
    }

}

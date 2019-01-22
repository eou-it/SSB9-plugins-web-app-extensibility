package web.app.extensibility

import grails.plugins.*
import grails.util.Environment
import grails.util.Holders
import net.hedtech.extensibility.metadata.ExtensionService
import net.hedtech.extensibility.metadata.ResourceService

class WebAppExtensibilityGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    List loadAfter = ['bannerCore']
    def dependsOn = [
            bannerCore: '9.28.1 => *'
    ]
    // TODO Fill in these fields
    def title = "Web App Extensibility" // Headline display name of the plugin
    def author = "Your name"
    def authorEmail = ""
    def description = '''\
Brief summary/description of the plugin.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/web-app-extensibility"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() { {->
            setupExternalConfig()
        }
    }

    void doWithDynamicMethods() {
        // TODO Implement registering dynamic methods to classes (optional)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }

    private setupExternalConfig(){
        def config = Holders.config
        ResourceService.resourcePath = config?.webAppExtensibility.locations.resources
        ExtensionService.extensionsPath =config?.webAppExtensibility.locations.extensions
        def roles = config.webAppExtensibility.adminRoles
        if (!roles) {
            //When in production do not use a default admin role
            roles = Environment.current == Environment.PRODUCTION ? "" : "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
        }
        def adminRoles = roles.tokenize(',')  // List of adminRoles for Spring security

        // Spring security
        // Make sure to add the extensibility security at the start (odd, a Map should have no order, but spring security appears to consider order)
        config.grails.plugin.springsecurity.interceptUrlMap << [
                [pattern:'/internal/**', access:['IS_AUTHENTICATED_ANONYMOUSLY']],
                [pattern:'/webadmin/**' , access:['IS_AUTHENTICATED_ANONYMOUSLY']]
        ]

        if(adminRoles){
            config.grails.plugin.springsecurity.interceptUrlMap  << [pattern:'/webadmin/**' , access:[adminRoles]]
        }
    }
}

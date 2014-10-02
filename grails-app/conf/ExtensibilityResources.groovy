/**
 * Created by hvthor on 2014-08-21.
 */


modules = {
    'extensibilityCommon' {
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-common/xe-common.js']
    }
    'extensibilityAngular' {
        dependsOn "extensibilityCommon"
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-angular/xe-angular.js']
    }
    'extensibilityJQuery' {
        dependsOn "extensibilityCommon"
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-jquery/xe-jquery.js']
    }
}
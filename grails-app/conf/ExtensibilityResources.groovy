/**
 * Created by hvthor on 2014-08-21.
 */


modules = {
    'extensibilityCommon' {
        resource url: [plugin: 'web-app-extensibility', file: 'css/extensibility-ss.css'], attrs: [media: 'screen, projection']
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
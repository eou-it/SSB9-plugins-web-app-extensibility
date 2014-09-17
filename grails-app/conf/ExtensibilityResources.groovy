/**
 * Created by hvthor on 2014-08-21.
 */


modules = {
    'extensibilityAngular' {
        dependsOn "extensibilityCommon"
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-angular/xe-angular.js']
    }
    'extensibilityCommon' {
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-common/xe-common.js']
    }
}
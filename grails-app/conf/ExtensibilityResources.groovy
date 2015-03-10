/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

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
    'extensibilityAngularRTL' {
        dependsOn "extensibilityAngular, extensibilityCommonRTL"
    }
    'extensibilityJQueryRTL' {
        dependsOn "extensibilityJQuery, extensibilityCommonRTL"
    }
    'extensibilityCommonRTL' {
        resource url: [plugin: 'web-app-extensibility', file: 'css/extensibility-ss-rtl.css'], attrs: [media: 'screen, projection']
        resource url: [plugin: 'web-app-extensibility', file: 'css/extensibility-ss-rtl-patch.css'], attrs: [media: 'screen, projection']
    }
}

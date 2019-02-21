package web.app.extensibility
/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

class ExtensibilityUrlMappings {
    static mappings = {
        "/webadmin/$pluralizedResourceName"(controller: 'webAppExtensibility') {
            action = [GET: "list", POST: "create"]
        }
    }
}

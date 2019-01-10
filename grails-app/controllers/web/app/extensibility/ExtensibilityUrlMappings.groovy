package web.app.extensibility
/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

class ExtensibilityUrlMappings {
    static mappings = {
        //read only restful api
        /*"/internal/$pluralizedResourceName/$id"(controller: 'webAppExtensibility') {
            action = [GET: "show"]
        }
        "/internal/$pluralizedResourceName"(controller: 'webAppExtensibility') {
            action = [GET: "list"]
        }
        //secured restful api requiring the webtailor admin role
        "/webadmin/$pluralizedResourceName/$id"(controller: 'webAppExtensibility') {
            action = [GET   : "show", PUT: "update",
                      DELETE: "delete"]
        }*/
        "/webadmin/$pluralizedResourceName"(controller: 'webAppExtensibility') {
            action = [GET: "list", POST: "create"]
        }



       /* "/webadmin/extensions"(controller: 'webAppExtensibility' ) {
            action = "extensions"
        }

        "/webadmin/resources" (controller: 'webAppExtensibility' ){
            action = "resources"
        }*/
    }
}

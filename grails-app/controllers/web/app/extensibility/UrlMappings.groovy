package web.app.extensibility

class UrlMappings {

    static mappings = {

        //read only restful api
        "/internal/$pluralizedResourceName/$id"(controller: 'restfulApi') {
            action = [GET: "show"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/internal/$pluralizedResourceName"(controller: 'restfulApi') {
            action = [GET: "list"]
            parseRequest = false
        }
        //secured restful api requiring the webtailor admin role
        "/webadmin/$pluralizedResourceName/$id"(controller: 'restfulApi') {
            action = [GET   : "show", PUT: "update",
                      DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/webadmin/$pluralizedResourceName"(controller: 'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        "/"(view:"/index")
        "500"(view:'/error')
    }
}

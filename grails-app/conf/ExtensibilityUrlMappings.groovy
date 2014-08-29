class ExtensibilityUrlMappings {
    static mappings = {
        //same mapping as for /api except it will have different spring security
        "/internal/$pluralizedResourceName/$id"(controller: 'restfulApi') {
            action = [GET: "show", PUT: "update",
                      DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/internal/$pluralizedResourceName"(controller: 'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }
    }
}
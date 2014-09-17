package net.hedtech.extensibility.metadata
import grails.converters.JSON
import grails.util.Environment

class ExtensionService {
    def static extensionsPath = grails.util.Holders.getConfig().webAppExtensibility.locations.extensions

    //Pages don't have a unique id, have to query by application and page name
    def list(params) {
        if (params.application && params.page) {
            def md = loadFromFile(params.application,params.page)
            if (md)
                return md
        }
    }

    def count(params) {
        return 1
    }

    def create(Map content, params) {
        def result = content.metadata
        println "Saving ${content.application} ${content.page} "
        if (content.application && content.page) {
            saveToFile(content.application, content.page, content.metadata)
            result
        } else {
            throw new Exception("Application and Page are required to save a page extension")
        }
    }

    private def saveToFile(application, page, metadata){
        def json = new JSON(metadata)
        def jsonStr = json.toString(true)
        def file = new File("${extensionsPath}/${application}")
        if (!file?.exists()) {
            file.mkdirs();
        }
        file = new File("${extensionsPath}/${application}/${page}.json")
        file.text=jsonStr
    }

    private def loadFromFile(application,page){
        def result = null
        def file = new File("${extensionsPath}/${application}/${page}.json")
        def jsonStr = null
        if (file?.exists()) {
            jsonStr =file.text
        } else if  ( Environment.getCurrent() != Environment.PRODUCTION )  {
            file = new File("plugins/web-app-extensibility.git/test/data/extensions/${page}.json")
            if (file?.exists()) {
                jsonStr =file.text
            }
        }
        if (jsonStr) {
            JSON.use("deep")
            result = JSON.parse(jsonStr)
        } else {
            println "error loading extensions from ${file.path}"
        }
        result
    }

}

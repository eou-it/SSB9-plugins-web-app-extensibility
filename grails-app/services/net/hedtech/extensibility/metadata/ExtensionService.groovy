package net.hedtech.extensibility.metadata
import grails.converters.JSON
import grails.util.Environment

class ExtensionService {
    def static extensionsPath = grails.util.Holders.getConfig().webAppExtensibility.locations.extensions

    def list(params) {

        if (params.page) {
            def md = loadFromFile(params.page)
            if (md)
                return md
        }
    }

    def count(params) {
        return 1
    }

    private def saveToFile(page, metadata){
        def json = new JSON(metadata)
        def jsonStr = json.toString(true)
        def file = new File("${extensionsPath}/${page}.json")
        file.text=jsonStr
    }

    private def loadFromFile(page){
        def result = null
        def file = new File("${extensionsPath}/${page}.json")
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

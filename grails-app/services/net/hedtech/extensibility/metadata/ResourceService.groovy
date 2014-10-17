package net.hedtech.extensibility.metadata

import grails.converters.JSON
import grails.util.Environment
import org.springframework.web.context.request.RequestContextHolder


class ResourceService {
    def grailsApplication
    def static resourcePath = grails.util.Holders.getConfig().webAppExtensibility.locations.resources

    //Pages don't have a unique id, have to query by application and page name
    def list(params) {
        if (params.application && params.page) {
            def md = loadFromFile(params.application,params.page)
            if (md) {
                return []<<md //list must return array, should really be using get
            }
        }
    }

    def count(params) {
        return 1
    }

    def create(Map content, params) {
        def result = content.metadata
        println "Saving resources for ${content.application} ${content.page} "
        if (content.application && content.page) {
            saveToFile(content.application, content.page, content.metadata)
            result
        } else {
            throw new Exception("Application and Page are required to save page resources")
        }
    }

    private def saveToFile(application, page, metadata){
        def json = new JSON(metadata)
        def jsonStr = json.toString(true)
        def file = new File("${resourcePath}/${application}")
        if (!file?.exists()) {
            file.mkdirs();
        }
        file = new File("${resourcePath}/${application}/${page}.json")
        file.text=jsonStr
    }

    def mergeJsonStr = { result, jsonStr ->
        JSON.use("deep")
        if (!result) {
            result = JSON.parse(jsonStr)
        } else {
            def map = JSON.parse(jsonStr)
            map.each { k,v ->
                result[k] = v
            }
        }
        result
    }

    private def loadFromFile(application,page){
        def request = RequestContextHolder.currentRequestAttributes().request
        def result = null
        //Should really be making use of properties loading mechanisms
        //For now merge files in order _lang, _lang_country,  _lang_country_variant
        def pieces = ["",request.locale.language, request.locale.country, request.locale.variant]
        def postfixes = [""]
        for (i in 1..pieces.size()-1 ){
            if (pieces[i])
                postfixes << "${postfixes[i-1]}_${pieces[i]}"
        }
        postfixes.each { postfix ->
            def file = new File("${resourcePath}/${application}/${page}${postfix}.json")
            if (file?.exists()) {
                result = mergeJsonStr(result, file.text)
            }
        }

        if ( (!result) && Environment.getCurrent() != Environment.PRODUCTION ){
            def file = new File("plugins/web-app-extensibility.git/test/data/i18n/${page}.json")
            if (file?.exists()) {
                result = JSON.parse(file.text)
            }
        }
        if (!result) {
            println "error loading resources from ${file.path}"
        }
        result
    }
}

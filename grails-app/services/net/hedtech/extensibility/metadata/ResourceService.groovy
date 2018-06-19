/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility.metadata

import grails.converters.JSON
import grails.util.Environment
import net.hedtech.banner.i18n.MessageHelper
import org.apache.log4j.Logger
import org.grails.web.converters.exceptions.ConverterException
import org.springframework.web.context.request.RequestContextHolder
import net.hedtech.extensibility.ExtensionUtility


class ResourceService {
    static transactional = false
    def localizerService = { mapToLocalize ->
        new MessageHelper().message(mapToLocalize)
    }

    def grailsApplication
    def static resourcePath = grails.util.Holders.getConfig().webAppExtensibility.locations.resources

    def create(Map content, params) {
        def result = content.metadata
        log.info "Saving resources for ${content.application} ${content.page} "
        if (content.application && content.page) {
            saveToFile(content.application, content.page, content.metadata)
            result
        } else {
            throw new Exception(localizerService(code: "resource.service.create.missing.param"))
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

    static def mergeJSONResult (result, jsonStr) {
        if (!result) {
            try {
                JSON.use("deep") {
                    result = JSON.parse(jsonStr)
                }
            }
            catch (ConverterException ce) {
                //TODO Task for asset pipeline
                //log.error "Error parsing resources json string"
                result = "{}"
            }
        } else {
            def map
            try {
                JSON.use("deep") {
                    map = JSON.parse(jsonStr)
                }
            }
            catch (ConverterException ce) {
                //TODO Task for asset pipeline
                //log.error "Error parsing resources json string"
                map = "{}"
            }
            map.each { k,v ->
                result[k] = v
            }
        }
        result
    }

    private static def loadJSONFromFile(application,page){
        def request = RequestContextHolder.currentRequestAttributes().request
        def result = null
        //Should really be making use of properties loading mechanisms
        //For now merge files in order _lang, _lang_country,  _lang_country_variant
        def pieces = ["",request.locale.language, request.locale.country, request.locale.variant]
        def postfixes = [""]
        for (i in 1..pieces.size()-1 ){
            if (pieces[i]) {
                postfixes << "${ postfixes[i - 1] }_${ pieces[i] }"
            }
        }
        postfixes.each { postfix ->
            def file = resourcePath?new File("${resourcePath}/${application}/${page}${postfix}.json"):null
            if (file?.exists()) {
                result = mergeJSONResult(result, file.text)
            }
        }
        return result.toString()
    }

    static def loadResourcesJSON(requestUri) {
        String pageUrl = requestUri.toString()
        String applicationName = ExtensionUtility.deriveApplicationName(pageUrl)
        String pageName = ExtensionUtility.derivePageName(pageUrl)
        return loadJSONFromFile(applicationName,pageName)
    }

}

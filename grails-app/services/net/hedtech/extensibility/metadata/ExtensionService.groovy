/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility.metadata
import grails.converters.JSON
import net.hedtech.banner.i18n.MessageHelper
import org.grails.web.converters.exceptions.ConverterException
import grails.util.Environment
import net.hedtech.extensibility.ExtensionUtility
import groovy.util.logging.Slf4j

@Slf4j
class ExtensionService {
    static transactional = false
    def static extensionsPath
    def static localizerService = { mapToLocalize ->
        new MessageHelper().message(mapToLocalize)
    }
    //Pages don't have a unique id, have to query by application and page name
    def list(params) {
        def md = []
        if (params.application && params.page) {
            def result = loadFromFile(params.application,params.page)
            if (result) {
                md << result
            }
        }
        return md
    }

    def count(params) {
        return 1
    }

    def create(Map content, params) {
        def result = content.metadata
        log.info "Saving ${content.application} ${content.page} "
        if (content.application && content.page) {
            saveToFile(content.application, content.page, content.metadata)
            result
        } else {
            throw new Exception(localizerService(code: "extension.service.create.missing.param"))
        }
    }

    private def saveToFile(application, page, metadata){
        if ( !extensionsPath ) {
            throw new Exception(localizerService(code: "extension.service.save.missing.extensionsPath"))
        }
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
        def result = "{}"
        def file = extensionsPath?new File("${extensionsPath}/${application}/${page}.json"):null
        def jsonStr = null
        if (file?.exists()) {
            try {
                jsonStr = file.text
            }
            catch (IOException ioe) {
                //TODO Task for asset pipeline
                log.error "Error reading extensions json file ${file.path}: " + ioe.stackTrace
            }
            if (jsonStr) {
                try {
                    JSON.use("deep") {
                        result = JSON.parse(jsonStr)
                    }
                }
                catch (ConverterException ce) {
                    //TODO Task for asset pipeline
                    log.error "Error parsing extensions json from ${file.path}: " + ce.stackTrace
                }
            } else {
                //TODO Task for asset pipeline
                log.error "error loading extensions from ${file.path}"
            }
        }
        result
    }

    private static def loadJSONStringFromFile(application,page){
        def result, emptyJSON= "{}"
        def file = new File("${extensionsPath}/${application}/${page}.json")
        def jsonStr = null
        if (file?.exists()) {
            try {
                jsonStr = file.text
            }
            catch (IOException ioe) {
                //TODO Task for asset pipeline
                log.error "Error reading extensions json file ${file.path}: " + ioe.stackTrace
            }
            if (jsonStr) {
                 //handle compatibility with older version where extensions were defined in an array
                if (jsonStr[0].equals("[") && jsonStr[jsonStr.length()-1].equals("]") && jsonStr!="[]") {
                    jsonStr = jsonStr.substring(1,jsonStr.length()-1)
                }
                try {
                    JSON.use("deep") {
                        result = JSON.parse(jsonStr)
                    }
                }
                catch (ConverterException ce) {
                    def parseError = ce.getCause().getMessage()
                    //TODO Task for asset pipeline
                    //log.error "Error parsing extensions json from ${file.path}: " + parseError
                    throw new Exception(localizerService(code: "xe.extensions.json.load.error",args: [parseError]))
                }
                return jsonStr
            } else {
                //TODO Task for asset pipeline
                log.error "error loading extensions from ${file.path}"
            }
        }
        emptyJSON
    }

    static def loadExtensionsJSON(requestUri) {
        String pageUrl = requestUri.toString()
        String applicationName = ExtensionUtility.deriveApplicationName(pageUrl)
        String pageName = ExtensionUtility.derivePageName(pageUrl)
        return loadJSONStringFromFile(applicationName,pageName)
    }


}

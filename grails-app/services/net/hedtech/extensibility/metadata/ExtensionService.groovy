/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility.metadata
import grails.converters.JSON
import net.hedtech.banner.i18n.MessageHelper
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import grails.util.Environment
import org.apache.log4j.Logger

class ExtensionService {
    static transactional=false
    def static extensionsPath = grails.util.Holders.getConfig().webAppExtensibility.locations.extensions
    private static final Logger log = Logger.getLogger( this.getClass() )
    def localizerService = { mapToLocalize ->
        new MessageHelper().message(mapToLocalize)
    }
    //Pages don't have a unique id, have to query by application and page name
    def list(params) {
        def md = []
        if (params.application && params.page) {
            md = loadFromFile(params.application,params.page)
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
        def result = []
        def file = new File("${extensionsPath}/${application}/${page}.json")
        def jsonStr = null
        if (!file?.exists() && Environment.getCurrent() != Environment.PRODUCTION) {
            // read the development test file
            file = new File("plugins/web-app-extensibility.git/test/data/extensions/${ page }.json")
        }
        if (file?.exists()) {
            try {
                jsonStr = file.text
            }
            catch (IOException ioe) {
                log.error "Error reading extensions json file ${file.path}: " + ioe.stackTrace
            }
            if (jsonStr) {
                JSON.use("deep")
                try {
                    result = JSON.parse(jsonStr)
                }
                catch (ConverterException ce) {
                    log.error "Error parsing extensions json from ${file.path}: " + ce.stackTrace
                }
            } else {
                log.error "error loading extensions from ${file.path}"
            }
        }
        result
    }

}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import net.hedtech.extensibility.metadata.ExtensionService
import net.hedtech.extensibility.metadata.ResourceService
import grails.util.Holders as CH
import org.springframework.web.context.request.RequestContextHolder


class InfoService {
    //static transactional = false

    // This service provides all information needed by the browser for extensibility as a String representation
    // of a JavaScript Object (JSON)
    static def getJSON( page, pluginUrl) {
        def application = CH.config.app.name
        def request = RequestContextHolder.currentRequestAttributes().request
        return """|{
                  |  "application": "${application}",
                  |  "page": "${page}",
                  |  "url": "$request.contextPath/",
                  |  "pluginUrl": "$pluginUrl/",
                  |  "admin": ${SecurityService.userHasAdminRole()},
                  |  "extensions": ${ExtensionService.loadJSONStringFromFile(application, page)},
                  |  "resources": ${ResourceService.loadJSONFromFile(application,page)}
                  |}""".stripMargin()

    }
}

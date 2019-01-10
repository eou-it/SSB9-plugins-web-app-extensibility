package net.hedtech.banner

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.extensibility.metadata.ExtensionService
import net.hedtech.extensibility.metadata.ResourceService
import net.sf.json.JSON


class WebAppExtensibilityController {
    ExtensionService extensionService
    ResourceService resourceService

  /*  def extensions (params){
      extensionService.create(params)
    }

    def resources (params){
        resourceService.create(params)

    }*/

    public def list() {
        def requestParams = params
        def res = extensionService.list(requestParams)
        render res as JSON
    }

    public def show() {
        def requestParams = params
        def res = extensionService.list(requestParams)
        render res as JSON
    }


    // POST /api/pluralizedResourceName
    //
    public def create() {
        def jsonObj = request.JSON
        try {
            extensionService.create(jsonObj, params)
            //response.setStatus( 201 )
            render(text:"", contentType:'text/plain')
        }catch(ApplicationException e){
            response.setStatus(501)
            println(e.getMessage())
            render(text:"", contentType:'text/plain')
        }
    }



}

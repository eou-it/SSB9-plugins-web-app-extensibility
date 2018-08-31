/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility.metadata

import grails.converters.JSON
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.codehaus.groovy.grails.web.json.JSONObject
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
class ExtensionServiceSpec extends Specification{

    def jsonData = '''{
               "sections":[
                  {
                     "name":"extzToolList",
                     "exclude":false,
                     "fields":[
                        {
                           "name":"about",
                           "exclude":false
                        },
                        {
                           "name":"uploadProperties",
                           "exclude":false
                        }
                     ]
                  }
               ]
            }'''

    def extensionService = new ExtensionService()

    def params = [unitTest: true, dummy: 1, application:"banner_extensibility", page:"Test",
                  "metadata":jsonData]
    def static extensionsPath = grails.util.Holders.getConfig().webAppExtensibility.locations.extensions


    def setup() {
        def folder = new File( "${extensionsPath}/${params.application}/${params.page}.json" )
        if( !folder.exists() ) {
            folder.getParentFile().mkdirs();
            folder.createNewFile();
            folder << jsonData.toString()
        }

    }

    def cleanup(){
        def file = new File( "${extensionsPath}/${params.application}/${params.page}.json" )
        if(file.exists()){
            file.delete()
        }
    }

    void "test for list"() {
        when:
        def result = extensionService.list(params)
        then:
        result.size()>0
    }


    void "test for count"() {
        when:
        def count = extensionService.count(params)
        then:
        count == 1
    }

    void "test for save"(){
        given:
        JSONObject row = new JSONObject()
        Map data = new HashMap()
        data.put("name","TestPage")
        row.put("sections",data)
        Map content = ["TestData":"Test Data output"]
        params << ["metadata":JSON.parse("{'sections':[{'name':'extzToolList','exclude':false}]}")]
        //params << ["metadata":jsonSlurper.parseText(jsonData)]
        when:
        def res = extensionService.create(params,content)
        then:
        noExceptionThrown()
        when:
        params <<["application":null]
        extensionService.create(params,content)
        then:
        thrown Exception

    }

    void "loadExtensionsJSON" (){
        given:
        def requestURI = "V${params.application}/${params.page}.json"
        when:
        ExtensionService.loadExtensionsJSON(requestURI)
        then:
        noExceptionThrown()
    }

}

/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.extensibility.metadata

import grails.converters.JSON
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.grails.web.json.JSONObject
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
class ResourceServiceSpec extends Specification{
    def resourceService

    //def resourceService = new net.hedtech.extensibility.metadata.ResourceService()

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

    void "test for save"(){
        given:
        resourceService = new ResourceService()
        JSONObject row = new JSONObject()
        Map data = new HashMap()
        data.put("name","TestPage")
        row.put("sections",data)
        //Map content = ["TestData":"Test Data output"]
        params << ["metadata":JSON.parse("{'sections':[{'name':'extzToolList','exclude':false," +
                "'data': [{'ID':1, 'page':'TestPage'},{'ID':2, 'page':'TestPage 2'}]}]" +
                ",'id':1, 'secondData': {'DataID':1, 'DataName':'TEST'}}")]
        def res
        when:
        res = resourceService.create(params,jsonData)
        then:
        noExceptionThrown()
        when:
        def output = ResourceService.mergeJSONResult(res, jsonData)
        then:
        output!=null
        when:
        params <<["application":null]
        resourceService.create(params,content)
        then:
        thrown Exception

    }

    void "loadExtensionsJSON" (){
        given:
        def requestURI = "V${params.application}/${params.page}.json"
        when:
        ResourceService.loadResourcesJSON(requestURI)
        then:
        noExceptionThrown()
    }
}
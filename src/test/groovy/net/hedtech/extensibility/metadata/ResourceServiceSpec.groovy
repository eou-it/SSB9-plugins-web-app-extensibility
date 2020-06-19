/*******************************************************************************
 Copyright 2018-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.extensibility.metadata

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.testing.services.ServiceUnitTest
import grails.testing.web.GrailsWebUnitTest
import org.grails.web.json.JSONObject
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletWebRequest
import spock.lang.Specification


class ResourceServiceSpec extends Specification implements ServiceUnitTest<ResourceService>, GrailsWebUnitTest{
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


    def paramstest = [unitTest: true, dummy: 1, application:"banner_extensibility", page:"Test",
                  "metadata":jsonData]

    def  static extensionsPath

    def setup() {
        def loc=grails.util.Holders.getConfig().webAppExtensibility?.locations?.extensions
        extensionsPath = loc?loc:System.getProperty("java.io.tmpdir")
        def folder = new File( "${extensionsPath}/${paramstest.application}/${paramstest.page}.json" )
        if( !folder.exists() ) {
            folder.getParentFile().mkdirs();
            folder.createNewFile();
            folder << jsonData.toString()
        }

    }

    def cleanup(){
        def file = new File( "${extensionsPath}/${paramstest.application}/${paramstest.page}.json" )
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
        paramstest << ["metadata":JSON.parse("{'sections':[{'name':'extzToolList','exclude':false," +
                "'data': [{'ID':1, 'page':'TestPage'},{'ID':2, 'page':'TestPage 2'}]}]" +
                ",'id':1, 'secondData': {'DataID':1, 'DataName':'TEST'}}")]
        def res
        when:
        res = resourceService.create(paramstest,jsonData)
        then:
        noExceptionThrown()
        when:
        def output = ResourceService.mergeJSONResult(res, jsonData)
        then:
        output!=null
        when:
        paramstest <<["application":null]
        resourceService.create(paramstest,content)
        then:
        thrown Exception

    }

    void "loadExtensionsJSON" (){
        RequestAttributes mockRequest = new ServletWebRequest(new MockHttpServletRequest("GET", "/test"))
        RequestContextHolder.setRequestAttributes(mockRequest)
        given:
        def requestURI = "V${paramstest.application}/${paramstest.page}.json"
        when:
        ResourceService.loadResourcesJSON(requestURI)
        then:
        noExceptionThrown()
    }
}

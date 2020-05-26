/*******************************************************************************
 Copyright 2015-2020 Ellucian Company L.P. and its affiliates.
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
import org.springframework.web.context.request.ServletWebRequest
import spock.lang.Specification
import org.springframework.web.context.request.RequestContextHolder


class ExtensionServiceSpec extends Specification implements ServiceUnitTest<ExtensionService>, GrailsWebUnitTest {

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

    def paramstest = [unitTest: true, dummy: 1, application:"banner_extensibility", page:"Test",
                  "metadata":jsonData]

    def static extensionsPath


    def setup() {
        def loc=grails.util.Holders.getConfig().webAppExtensibility?.locations?.extensions
        extensionsPath = loc?loc:System.getProperty("java.io.tmpdir")
        extensionService.extensionsPath=extensionsPath
        def folder = new File( "${extensionsPath}/${params.application}/${params.page}.json" )
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

    void "test for list"() {
        when:
        def result = extensionService.list(paramstest)
        then:
        result.size()>0
    }


    void "test for count"() {
        when:
        def count = extensionService.count(paramstest)
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
        paramstest << ["metadata":JSON.parse("{'sections':[{'name':'extzToolList','exclude':false}]}")]
        //params << ["metadata":jsonSlurper.parseText(jsonData)]
        when:
        def res = extensionService.create(paramstest,content)
        then:
        noExceptionThrown()
        when:
        paramstest <<["application":null]
        extensionService.create(paramstest,content)
        then:
        thrown Exception

    }

    void "loadExtensionsJSON" (){
        given:
        RequestAttributes mockRequest = new ServletWebRequest(new MockHttpServletRequest("GET", "/test"))
        RequestContextHolder.setRequestAttributes(mockRequest)
        def requestURI = "V${paramstest.application}/${paramstest.page}.json"
        when:
        ExtensionService.loadExtensionsJSON(requestURI)
        then:
        noExceptionThrown()
    }

}

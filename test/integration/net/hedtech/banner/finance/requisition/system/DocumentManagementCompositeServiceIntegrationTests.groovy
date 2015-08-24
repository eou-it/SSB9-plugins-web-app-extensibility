/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.apache.commons.io.IOUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

import javax.xml.ws.WebServiceException

class DocumentManagementCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def documentManagementCompositeService

    /**
     * Super class setup
     */
    @Before
    void setUp() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
    }


    @Test
    void dummyTest() {
        assertTrue( true )
    }


    @Test
    void testUploadDocument() {
        try {
            Integer pidm = 2510
            MockMultipartFile multipartFile = formFileObject()
            def dataMap = documentManagementCompositeService.uploadDocument( multipartFile, 'RSED0001', "REQUISITION", pidm, null, true )
            assertTrue( dataMap.size() > 0 )
        } catch (WebServiceException e) {
            assertNotNull( e.getMessage() )
        }
    }


    @Test
    void testUploadDocumentWithOutBDM() {
        Integer pidm = 2510
        MockMultipartFile multipartFile = formFileObject()
        try {
            documentManagementCompositeService.uploadDocument( multipartFile, 'RSED0001', "REQUISITION", pidm, null, false )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED )
        }
    }


    @Test
    void testDeleteDocumentsByRequisitionCode() {
        Integer pidm = 2510
        MockMultipartFile multipartFile = formFileObject()
        def dataMap
        try {
            dataMap = documentManagementCompositeService.uploadDocument( multipartFile, 'RSED0003', "REQUISITION", pidm, null, true )
            assertTrue( dataMap.size() > 0 )
            dataMap = documentManagementCompositeService.deleteDocumentsByRequisitionCode( dataMap[0].DOCID, null, true, 'RSED0003' )
            assertTrue( dataMap.size() > 0 )
        } catch (WebServiceException e) {
            assertNotNull( e.getMessage() )
        }
    }


    @Test
    void testDeleteDocumentsByRequisitionCodeWithOutBDM() {
        Integer pidm = 2510
        MockMultipartFile multipartFile = formFileObject()
        def dataMap
        try {
            dataMap = documentManagementCompositeService.uploadDocument( multipartFile, 'RSED0003', "REQUISITION", pidm, null, true )
            assertTrue( dataMap.size() > 0 )
            documentManagementCompositeService.deleteDocumentsByRequisitionCode( dataMap[0].DOCID, null, false, 'RSED0003' )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED )
        } catch (WebServiceException e) {
            assertNotNull( e.getMessage() )
        }
    }


    @Test
    void testListDocumentsByRequisitionCode() {
        Integer pidm = 2510
        MockMultipartFile multipartFile = formFileObject()
        def dataMap
        try {
            dataMap = documentManagementCompositeService.uploadDocument( multipartFile, 'RSED0003', "CHECK", pidm, null, true )
            assertTrue( dataMap.size() > 0 )
            dataMap = documentManagementCompositeService.listDocumentsByRequisitionCode( 'RSED0003', null, true )
            assertTrue( dataMap.size() > 0 )
        } catch (WebServiceException e) {
            assertNotNull( e.getMessage() )
        }
    }


    @Test
    void testListDocumentsByRequisitionCodeWithOutBDM() {
        try {
            documentManagementCompositeService.listDocumentsByRequisitionCode( 'RSED0003', null, false )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR )
        }
    }


    private MockMultipartFile formFileObject() {
        File testFile
        try {
            String data = " Test data for integration testing"
            String tempPath = ConfigurationHolder.config.bdm.file.location
            testFile = new File( tempPath, "BDMTestFile.txt" )
            if (!testFile.exists()) {
                testFile.createNewFile()
                FileWriter fileWritter = new FileWriter( testFile, true )
                BufferedWriter bufferWritter = new BufferedWriter( fileWritter )
                bufferWritter.write( data )
                bufferWritter.close()
            }
        } catch (IOException e) {
            throw e
        }
        FileInputStream input = new FileInputStream( testFile );
        MultipartFile multipartFile = new MockMultipartFile( "file",
                                                             testFile.getName(), "text/plain", IOUtils.toByteArray( input ) )
        multipartFile
    }
}

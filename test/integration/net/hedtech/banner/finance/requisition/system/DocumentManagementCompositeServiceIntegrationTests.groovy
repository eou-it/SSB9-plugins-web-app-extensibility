/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.apache.commons.io.IOUtils
import grails.util.Holders
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

import javax.xml.ws.WebServiceException

class DocumentManagementCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def documentManagementCompositeService

    def bdmEnabled = Holders?.config.bdm.enabled
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

    /**
     * Tests Upload Document Without BDM
     */
    @Test
    void testUploadDocumentWithOutBDM() {
        Integer pidm = 2510
        MockMultipartFile multipartFile = formFileObject()
        try {
            documentManagementCompositeService.uploadDocument( multipartFile, 'RSED0005', "CHECK", pidm, null, false )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED )
        }
    }

    /**
     * Tests Upload Document
     */
    @Test
    void testUploadDocument() {
        if(bdmEnabled) {
            try {
                Integer pidm = 2510
                MockMultipartFile multipartFile = formFileObject()
                def dataMap = documentManagementCompositeService.uploadDocument(multipartFile, 'RSED0005', "REQUISITION", pidm, null, true)
                assertTrue(dataMap.size() > 0)
            } catch (ApplicationException e) {
                assertNotNull(e.getMessage())
            }
        }
    }

    /**
     * Tests Upload Document with Empty content
     */
    @Test
    void testUploadDocumentEmptyContent() {
        if(bdmEnabled) {
            try {
                Integer pidm = 2510
                MockMultipartFile multipartFile = formEmptyFileObject()
                documentManagementCompositeService.uploadDocument(multipartFile, 'RSED0005', "REQUISITION", pidm, null, true)
            } catch (ApplicationException e) {
                assertNotNull(e.getMessage())
            }
        }
    }

    /**
     * Tests Delete document requisition by code Second case
     */
    @Test
    void testDeleteDocumentsByRequisitionCodeSecondCase() {
        Integer pidm = 2510
        def dataMap
        if(bdmEnabled) {
            try {
                dataMap = documentManagementCompositeService.listDocumentsByRequisitionCode('RSED0005', null, true)
                assertTrue(dataMap.size() > 0)
                dataMap = documentManagementCompositeService.deleteDocumentsByRequisitionCode(dataMap.documentList[0]?.docAttributes?.DOCID, null, true, 'RSED0004')
                assertTrue(dataMap.size() > 0)
            } catch (ApplicationException ae) {
                assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR)
            } catch (WebServiceException e) {
                assertNotNull(e.getMessage())
            }
        }
    }

    /**
     * Tests Delete document requisition by code Without BDM
     */
    @Test
    void testDeleteDocumentsByRequisitionCodeWithOutBDM() {
        try {
            documentManagementCompositeService.deleteDocumentsByRequisitionCode( '', null, false, 'RSED0004' )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED )
        } catch (WebServiceException e) {
            assertNotNull( e.getMessage() )
        }
    }

    /**
     * Tests Delete document requisition by code Without BDM
     */
    @Test
    void testDeleteDocumentsByRequisitionCodeWithWrongDocID() {
        if(bdmEnabled) {
            try {
                documentManagementCompositeService.deleteDocumentsByRequisitionCode('test', null, true, 'RSED0004')
            } catch (ApplicationException ae) {
                assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR)
            } catch (WebServiceException e) {
                assertNotNull(e.getMessage())
            }
        }
    }

    /**
     * Tests Delete document requisition By code
     */
    @Test
    void testDeleteDocumentsByRequisitionCode() {
        Integer pidm = 2510
        def dataMap
        if(bdmEnabled) {
            try {
                dataMap = documentManagementCompositeService.listDocumentsByRequisitionCode('RSED0005', null, true)
                assertTrue(dataMap.size() > 0)
                dataMap = documentManagementCompositeService.deleteDocumentsByRequisitionCode(dataMap.documentList[0]?.docAttributes?.DOCID, null, true, 'RSED0003')
                assertTrue(dataMap.size() > 0)
            } catch (ApplicationException ae) {
                assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR)
            } catch (WebServiceException e) {
                assertNotNull(e.getMessage())
            }
        }
    }

    /**
     * Tests List documents requisition by code Without BDM
     */
    @Test
    void testListDocumentsByRequisitionCodeWithOutBDM() {
        if(bdmEnabled) {
            try {
                documentManagementCompositeService.listDocumentsByRequisitionCode('RSED0006', null, false)
            } catch (ApplicationException ae) {
                assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED)
            }
        }
    }

    /**
     * Tests List documents requisition by code Without BDM
     */
    @Test
    void testListDocumentsByRequisitionCodeWithInvalidReqId() {
        if(bdmEnabled) {
            try {
                documentManagementCompositeService.listDocumentsByRequisitionCode('test', 'test', true)
            } catch (ApplicationException ae) {
                assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR)
            }
        }
    }

    /**
     * Tests List documents requisition by code
     */
    @Test
    void testListDocumentsByRequisitionCode() {
        Integer pidm = 2510
        MockMultipartFile multipartFile = formFileObject()
        def dataMap
        if(bdmEnabled) {
            try {
                dataMap = documentManagementCompositeService.uploadDocument(multipartFile, 'RSED0004', "CHECK", pidm, null, true)
                assertTrue(dataMap.size() > 0)
                dataMap = documentManagementCompositeService.listDocumentsByRequisitionCode('RSED0004', null, true)
                assertTrue(dataMap.size() > 0)
                documentManagementCompositeService.deleteDocumentsByRequisitionCode(dataMap.documentList[0]?.docAttributes?.DOCID, null, true, 'RSED0006')
            } catch (ApplicationException ae) {
                assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR)
            } catch (WebServiceException e) {
                assertNotNull(e.getMessage())
            }
        }
    }

    /**
     * Form file Object
     */
    private MockMultipartFile formFileObject() {
        File testFile
        try {
            String data = " Test data for integration testing"
            String tempPath = Holders?.config.bdm.file.location
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

    /**
     * Form empty file Object
     * @return
     */
    private MockMultipartFile formEmptyFileObject() {
        File testFile
        try {
            String tempPath = Holders?.config.bdm.file.location
            testFile = new File( tempPath, "BDMTestFileEmpty.txt" )
            if (!testFile.exists()) {
                testFile.createNewFile()
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

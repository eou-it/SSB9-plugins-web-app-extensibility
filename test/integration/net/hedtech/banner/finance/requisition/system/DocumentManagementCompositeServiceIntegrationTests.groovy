/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.apache.commons.io.IOUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

class DocumentManagementCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def documentManagementCompositeService
    def requisitionHeaderService

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


   /* @Test
    void testUploadDocument() {
        File testFile
        try {
            String data = " Test data for integration testing"
            String tempPath = ConfigurationHolder.config.bdm.file.location
            testFile = new File( tempPath, "integrationTest.txt" )
            if (!testFile.exists()) {
                testFile.createNewFile()
            }
            FileWriter fileWritter = new FileWriter( testFile.getName(), true )
            BufferedWriter bufferWritter = new BufferedWriter( fileWritter )
            bufferWritter.write( data )
            bufferWritter.close();
        } catch (IOException e) {
            throw e
        }
        FileInputStream input = new FileInputStream( testFile );
        MultipartFile multipartFile = new MockMultipartFile( "file",
                                                             testFile.getName(), "text/plain", IOUtils.toByteArray( input ) )
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( 'RSED0001' )
        def dataMap = documentManagementCompositeService.uploadDocument( multipartFile, header.requestCode, "REQUISITION", 2510, null, true )
        println dataMap
        assertNotNull( dataMap )
    }*/
}

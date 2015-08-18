/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.junit.After
import org.junit.Before
import org.junit.Test

class DocumentManagementCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def documentManagementCompositeService
    def grailsApplication

    /**
     * Super class setup
     */
    @Before
    void setUp() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
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
             String data = " Test data for integration testing";
             String filePath = 'test/util/BDMIntegrationTestData.txt'
             testFile = new File(filePath );
             if (!testFile.exists()) {
                 testFile.createNewFile();
             }
             FileWriter fileWritter = new FileWriter( testFile.getName(), true );
             BufferedWriter bufferWritter = new BufferedWriter( fileWritter );
             bufferWritter.write( data );
             bufferWritter.close();
         } catch (IOException e) {
             //assertApplicationException e, (e.getMessage())
             throw e
         }
         def dataMap = documentManagementCompositeService.uploadDocument( testFile,"RSD00001","REQUISITION",2510,null,true )
         println dataMap
         assertNotNull(dataMap)

     }*/
}

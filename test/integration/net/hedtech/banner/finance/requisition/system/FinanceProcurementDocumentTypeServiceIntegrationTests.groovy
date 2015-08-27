/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class FinanceProcurementDocumentTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def financeProcurementDocumentTypeService

    /**
     * Setup
     */
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * tear down
     */
    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Tests Fetch Document Types By code
     */
    @Test
    void testFetchDocumentTypesByCode() {
        def documents = financeProcurementDocumentTypeService.getCommonMatchingDocs( "I", 10, 0 )
        assertNotNull( documents )
    }

    /**
     * Tests Fetch Document Types By Desc
     */
    @Test
    void testFetchDocumentTypesByDesc() {
        def documents = financeProcurementDocumentTypeService.getCommonMatchingDocs( "I", 10, 0 )
        assertNotNull( documents )
    }

    /**
     * Tests Fetch Document Types By Desc SQLException
     */
    @Test
    void testFetchDocumentTypesByDescSQLException() {
        try {
            financeProcurementDocumentTypeService.getCommonMatchingDocs( "I", 10, 0, 'INVALID :docTypeCode AND :docTypeDesc' )
        }
        catch (ApplicationException ae) {
            assertNotNull( ae.getMessage() )
        }
    }
}

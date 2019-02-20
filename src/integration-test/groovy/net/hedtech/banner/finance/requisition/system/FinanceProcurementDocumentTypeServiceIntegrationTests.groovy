/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.util.Holders
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class FinanceProcurementDocumentTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def financeProcurementDocumentTypeService

    def bdmEnabled = Holders?.config.bdm.enabled

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
        if(bdmEnabled) {
            def documents = financeProcurementDocumentTypeService.getCommonMatchingDocs("I", 10, 0)
            assertNotNull(documents)
        }
    }

    /**
     * Tests Fetch Document Types By Desc
     */
    @Test
    void testFetchDocumentTypesByDesc() {
        if(bdmEnabled) {
            def documents = financeProcurementDocumentTypeService.getCommonMatchingDocs("I", 10, 0)
            assertNotNull(documents)
        }
    }

    /**
     * Tests Fetch Document Types By Desc SQLException
     */
    @Test
    void testFetchDocumentTypesByDescSQLException() {
        if(bdmEnabled) {
            try {
                financeProcurementDocumentTypeService.getCommonMatchingDocs("I", 10, 0, 'INVALID :docTypeCode AND :docTypeDesc')
            }
            catch (ApplicationException ae) {
                assertNotNull(ae.getMessage())
            }
        }
    }
}

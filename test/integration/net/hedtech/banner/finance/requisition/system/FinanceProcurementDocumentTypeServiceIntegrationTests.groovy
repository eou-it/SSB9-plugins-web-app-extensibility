/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class FinanceProcurementDocumentTypeServiceIntegrationTests extends BaseIntegrationTestCase{

    def financeProcurementDocumentTypeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testFetchDocumentTypesByCode() {
        def documents =  financeProcurementDocumentTypeService.getCommonMatchingDocs ("I", 10, 0)
        assertNotNull( documents )
    }

    @Test
    void testFetchDocumentTypesByDesc() {
        def documents =  financeProcurementDocumentTypeService.getCommonMatchingDocs("I", 10, 0)
        assertNotNull( documents )
    }
}

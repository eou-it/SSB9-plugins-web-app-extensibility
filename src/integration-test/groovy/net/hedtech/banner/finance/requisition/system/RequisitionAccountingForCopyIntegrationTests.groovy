/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequisitionAccountingForCopyIntegrationTests extends BaseIntegrationTestCase {

    def requisitionAccountingForCopyService
    /**
     * Super class setup
     */
    @Before
    void setUp() {
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
     * Test case to test pre delete method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreDeleteFailCase() {
        def map = [:]
        requisitionAccountingForCopyService.delete( map )
    }

    /**
     * Test case to test pre update method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreUpdateFailCase() {
        def map = RequisitionAccountingForCopy.findByRequestCode( 'RSED0005' )
        map.requestCode = 'RSED0001'
        requisitionAccountingForCopyService.update( map )
    }
}

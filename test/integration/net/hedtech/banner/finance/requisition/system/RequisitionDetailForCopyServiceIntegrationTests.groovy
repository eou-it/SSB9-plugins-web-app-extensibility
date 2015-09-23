/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequisitionDetailForCopyServiceIntegrationTests extends BaseIntegrationTestCase {

    def requisitionDetailForCopyService
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
        requisitionDetailForCopyService.delete( map )
    }

    /**
     * Test case to test pre update method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreUpdateFailCase() {
        RequisitionDetailForCopy copy = new RequisitionDetailForCopy(
                requestCode: 'RSED0005',item:1

        )
        copy = requisitionDetailForCopyService.create( [domainModel: copy] )
        def map = RequisitionDetailForCopy.findByRequestCode( 'RSED0005' )
        map.requestCode = 'RSED0001'
        requisitionDetailForCopyService.update( map )
    }
}

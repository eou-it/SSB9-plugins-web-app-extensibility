/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequisitionTaxForCopyServiceIntegrationTests extends BaseIntegrationTestCase {

    def requisitionTaxForCopyService
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
        requisitionTaxForCopyService.delete( map )
    }

    /**
     * Test case to test pre update method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreUpdateFailCase() {
        RequisitionTaxForCopy copy = new RequisitionTaxForCopy(
                requestCode: 'RSED0005', item: 1, proximityNumber: 1, taxRateCode: 1, taxAmount: 1, taxableAmount: 1, payTaxTo: 'me'

        )
        copy = requisitionTaxForCopyService.create( [domainModel: copy] )
        def map = RequisitionTaxForCopy.findByRequestCode( 'RSED0005' )
        map.requestCode = 'RSED0001'
        requisitionTaxForCopyService.update( map )
    }

}

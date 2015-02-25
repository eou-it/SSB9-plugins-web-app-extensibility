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
import org.springframework.security.authentication.BadCredentialsException

/**
 * Test cases class for RequisitionDetailService.
 *
 */
class RequisitionDetailServiceIntegrationTests extends BaseIntegrationTestCase {
    def requisitionDetailService
    def reqCode = 'R0000124'
    def commodityCode = '2210000000'
    /**
     * Super class setup
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test case to test find requisition detail list by request code.
     */
    @Test
    public void testFindRequisitionDetailListByRequistionCode() {
        def pagingParams = [max: 500, offset: 0]
        def requisitionDetails = requisitionDetailService.findRequisitionDetailListByRequistionCode( reqCode, pagingParams )
        assertTrue( requisitionDetails.size() > 0 || requisitionDetails.isEmpty() )
    }

    /**
     * Test case to test find requisition detail by request code and commodity.
     */
    @Test
    public void testFindRequisitionDetailByCodeAndCommodityCode() {
        def map = [:]
        map.requisitionCode = reqCode
        map.commodityCode = commodityCode
        def requisitionDetail = requisitionDetailService.findRequisitionDetailByCodeAndCommodityCode( map )
        assertTrue( requisitionDetail == null || requisitionDetail.requestCode == reqCode )
    }

    /**
     * Test case to test find requisition detail list by user.
     */
    @Test
    public void testFindRequisitionDetailListByUser() {
        def pagingParams = [max: 500, offset: 0]
        try {
            def list = requisitionDetailService.findRequisitionDetailListByUser( pagingParams )
            assertTrue( list.size() > 0 )
        } catch (ApplicationException e) {
            assertApplicationException e, (FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL)
        }
    }

    /**
     * Test case to test find requisition detail list by sending invalid user and expecting BadCredentialsException.
     */
    @Test(expected = BadCredentialsException.class)
    public void testFindRequisitionDetailListByUserInvalidUserCase() {
        super.login 'INVALID_USER', 'INVALID_PASSWORD'
        def pagingParams = [max: 500, offset: 0]
        requisitionDetailService.findRequisitionDetailListByUser( pagingParams )
    }

    /**
     * Test to test the get the last item for request code.
     */
    @Test
    public void testGetLastItem() {
        def lastItem = requisitionDetailService.getLastItem( reqCode )
        assertTrue( lastItem == 0 || lastItem > 0 )
    }
}

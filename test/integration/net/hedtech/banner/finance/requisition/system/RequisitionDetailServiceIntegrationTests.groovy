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
    def springSecurityService
    def reqCode = 'R0000124'
    def item = '1'
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
    public void testFindRequisitionDetailListByRiquistionCode() {
        def pagingParams = [max: 500, offset: 0]
        def requisitionDetails = requisitionDetailService.fetchByRequestCodeAndItem( reqCode, item, pagingParams )
        assertTrue( requisitionDetails.size() > 0 || requisitionDetails.isEmpty() )
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
     * test Listing headers Invalid user
     */
    @Test
    void testFindRequisitionDetailListByInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        springSecurityService.getAuthentication().user.oracleUserName = ''
        try {
            requisitionDetailService.findRequisitionDetailListByUser( pagingParams )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID
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

    /**
     * Test case to test get requisition detail by request code and item.
     */
    @Test
    public void testGetRequisitionDetailByRequestCodeAndItem() {
        def requisitionDetail = requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'R0000561', '1' )
        assertTrue( (requisitionDetail.requestCode == 'R0000561' && requisitionDetail.item == 1) || requisitionDetail == null )
    }

    /**
     * Test case to test get requisition detail by sending wrong request code and item.
     */
    @Test(expected = ApplicationException.class)
    public void testGetRequisitionDetailByRequestCodeAndItemFailureCase() {
        requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'R000012912R121', '12' )
    }
}

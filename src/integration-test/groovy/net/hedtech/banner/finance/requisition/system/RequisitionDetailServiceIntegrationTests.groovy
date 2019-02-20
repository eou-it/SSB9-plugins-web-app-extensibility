/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.BadCredentialsException
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
/**
 * Test cases class for RequisitionDetailService.
 *
 */

@Integration
@Rollback
class RequisitionDetailServiceIntegrationTests extends BaseIntegrationTestCase {
    def requisitionDetailService
    def springSecurityService
    def reqCode = 'RSED0003'
    def item = 1
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
        logout()
    }

    /**
     * Test case to test find requisition detail list by request code.
     */
    @Test
    public void testFindRequisitionDetailListByRiquistionCode() {
        def requisitionDetails = requisitionDetailService.findByRequestCodeAndItem( reqCode, item )
        assertTrue( requisitionDetails != null )
    }

    /**
     * Test case to test find requisition detail list by request code. Empty items
     */
    @Test
    public void testFetchByRequestCodeAndItemWithEmptyItem() {
        try {
            requisitionDetailService.findByRequestCodeAndItem( reqCode, 0 )
        } catch (ApplicationException e) {
            assertApplicationException e, (FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL)
        }
    }

    /**
     * Test case to test find requisition detail list by user.
     */
    @Test
    public void testFindRequisitionDetailListByUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = 'GRAILS'
        try {
            def list = requisitionDetailService.fetchRequisitionDetailListByUser( pagingParams )
            assertTrue( list.size() > 0 )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test case to test find requisition detail list by sending invalid user and expecting BadCredentialsException.
     */
    @Test(expected = BadCredentialsException.class)
    public void testFindRequisitionDetailListByUserInvalidUserCase() {
        super.login 'INVALID_USER', 'INVALID_PASSWORD'
        def pagingParams = [max: 500, offset: 0]
        requisitionDetailService.fetchRequisitionDetailListByUser( pagingParams )
    }

    /**
     * Test case to test find requisition detail list and expecting ApplicationException.
     */
    @Test
    public void testFindRequisitionDetailList() {
        super.login 'FORSED23',
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        try {
            requisitionDetailService.fetchRequisitionDetailListByUser( pagingParams )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL )
        }
    }

    /**
     * Test case to test find requisition detail list by sending invalid user and expecting ApplicationException.
     */
    @Test
    public void testFindRequisitionDetailListByUserInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def pagingParams = [max: 500, offset: 0]
        try {
            requisitionDetailService.fetchRequisitionDetailListByUser( pagingParams )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test to test the get the last item for request code.
     */
    @Test
    public void testGetLastItem() {
        def lastItem = requisitionDetailService.getLastItem( 'RSED0003' )
        assertTrue( lastItem > 0 )
    }

    /**
     * Test to test the get the last item for request code.
     */
    @Test
    public void testGetLastItemWithInvalidRequestCode() {
        def lastItem = requisitionDetailService.getLastItem( null )
        assertTrue( lastItem == 0 )
    }

    /**
     * Test case to test get requisition detail by request code and item.
     */
    @Test
    public void testGetRequisitionDetailByRequestCodeAndItem() {
        def requisitionDetail = requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'RSED0003', 1 )
        assertTrue( (requisitionDetail.requestCode == 'RSED0003' && requisitionDetail.item == 1) || requisitionDetail == null )
    }

    /**
     * Test case to test get requisition detail by sending wrong request code and item.
     */
    @Test(expected = ApplicationException.class)
    public void testGetRequisitionDetailByRequestCodeAndItemFailureCase() {
        requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'R000012912R121', 12 )
    }

    /**
     * Test case method to create requisition detail commodity level.
     */
    @Test
    public void testCreateRequisitionDetail() {
        def requestDetail = getRequisitionDetails()
        requestDetail.save( failOnError: true, flush: true )
        requestDetail.refresh()
        def requisitionDetailCommodityLevel = requisitionDetailService.findByRequestCodeAndItem(
                requestDetail.requestCode, requestDetail.item )
        assertTrue requisitionDetailCommodityLevel.requestCode == requestDetail.requestCode
        assertTrue requisitionDetailCommodityLevel.item == requestDetail.item
    }

    /**
     * Test findByDocumentCode with valid code
     */
    @Test
    public void findByRequestCode() {
        def detail = requisitionDetailService.findByRequestCode( 'RSED0003' )
        assertTrue 'RSED0003' == detail[0].requestCode
    }

    /**
     * Test findByDocumentCode With InvalidCode
     */
    @Test
    public void findByRequestCodeWithInvalidCode() {
        try {
            requisitionDetailService.findByRequestCode( 'INVALID' )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL )
        }
    }

    /**
     * The method is used to get the RequisitionDetail object with all required values to insert/update.
     * @return RequisitionDetail.
     */
    private RequisitionDetail getRequisitionDetails() {
        def reqCode = "RSED0003"
        def commodityCode = '2210000000'
        def lastItem = RequisitionDetail.getLastItem( reqCode ).getAt( 0 )
        if (lastItem == null) {
            lastItem = 0
        }
        lastItem = lastItem.next()
        def requisitionDetail = new RequisitionDetail(
                requestCode: reqCode,
                item: lastItem,
                userId: FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                commodity: commodityCode,
                commodityDescription: 'New',
                chartOfAccount: 'B',
                organization: '11003',
                quantity: '2',
                unitOfMeasure: 'EA',
                unitPrice: 99.99,
                deliveryDate: new Date() + 30,
                ship: 'EAST',
                suspenseIndicator: false,
                textUsageIndicator: 'S',
                discountAmount: '0',
                taxAmount: '0',
                additionalChargeAmount: '0',
                taxGroup: null,
                dataOrigin: 'Banner'
        )
        return requisitionDetail
    }
}

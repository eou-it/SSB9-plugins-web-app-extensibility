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
 * Test class for Requisition Details Composite Service
 */
class RequisitionDetailsCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionDetailsCompositeService
    def requisitionDetailService
    def springSecurityService
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
     * Test create Requisition Detail
     */
    @Test
    void testCreatePurchaseRequisitionDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqDetailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        def requestCode = requisitionDetailsCompositeService.createPurchaseRequisitionDetail( domainModelMap )
        assertTrue requestCode?.requestCode == requestHeaderCode
    }

    /**
     * Test create With Invalid user
     */
    @Test(expected = BadCredentialsException.class)
    void testCreatePurchaseRequisitionDetailBadCredentials() {
        login 'Invalid_user', 'invalid_password'
        def reqDetailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        requisitionDetailsCompositeService.createPurchaseRequisitionDetail( domainModelMap )
    }

    /**
     * Test create With invalid Requisition Header Code
     */
    @Test(expected = ApplicationException.class)
    void testCreatePurchaseRequisitionInvalidRequisitionCode() {
        def reqDetailDomainModel = getRequisitionDetails()
        reqDetailDomainModel.requestCode = 'R0000129182991'
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        requisitionDetailsCompositeService.createPurchaseRequisitionDetail( domainModelMap )
    }

    /**
     * Test create Requisition Detail to test invalid user.
     */
    @Test
    void testCreatePurchaseRequisitionDetailInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def reqDetailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        try {
            requisitionDetailsCompositeService.createPurchaseRequisitionDetail( domainModelMap )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test case to test delete purchase requisition detail.
     */
    @Test
    void testDeletePurchaseRequisitionDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionDetailsCompositeService.deletePurchaseRequisitionDetail( 'R0000561', 1 )
        try {
            requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'R0000561', 1 )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
    }

    /**
     * Test update
     */
    @Test
    void updatePurchaseDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def detailDomainModel = getRequisitionDetails()
        detailDomainModel.item = 1
        def domainModelMap = [requisitionDetail: detailDomainModel]
        def detail = requisitionDetailsCompositeService.updateRequisitionDetail( domainModelMap )
        assertTrue( detail.requestCode == requestHeaderCode )
    }

    /**
     * Test update to test invalid user.
     */
    @Test
    void updatePurchaseDetailInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        //
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def detailDomainModel = getRequisitionDetails()
        detailDomainModel.item = 1
        def domainModelMap = [requisitionDetail: detailDomainModel]
        try {
            requisitionDetailsCompositeService.updateRequisitionDetail( domainModelMap )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test update  with empty list
     */
    @Test
    void updatePurchaseDetailWithEmptyList() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def detailDomainModel = getRequisitionDetails()
        detailDomainModel.item = 0
        def domainModelMap = [requisitionDetail: detailDomainModel]
        try {
            requisitionDetailsCompositeService.updateRequisitionDetail( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED
        }
    }

    /**
     * The method is used to get the RequisitionDetail object with all required values to insert/update.
     * @return RequisitionDetail.
     */
    private RequisitionDetail getRequisitionDetails() {
        def requisitionDetail = [
                'requestCode'           : requestHeaderCode,
                'userId'                : FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                'commodity'             : '2210000000',
                'commodityDescription'  : 'New Requisition Detail',
                'quantity'              : '2',
                'unitOfMeasure'         : 'EA',
                'unitPrice'             : '99.99',
                'suspenseIndicator'     : true,
                'textUsageIndicator'    : FinanceProcurementConstants.DEFAULT_FPBREQD_TEXT_USAGE,
                'discountAmount'        : '0',
                'additionalChargeAmount': '9',
                'taxGroup'              : 'NT',
                'dataOrigin'            : FinanceProcurementConstants.DEFAULT_REQUISITION_ORIGIN
        ]
        return requisitionDetail
    }
    def requestHeaderCode = "R0001228"
}

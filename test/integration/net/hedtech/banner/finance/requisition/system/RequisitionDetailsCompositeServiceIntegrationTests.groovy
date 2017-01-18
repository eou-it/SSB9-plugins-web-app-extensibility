/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
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
        logout()
    }

    /**
     * Test create Requisition Detail
     */
    @Test
    void testCreatePurchaseRequisitionDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqDetailDomainModel = getRequisitionDetails()
        reqDetailDomainModel.privateComment = 'Testing Private comment.'
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        def requestCode = requisitionDetailsCompositeService.createPurchaseRequisitionDetail( domainModelMap )
        assertTrue requestCode?.requestCode == requestHeaderCode
    }

    /**
     * Test create Requisition Detail Having discount code
     */
    @Test
    void testCreatePurchaseRequisitionDetailWithDiscountCode() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqDetailDomainModel = getRequisitionDetails()
        reqDetailDomainModel.requestCode = 'RSED0011'
        reqDetailDomainModel.privateComment = 'Testing Private comment.'
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        def requestCode = requisitionDetailsCompositeService.createPurchaseRequisitionDetail( domainModelMap )
        assertTrue requestCode?.requestCode == 'RSED0011'
    }

    /**
     * Test create Requisition Detail
     */
    @Test
    void testCreatePurchaseRequisitionDetailWithNoTaxProcessing() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        Session session = sessionFactory.getCurrentSession()
        session.createSQLQuery("UPDATE FOBSYSC SET FOBSYSC_TAX_PROCESSING_IND = 'N' WHERE FOBSYSC_EFF_DATE <= SYSDATE AND FOBSYSC_STATUS_IND='A' AND (FOBSYSC_TERM_DATE >= SYSDATE OR FOBSYSC_TERM_DATE IS NULL) AND (FOBSYSC_NCHG_DATE IS NULL OR FOBSYSC_NCHG_DATE > SYSDATE )").executeUpdate()
            def reqDetailDomainModel = getRequisitionDetails()
            reqDetailDomainModel.publicComment = 'Testing Public comment.'
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
        reqDetailDomainModel.publicComment = 'Testing Public comment.'
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
        reqDetailDomainModel.privateComment = 'Testing Private comment.'
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
        reqDetailDomainModel.privateComment = 'Testing Private comment.'
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
        requisitionDetailsCompositeService.deletePurchaseRequisitionDetail( 'RSED0003', 1 )
        try {
            requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'RSED0003', 1 )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
    }

    /**
     * Test case to test delete purchase requisition detail for commodity level accounting.
     */
    @Test
    void testDeletePurchaseRequisitionDetailForCommodityLvlAcc() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionDetailsCompositeService.deletePurchaseRequisitionDetail( 'RSED0004', 1 )
        try {
            requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'RSED0004', 1 )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
    }

    /**
     * Test case to test delete purchase requisition detail for commodity level accounting.
     */
    @Test
    void testDeletePurchaseRequisitionDetailForCommodityLvlAccHavingAccount() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionDetailsCompositeService.deletePurchaseRequisitionDetail( 'RSED0013', 1 )
        try {
            requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'RSED0013', 1 )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
    }

    /**
     * Test case to test delete purchase requisition detail for commodity level accounting.
     */
    @Test
    void testDeletePurchaseRequisitionDetailHavingOneAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionDetailsCompositeService.deletePurchaseRequisitionDetail( 'RSED0011', 1 )
        try {
            requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'RSED0011', 1 )
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
        detailDomainModel.privateComment = 'Testing Private comment.'
        def domainModelMap = [requisitionDetail: detailDomainModel]
        def detail = requisitionDetailsCompositeService.updateRequisitionDetail( domainModelMap )
        assertTrue( detail.requestCode == requestHeaderCode )
    }

    /**
     * Test update
     */
    @Test
    void updatePurchaseDetailWithDiscountCode() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def detailDomainModel = getRequisitionDetails()
        detailDomainModel.item = 1
        detailDomainModel.requestCode = 'RSED0011'
        detailDomainModel.privateComment = 'Testing Private comment.'
        def domainModelMap = [requisitionDetail: detailDomainModel]
        def detail = requisitionDetailsCompositeService.updateRequisitionDetail( domainModelMap )
        assertTrue detail.requestCode == 'RSED0011'
    }

    /**
     * findByDocumentCode
     */
    @Test
    void findByRequestCode() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assert requisitionDetailsCompositeService.findByRequestCode( 'RSED0003' ).commodityDescription != null
    }

    /**
     * Test findByDocumentCode InvalidCode
     */
    @Test
    void findByRequestCodeInvalidCode() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        try {
            requisitionDetailsCompositeService.findByRequestCode( 'INVALID' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
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
        detailDomainModel.publicComment = 'Testing Public comment.'
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
        detailDomainModel.privateComment = 'Testing Private comment.'
        def domainModelMap = [requisitionDetail: detailDomainModel]
        try {
            requisitionDetailsCompositeService.updateRequisitionDetail( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED
        }
    }

    /**
     * The test case is to test find requisition by code with all required information.
     */
    @Test
    void testFindByRequestCodeAndItemWithOnlyCommodityDescription() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqDetailInfo = requisitionDetailsCompositeService.findByRequestCodeAndItem( 'RSED0011', 1 )
        assertTrue( reqDetailInfo.requisitionDetail.requestCode == 'RSED0011' )
    }

    /**
     * The test case is to test find requisition by code with all required information.
     */
    @Test
    void testFindByRequestCodeAndItem() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqDetailInfo = requisitionDetailsCompositeService.findByRequestCodeAndItem( 'RSED0004', 1 )
        assertTrue( reqDetailInfo.requisitionDetail.requestCode == 'RSED0004' )
    }

    /**
     * The test case to list commodity with accounting.
     */
    @Test
    void testListCommodityWithAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def listCommoityWithAccounting = requisitionDetailsCompositeService.listCommodityWithAccounting( 'RSED0004' )
        assertTrue( listCommoityWithAccounting.size() > 0 )
    }

    /**
     * The test case to list commodity with accounting.
     */
    @Test
    void testListCommodityWithAccountingWithCommodityAndAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def listCommoityWithAccounting = requisitionDetailsCompositeService.listCommodityWithAccounting( 'RSED0013' )
        assertTrue( listCommoityWithAccounting.size() > 0 )
    }

    /**
     * The test case to list commodity with document level accounting.
     */
    @Test
    void testListCommodityWithDocumentLevelAccounting() {
        def date = new Date()
        def list = requisitionDetailsCompositeService.listCommodityWithDocumentLevelAccounting( 'RSED0004', date )
        assertTrue( list.size() > 0 )
    }

    /**
     * The test case to list commodity with document level accounting with No Commoditity.
     */
    @Test
    void testListCommodityWithDocumentLevelAccountingNoDetails() {
        def date = new Date()
        def list = requisitionDetailsCompositeService.listCommodityWithDocumentLevelAccounting( 'RSED0001', date )
        assertTrue( list.size() > 0 )
    }


    @Test
    void testListCommodityWithCommodityLevelAccounting() {
        def listCommodityWithAccounting = requisitionDetailsCompositeService.listCommodityWithAccounting( 'RSED0003' )
        assertTrue( listCommodityWithAccounting.size() > 0 )
    }

    /**
     * Test listing with No commodity
     */
    @Test
    void testListCommodityWithNoCommodity() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def listCommodityWithAccounting = requisitionDetailsCompositeService.listCommodityWithAccounting( 'RSED0014' )
        assertTrue( listCommodityWithAccounting.size() > 0 )
    }

    /**
     * The method is used to get the RequisitionDetail object with all required values to insert/update.
     * @return RequisitionDetail.
     */
    private def getRequisitionDetails() {
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
    def requestHeaderCode = "RSED0003"
}

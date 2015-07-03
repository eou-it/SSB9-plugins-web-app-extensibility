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

/**
 * Test class for RequisitionListingCompositeService
 */
class RequisitionListingCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionListingCompositeService
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
     * Test list requisitions With Draft Buckets
     */
    @Test
    void listRequisitionsByDraftBucket() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def list = requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT], pagingParams, 'USD')
        assertTrue list[0].count > 0
    }

    /**
     * Test list requisitions With Draft Buckets with no user.
     */
    @Test
    void listRequisitionsByDraftBucketWithNoUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        try {
            def pagingParams = [max: 500, offset: 0]
            requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT], pagingParams, 'USD')
        } catch (ApplicationException ae) {
            assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID)
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test list requisitions With All Buckets
     */
    @Test
    void listRequisitionsByAllBuckets() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def list = requisitionListingCompositeService.listRequisitionsByBucket([], pagingParams, 'USD')
        assertTrue list[0].count > 0
    }

    /**
     * Test list requisitions With All Buckets with no user.
     */
    @Test
    void listRequisitionsByAllBucketsWitNoUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        try {
            def pagingParams = [max: 500, offset: 0]
            requisitionListingCompositeService.listRequisitionsByBucket([], pagingParams, 'USD')
        } catch (ApplicationException ae) {
            assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID)
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test list requisitions With Invalid User
     */
    @Test
    void listRequisitionsByALlBucketInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = null
        try {
            def pagingParams = [max: 500, offset: 0]
            requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE], pagingParams, 'USD')
        } catch (ApplicationException ae) {
            assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID)
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test list requisitions With Pending Buckets
     */
    @Test
    void listRequisitionsByPendingBucket() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def list = requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING], pagingParams, 'USD')
        assertTrue list[0].count != null
    }

    /**
     * Test list requisitions With Pending Buckets with no user.
     */
    @Test
    void listRequisitionsByPendingBucketWithNoUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = null
        try {
            def pagingParams = [max: 500, offset: 0]
            requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_PENDING], pagingParams, 'USD')
        } catch (ApplicationException ae) {
            assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID)
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test list requisitions With Complete Buckets
     */
    @Test
    void listRequisitionsByCompleteBucket() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def list = requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE], pagingParams, 'USD')
        assertTrue list[0].count != null
    }

    /**
     * Test list requisitions With Complete Buckets with no user.
     */
    @Test
    void listRequisitionsByCompleteBucketWithNoUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = null
        try {
            def pagingParams = [max: 500, offset: 0]
            requisitionListingCompositeService.listRequisitionsByBucket([FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE], pagingParams, 'USD')
        } catch (ApplicationException ae) {
            assertApplicationException(ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID)
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test list requisitions by invalid bucket.
     */
    @Test
    void listRequisitionsByInvalidBucket() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        try {
            requisitionListingCompositeService.listRequisitionsByBucket(['INVALID'], pagingParams, 'USD')
        } catch (ApplicationException e) {
            assertApplicationException(e, FinanceProcurementConstants.ERROR_MESSAGE_INVALID_BUCKET_TYPE)
        }
    }
}

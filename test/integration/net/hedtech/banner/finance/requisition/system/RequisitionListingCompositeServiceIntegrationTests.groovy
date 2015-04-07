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
        def list = requisitionListingCompositeService.listRequisitionsByBucket( [FinanceProcurementConstants.REQUISITION_LIST_BUCKET_DRAFT], pagingParams )
        assertTrue list[0].count > 0
    }

    /**
     * Test list requisitions With All Buckets
     */
    @Test
    void listRequisitionsByAllBuckets() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def pagingParams = [max: 500, offset: 0]
        def list = requisitionListingCompositeService.listRequisitionsByBucket( [], pagingParams )
        assertTrue list[0].count > 0
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
            requisitionListingCompositeService.listRequisitionsByBucket( [FinanceProcurementConstants.REQUISITION_LIST_BUCKET_COMPLETE], pagingParams )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }

    }
}

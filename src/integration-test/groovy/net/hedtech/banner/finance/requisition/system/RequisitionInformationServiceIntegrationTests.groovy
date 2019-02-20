/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
/**
 * Test class for RequisitionInformationService
 */

@Integration
@Rollback
class RequisitionInformationServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionInformationService
    def springSecurityService

    /**
     * Super class setup
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
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
     * test fetch count
     */
    @Test
    void fetchRequisitionsCountByStatus() {
        def count = requisitionInformationService.fetchRequisitionsCountByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], null )
        assertTrue count.get( FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT ) > 0
    }

    /**
     * test fetch count with oracle User
     */
    @Test
    void fetchRequisitionsCountByStatusWithOraleUser() {
        def count = requisitionInformationService.fetchRequisitionsCountByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT],
                                                                                  FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME )
        assertTrue count.get( FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT ) > 0
    }
    /**
     * test fetch count with invalid user
     */
    @Test
    void fetchRequisitionsCountByStatusWithInvalidUser() {
        def count = requisitionInformationService.fetchRequisitionsCountByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], 'Invalid' )
        assertTrue count.size() == 0
    }

    /**
     * Test case to test invalid user.
     */
    @Test
    void fetchRequisitionsCountByStatusBySendingInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        //
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        try {
            requisitionInformationService.fetchRequisitionsCountByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], null )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * test fetch requisitions
     */
    @Test
    void listRequisitionsByStatus() {
        def pagingParams = [max: 500, offset: 0]
        def result = requisitionInformationService.listRequisitionsByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], pagingParams, null )
        assertTrue result.count > 0
    }

    /**
     * test fetch requisitions With oracle user
     */
    @Test
    void listRequisitionsByStatusWithOracleUser() {
        def pagingParams = [max: 500, offset: 0]
        def result = requisitionInformationService.listRequisitionsByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], pagingParams,
                                                                             FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME )
        assertTrue result.count > 0
    }

    /**
     * test fetch requisitions
     */
    @Test
    void fetchRequisitionsByReqNumber() {
        assertTrue requisitionInformationService.fetchRequisitionsByReqNumber( 'RSED0001' ) != null
    }

    /**
     * test fetch requisitions with code and user
     */
    @Test
    void fetchRequisitionsByReqNumberWithUser() {
        assertTrue requisitionInformationService.fetchRequisitionsByReqNumber( 'RSED0001', FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME ) != null
    }

    /**
     * test fetch requisitions with invalid user No User Restriction
     */
    @Test
    void fetchRequisitionsByReqNumberNoUserRestriction() {
        assertTrue requisitionInformationService.fetchRequisitionsByReqNumber( 'RSED0001', null ) != null
    }

    /**
     * test fetch requisitions with invalid user
     */
    @Test
    void listRequisitionsByStatusWithInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        def result = requisitionInformationService.listRequisitionsByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], pagingParams, 'Invalid' )
        assertTrue result.count == 0
    }

    /**
     * Test case to test invalid user.
     */
    @Test
    void listRequisitionsByStatusBySendingInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        //
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        try {
            def pagingParams = [max: 500, offset: 0]
            requisitionInformationService.listRequisitionsByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], pagingParams, null )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void searchRequisitionsBySearchParamAsString() {

        def pagingParams = [max: 500, offset: 0]
        def requisitions = requisitionInformationService.searchRequisitionsBySearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT.toUpperCase(),
                pagingParams, false )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void searchRequisitionsBySearchParamAsDate() {

        def pagingParams = [max: 500, offset: 0]
        def requisitions = requisitionInformationService.searchRequisitionsBySearchParam(
                null, FinanceCommonUtility.parseDate( '06/19/2015' ),
                pagingParams, true )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void searchRequisitionsByStatusAndSearchParamString() {
        def status = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                      FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = requisitionInformationService.searchRequisitionsByStatusAndSearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'RSED0001'.toUpperCase(),
                pagingParams, status, false )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status with null user
     */
    @Test
    void searchRequisitionsByStatusAndSearchParamAsDate() {
        def status = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                      FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = requisitionInformationService.searchRequisitionsByStatusAndSearchParam(
                null, FinanceCommonUtility.parseDate( '06/19/2015' ),
                pagingParams, status, true )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }
}

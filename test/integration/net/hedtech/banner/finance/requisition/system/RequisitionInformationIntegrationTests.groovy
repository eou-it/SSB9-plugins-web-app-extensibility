/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class for RequisitionInformationIntegration
 */
class RequisitionInformationIntegrationTests extends BaseIntegrationTestCase {
    def springSecurityService


    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }


    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void listRequisitionsByStatus() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatus(
                FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams,
                [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT] )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid User
     */
    @Test
    void listRequisitionsByStatusWithInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatus( 'INVALID_USER', pagingParams,
                                                                            [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT] )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid Status
     */
    @Test
    void listRequisitionsByStatusWithInvalidStatus() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatus(
                FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams, ['draft_invalid'] )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByStatus() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatus( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                                                                                  [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT] )
        assert requisitions > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByInvalidStatus() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatus( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                                                                                  ['draft_invalid'] )
        assert requisitions == 0
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void listRequisitionsBySearchParam() {

        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsBySearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT.toUpperCase(),
                pagingParams )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid User
     */
    @Test
    void listRequisitionsBySearchParamWithInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsBySearchParam( 'INVALID_USER', FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT.toUpperCase(),
                                                                                 pagingParams )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid Status
     */
    @Test
    void listRequisitionsBySearchParamWithInvalidSearchParam() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsBySearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'invalid_draft',
                pagingParams )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountBySearchParam() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountBySearchParam( FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT.toUpperCase(), FinanceProcurementConstants.REQUISITION_INFO_USER_NAME
        )
        assert requisitions > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByInvalidSearchParam() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatus( 'draft_invalid',
                                                                                  FinanceProcurementConstants.REQUISITION_INFO_USER_NAME )
        assert requisitions == 0
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void listRequisitionsByTransactionDate() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByTransactionDate(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, FinanceCommonUtility.parseDate( '06/19/2015' ),
                pagingParams )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid User
     */
    @Test
    void listRequisitionsByTransactionDateWithInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByTransactionDate( 'INVALID_USER', FinanceCommonUtility.parseDate( '06/19/2015' ),
                                                                                     pagingParams )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid Status
     */
    @Test
    void listRequisitionsByTransactionDateWithInvalidSearchParam() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsBySearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'invalid_draft',
                pagingParams )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByTransactionDate() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountBySearchParam( FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT.toUpperCase(),
                                                                                       FinanceProcurementConstants.REQUISITION_INFO_USER_NAME
        )
        assert requisitions > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByInvalidTransactionDate() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatus( 'draft_invalid',
                                                                                  FinanceProcurementConstants.REQUISITION_INFO_USER_NAME )
        assert requisitions == 0
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void listRequisitionsByStatusAndSearchParam() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions
        requisitions = RequisitionInformation.listRequisitionsByStatusAndSearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'RSD00001'.toUpperCase(),
                pagingParams, draftStatus )
        assertNotNull requisitions
        assert requisitions.size() > 0

        def completedStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_COMPLETED,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER,
                               FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO]

        requisitions = RequisitionInformation.listRequisitionsByStatusAndSearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'RSD00005'.toUpperCase(),
                pagingParams, completedStatus )
        assertNotNull requisitions
        assert requisitions.size() > 0

        def pendingStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING]
        requisitions = RequisitionInformation.listRequisitionsByStatusAndSearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'RSD00007'.toUpperCase(),
                pagingParams, pendingStatus )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid User
     */
    @Test
    void listRequisitionsByStatusAndSearchParamWithInvalidUser() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatusAndSearchParam( 'INVALID_USER', FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT.toUpperCase(),
                                                                                          pagingParams, draftStatus )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid Status
     */
    @Test
    void listRequisitionsByStatusAndSearchParamWithInvalidSearchParam() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatusAndSearchParam(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, 'invalid_draft',
                pagingParams, draftStatus )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByStatusAndSearchParam() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatusAndSearchParam( 'RSD00001'.toUpperCase(), FinanceProcurementConstants.REQUISITION_INFO_USER_NAME
                                                                                                ,draftStatus)
        assert requisitions > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByStatusAndInvalidSearchParam() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatusAndSearchParam( 'draft_invalid',
                                                                                  FinanceProcurementConstants.REQUISITION_INFO_USER_NAME ,draftStatus)
        assert requisitions == 0
    }

    /**
     * Test Fetch Requisitions for specified status
     */
    @Test
    void listRequisitionsByStatusAndTransactionDate() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatusAndTransactionDate(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, FinanceCommonUtility.parseDate( '06/19/2015' ),
                pagingParams ,draftStatus )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid User
     */
    @Test
    void listRequisitionsByStatusAndTransactionDateWithInvalidUser() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatusAndTransactionDate( 'INVALID_USER', FinanceCommonUtility.parseDate( '06/19/2015' ),
                                                                                     pagingParams ,draftStatus)
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid Status
     */
    @Test
    void listRequisitionsByStatusAndTransactionDateWithInvalidSearchParam() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByStatusAndTransactionDate(
                FinanceProcurementConstants.REQUISITION_INFO_USER_NAME, FinanceCommonUtility.parseDate( '06/19/2000' ),
                pagingParams ,draftStatus)
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByStatusAndTransactionDate() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatusAndTransactionDate(  FinanceCommonUtility.parseDate( '06/19/2015' ),
                                                                                       FinanceProcurementConstants.REQUISITION_INFO_USER_NAME,draftStatus)
        assert requisitions > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByStatusAndInvalidTransactionDate() {
        def draftStatus = [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT,
                           FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED]
        def requisitions = RequisitionInformation.fetchRequisitionsCountByStatusAndTransactionDate( FinanceCommonUtility.parseDate( '06/19/2000' ),
                                                                                  FinanceProcurementConstants.REQUISITION_INFO_USER_NAME ,draftStatus)
        assert requisitions == 0
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class for RequisitionInformationIntegration
 */
class RequisitionInformationIntegrationTests extends BaseIntegrationTestCase {

    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
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
        def requisitions = RequisitionInformation.listRequisitionsByUser( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams )
        assertNotNull requisitions
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid User
     */
    @Test
    void listRequisitionsByStatusWithInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByUser( 'INVALID_USER', pagingParams )
        assert requisitions.size() == 0
    }

    /**
     * Test Fetch Requisitions for specified status With Invalid Status
     */
    @Test
    void listRequisitionsByStatusWithInvalidStatus() {
        def pagingParams = [max: 500, offset: 0]
        def requisitions = RequisitionInformation.listRequisitionsByUser( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams )
        assert requisitions.size() > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByStatus() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountByUser( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                                                                                  [FinanceProcurementConstants.REQUISITION_LIST_STATUS_DRAFT] )
        assert requisitions > 0
    }

    /**
     * Test Fetch Requisitions count for specified status
     */
    @Test
    void listRequisitionsCountByInvalidStatus() {
        def requisitions = RequisitionInformation.fetchRequisitionsCountByUser( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, ['draft_invalid'] )
        assert requisitions == 0
    }

}

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
 * Test class for RequisitionInformationService
 */
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
    }

    /**
     * test fetch count
     */
    @Test
    void fetchRequisitionsCountByStatus() {
        def count = requisitionInformationService.fetchRequisitionsCountByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], null )
        assertTrue count > 0
    }

    /**
     * test fetch count with invalid user
     */
    @Test
    void fetchRequisitionsCountByStatusWithInvalidUser() {
        def count = requisitionInformationService.fetchRequisitionsCountByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], 'Invalid' )
        assertTrue count == 0
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
     * test fetch requisitions with invalid user
     */
    @Test
    void listRequisitionsByStatusWithInvalidUser() {
        def pagingParams = [max: 500, offset: 0]
        def result = requisitionInformationService.listRequisitionsByStatus( [FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT], pagingParams, 'Invalid' )
        assertTrue result.count == 0
    }
}

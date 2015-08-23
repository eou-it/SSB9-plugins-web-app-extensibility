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
 * Test class for RequisitionHeaderService
 */
class RequisitionHeaderServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionHeaderService
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
     * test Listing headers
     */
    @Test
    void testListRequisitionHeadersByUserName() {
        def paginationParam = [max: 500, offset: 0]
        def requestHeader = newRequisitionHeader()
        requestHeader.save( failOnError: true, flush: true )
        requestHeader.refresh()
        def headers = requisitionHeaderService.listRequisitionHeaderForLoggedInUser( paginationParam )
        assertTrue headers.size() > 0
    }

    /**
     * test Listing headers with now list
     */
    @Test
    void testListRequisitionHeadersNoList() {
        login 'grails_user', 'u_pick_it'
        def paginationParam = [max: 500, offset: 0]
        try {
            requisitionHeaderService.listRequisitionHeaderForLoggedInUser( paginationParam )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     * test Listing headers with invalid user.
     */
    @Test
    void testListRequisitionHeadersInvalidUser() {
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def paginationParam = [max: 500, offset: 0]
        try {
            requisitionHeaderService.listRequisitionHeaderForLoggedInUser( paginationParam )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID
        } catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     *  Test Finding headers for specified code. Failed case
     */
    @Test
    void testFindRequisitionHeaderByRequestCodeFailedCase() {

        try {
            requisitionHeaderService.findRequisitionHeaderByRequestCode( 'INVALID_CODE' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     *  Test Finding headers for specified code
     */
    @Test
    void testFindRequisitionHeaderByRequestCode() {

        def requestHeader = newRequisitionHeader()
        requestHeader.save( failOnError: true, flush: true )
        requestHeader.refresh()
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestHeader.requestCode )
        assertTrue header.requestCode == requestHeader.requestCode
    }

    /**
     * Test case to test recall requisition.
     */
    @Test
    public void testRecallRequisition() {
        requisitionHeaderService.recallRequisition( 'RSED0007' )
    }

    /**
     * Test case to test recall requisition.
     */
    @Test
    public void testRecallRequisitionHavingNoApprovalHistory() {
        requisitionHeaderService.recallRequisition( 'RSED0012' )
    }

    /**
     * Test case to test fail case for recall requisition.
     */
    @Test
    public void testRecallRequisitionFailCase() {
        try {
            requisitionHeaderService.recallRequisition( 'RSED0001' )
        } catch (ApplicationException e) {
            assertApplicationException e, (FinanceProcurementConstants.ERROR_MESSAGE_RECALL_REQUISITION_PENDING_REQ_IS_REQUIRED)
        }
    }

    /**
     * New object of Requisition Header
     * @return
     */
    private RequisitionHeader newRequisitionHeader() {
        def name = "Maneesh"
        def requestorPhoneAreaCode = "080"
        def requestorPhoneNumber = "242037662"
        def requestorPhoneExt = "9066"
        def vendorPidm = 278
        def addressType = "BU"
        def vendorAddressTypeSequence = 1
        def chartOfAccountsCode = "B"
        def orgnCode = "11103"
        def attentionTo = "Avery Johnson"
        def isDocumentLevelAccounting = true
        def requestTypeIndicator = "P"
        def ship_Code = "EAST"
        def matchRequired = "U"
        def requisitionHeader = new RequisitionHeader(
                requestCode: FinanceProcurementConstants.DEFAULT_REQUEST_CODE,
                requestDate: new Date(),
                transactionDate: new Date(),
                postingDate: new Date(),
                deliveryDate: new Date() + 20,
                requesterName: name,
                ship: ship_Code,
                requesterPhoneArea: requestorPhoneAreaCode,
                requesterPhoneNumber: requestorPhoneNumber,
                requesterPhoneExtension: requestorPhoneExt,
                vendorPidm: vendorPidm,
                vendorAddressType: addressType,
                vendorAddressTypeSequence: vendorAddressTypeSequence,
                chartOfAccount: chartOfAccountsCode,
                organization: orgnCode,
                attentionTo: attentionTo,
                isDocumentLevelAccounting: isDocumentLevelAccounting,
                requestTypeIndicator: requestTypeIndicator,
                matchRequired: matchRequired,
                lastModified: new Date(),
                userId: FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                dataOrigin: "Banner"
        )
        return requisitionHeader
    }
}

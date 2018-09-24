/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class for RequisitionHeaderIntegration
 */
class RequisitionHeaderIntegrationTests extends BaseIntegrationTestCase {

    def reqCode = "RSED0001"
    def financeUtilityService

    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }


    @After
    void tearDown() {
        super.tearDown()
        logout()
    }

    /**
     * Test Fetch Request for specified requestCode
     */
    @Test
    void testFetchRequisitionHeaderByRequestCode() {
        def header = RequisitionHeader.fetchByRequestCode( reqCode, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME )
        assertNotNull header
        assertEquals reqCode, header.requestCode
    }

    /**
     * Test Fetch Request for specified user
     */
    @Test
    void testFetchRequisitionHeaderByUser() {
        def requestHeader = newRequisitionHeader()
        try {
            requestHeader.save( failOnError: true, flush: true )
            assertNotNull requestHeader.id
        }
        catch (e) {
            Lo
        }
        def pagingParams = [max: 500, offset: 0]
        def header = RequisitionHeader.fetchByUser( FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams )
        assertNotNull header
        assertTrue( header.list.size() > 0 )
    }

    /**
     * Test Fetch Request for specified user Invalid user
     */
    @Test
    void testFetchRequisitionHeaderByUserInvalidCase() {
        def pagingParams = [max: 500, offset: 0]
        def header = RequisitionHeader.fetchByUser( 'INVALID_USER', pagingParams )
        assertNotNull header
        assertTrue( header.list.size() == 0 )
    }

    /**
     * Test to create requisition Headers
     */
    @Test
    void testCreate() {

        def requestHeader = newRequisitionHeader()
        requestHeader.save( failOnError: true, flush: true )
        assertNotNull requestHeader.id
        def reqId = requestHeader.id
        requestHeader.refresh()
        def request = RequisitionHeader.findById( reqId )
        assertNotNull request.id
        assertNotNull request.requestCode
        assertFalse( request.requestCode == financeUtilityService.getLocalizedNextKeyword() )
    }

    /**
     * Test invalid case for create requisition header
     */
    @Test
    void testInvalidCreate() {
        def requestHeader = newRequisitionHeader()
        requestHeader.deliveryDate = null
        shouldFail {
            requestHeader.save( failOnError: true, flush: true )
        }
    }

    /**
     * New object of Requisition Header
     * @return
     */
    public RequisitionHeader newRequisitionHeader() {


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
                requestCode:financeUtilityService.getLocalizedNextKeyword(),
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
                dataOrigin: FinanceProcurementConstants.DEFAULT_REQUISITION_ORIGIN
        )
        return requisitionHeader
    }
}

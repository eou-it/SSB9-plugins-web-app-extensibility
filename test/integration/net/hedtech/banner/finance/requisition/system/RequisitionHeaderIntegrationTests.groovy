/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class for RequisitionHeaderIntegration
 */
class RequisitionHeaderIntegrationTests extends BaseIntegrationTestCase {

    def reqCode = "R0000026"
    def final DEFAULT_REQ_CODE = 'NEXT'


    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()

    }


    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test Fetch Request for specified requestCode
     */
    @Test
    void testFetchRequisitionHeaderByRequestCode() {
        def header = RequisitionHeader.fetchByRequestCode( reqCode )
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
            e.printStackTrace()
        }
        def pagingParams = [max: 500, offset: 0]
        def header = RequisitionHeader.fetchByUser( 'FIMSPRD', pagingParams )
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
        try {
            requestHeader.save( failOnError: true, flush: true )
            assertNotNull requestHeader.id
        }
        catch (e) {
            e.printStackTrace()
        }
        def reqId = requestHeader.id
        requestHeader.refresh()

        def request = RequisitionHeader.findById( reqId )
        assertNotNull request.id
        assertNotNull request.requestCode
        assertFalse( request.requestCode == DEFAULT_REQ_CODE )
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
        def addressTypeSequence = 1
        def chartOfAccountsCode = "B"
        def orgnCode = "11103"
        def attentionTo = "Avery Johnson"
        def singleAcctgIndicator = true
        def requestTypeIndicator = "P"
        def ship_Code = "EAST"
        def matchRequired = "U"
        def requisitionHeader = new RequisitionHeader(
                requestCode: DEFAULT_REQ_CODE,
                requestDate: new Date(),
                transactionDate: new Date(),
                postingDate: new Date(),
                deliveryDate: new Date() + 20,
                name: name,
                ship: ship_Code,
                phoneArea: requestorPhoneAreaCode,
                phoneNumber: requestorPhoneNumber,
                phoneExtension: requestorPhoneExt,
                vendorPidm: vendorPidm,
                addressType: addressType,
                addressTypeSequence: addressTypeSequence,
                chartOfAccount: chartOfAccountsCode,
                organization: orgnCode,
                attentionTo: attentionTo,
                singleAcctgDistrIndicator: singleAcctgIndicator,
                requestTypeIndicator: requestTypeIndicator,
                matchRequired: matchRequired,
                lastModified: new Date(),
                userId: "FIMSPRD",
                dataOrigin: "Banner"
        )
        return requisitionHeader
    }
}

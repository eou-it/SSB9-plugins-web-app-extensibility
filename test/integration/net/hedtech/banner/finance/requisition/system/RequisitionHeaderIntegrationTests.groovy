package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequisitionHeaderIntegrationTests extends BaseIntegrationTestCase {

    def reqCode = "R0000026"
    def institutionName = "Maneesh"
    def requestorPhoneAreaCode = "080"
    def requestorPhoneNumber = "242037662"
    def requestorPhoneExt = "9066"
    def vendorPidm = 278
    def requestorAccountTypeCode = "BU"
    def requestorAccountTypeSeqNum = 1
    def chartOfAccountsCode = "B"
    def orgnCode = "11103"
    def attentionTo = "Avery Johnson"
    def singleAcctgIndicator = true
    def requestTypeIndicator = "P"
    def ship_Code = "EAST"
    def matchRequired = "U"


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
    void testFetchRequest() {
        def request = RequisitionHeader.fetchByRequestCode( reqCode )
        assertNotNull request
        assertEquals reqCode, request.requestCode
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
        catch(e){
            e.printStackTrace(  )
        }
        def reqId = requestHeader.id
        requestHeader.refresh()

        def request = RequisitionHeader.findById( reqId )
        assertNotNull request.id
        assertNotNull request.requestCode
        assertFalse( request.requestCode == "NEXT" )
    }


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

        def requisitionHeader = new RequisitionHeader(
                requestCode: "NEXT",
                requestDate: new Date(),
                transactionDate: new Date(),
                postingDate: new Date(),
                deliveryDate: new Date() + 20,
                institutionName: institutionName,
                ship: ship_Code,
                phoneArea: requestorPhoneAreaCode,
                shipToPhoneNumber: requestorPhoneNumber,
                phoneExtension: requestorPhoneExt,
                vendorPidm: vendorPidm,
                accountType: requestorAccountTypeCode,
                accountTypeSequenceNumber: requestorAccountTypeSeqNum,
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

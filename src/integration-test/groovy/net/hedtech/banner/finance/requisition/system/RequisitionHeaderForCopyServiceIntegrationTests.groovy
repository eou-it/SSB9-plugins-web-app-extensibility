/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
/**
 * Test class for RequisitionHeaderForCopyService
 */

@Integration
@Rollback
class RequisitionHeaderForCopyServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionHeaderForCopyService
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
     * Test case to test pre delete method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreDeleteFailCase() {
        def map = [:]
        requisitionHeaderForCopyService.delete( map )
    }

    /**
     * Test case to test pre update method which will throw the ApplicationException.
     */
    @Test(expected = ApplicationException.class)
    void testPreUpdateFailCase() {
        def map = RequisitionHeaderForCopy.findByRequestCode( 'RSED0005' )
        map.requestCode = 'RSED0001'
        requisitionHeaderForCopyService.update( map )
    }

    /**
     *  Test Finding headers for specified code
     */
    @Test
    void testFindRequisitionHeaderByRequestCode() {

        def requestHeaderForCopy = newRequisitionHeaderForCopy()
        requestHeaderForCopy = requisitionHeaderForCopyService.create( [domainModel: requestHeaderForCopy] )
        assertTrue requestHeaderForCopy.requestCode == requestHeaderForCopy.requestCode
    }

    /**
     *  Test Finding headers
     */
    @Test
    void listRequisitionHeader() {

        def requisitions = requisitionHeaderForCopyService.listRequisitionHeader( null, [offset: 0, max: 10] )
        assert requisitions.size() > 0
    }

    /**
     *  Test Finding headers
     */
    @Test
    void listRequisitionHeaderForReqCode() {

        def requisitions = requisitionHeaderForCopyService.listRequisitionHeader( 'RSED0005', [offset: 0, max: 10] )
        assert requisitions.size() == 1
    }

    /**
     * New object of Requisition Header
     * @return
     */
    private RequisitionHeaderForCopy newRequisitionHeaderForCopy() {
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
        def requisitionHeader = new RequisitionHeaderForCopy(
                requestCode: 'R100001',
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

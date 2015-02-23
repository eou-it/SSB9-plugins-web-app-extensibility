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
import org.springframework.security.authentication.BadCredentialsException

/**
 * Test class for RequisitionHeaderService
 */
class PurchaseRequisitionCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def purchaseRequisitionCompositeService

    def requisitionHeaderService

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
     * Test create
     */
    @Test
    void createPurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def requestCode = purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
        assertTrue requestCode?.headerReqCode != FinanceProcurementConstants.DEFAULT_REQUEST_CODE
    }

    /**
     * Test create With Invalid user
     */
    @Test(expected = BadCredentialsException.class)
    void createPurchaseRequisitionInvalidUser() {
        login 'Invalid_user', 'invalid_password'
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
    }

    /**
     * Test create With invalid Currency
     */
    @Test(expected = ApplicationException.class)
    void createPurchaseRequisitionInvalidCcy() {
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.currency = 'ABC'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
    }

    /**
     * Test delete
     */
    @Test
    void deletePurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def requestCode = purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
        purchaseRequisitionCompositeService.deletePurchaseRequisition( requestCode?.headerReqCode )
        try {
            requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode?.headerReqCode )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     * Test update
     */
    @Test
    void updatePurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.requesterName = 'Modified'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def header = purchaseRequisitionCompositeService.updateRequisitionHeader( domainModelMap, 'R0000026' )
        assertTrue header.requesterName == 'Modified'
    }

    /**
     * Test update
     */
    @Test
    void updatePurchaseRequisitionInvalidCode() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.requesterName = 'Modified'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        try {
            purchaseRequisitionCompositeService.updateRequisitionHeader( domainModelMap, 'INVALID' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     * New object of Requisition Header
     * @return
     */
    private RequisitionHeader newRequisitionHeader() {
        return [
                'requestCode'              : FinanceProcurementConstants.DEFAULT_REQUEST_CODE,
                'requestDate'              : new Date( '18-Feb-2015' ),
                'transactionDate'          : new Date( '19-Feb-2015' ),
                'requesterName'            : 'Caliper College_u1',
                'ship'                     : 'EAST',
                'requesterPhoneNumber'     : '242037662',
                'requesterPhoneArea'       : '080',
                'requesterPhoneExtension'  : '9066',
                'vendorPidm'               : '278',
                'vendorAddressType'        : 'BU',
                'vendorAddressTypeSequence': 1,
                'chartOfAccount'           : 'B',
                'organization'             : 11103,
                'attentionTo'              : 'Avery Johnson',
                'isDocumentLevelAccounting': true,
                'requestTypeIndicator'     : 'P',
                'matchRequired'            : 'N',
                'requesterFaxArea'         : '2323',
                'requesterFaxNumber'       : '23823292',
                'requesterFaxExtension'    : '2323',
                'requesterFaxCountry'      : 'IND',
                'requesterEmailAddress'    : 'shiv@shiv.com',
                'deliveryComment'          : 'test',
                'taxGroup'                 : 'AU',
                'discount'                 : 30,
                'currency'                 : 'CAN',
                'vendorContact'            : 'Bangalore',
                'vendorEmailAddress'       : 'vendor@vendorgroup.com',
                'requisitionOrigination'   : FinanceProcurementConstants.DEFAULT_REQUISITION_ORIGIN,
                'deliveryDate'             : new Date( '20-Feb-2015' )
        ]
    }
}

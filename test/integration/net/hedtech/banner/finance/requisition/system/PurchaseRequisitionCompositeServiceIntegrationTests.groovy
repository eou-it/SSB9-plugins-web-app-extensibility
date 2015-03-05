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
 * Test class for PurchaseRequisitionCompositeService
 */
class PurchaseRequisitionCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def purchaseRequisitionCompositeService

    def requisitionHeaderService
    def requisitionDetailService
    def springSecurityService
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
                'requesterEmailAddress'    : 'test@test.com',
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

    def requestHeaderCode = "R0001228"

    /**
     * Test create Requisition Detail
     */
    @Test
    void testCreatePurchaseRequisitionDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqDetailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        def requestCode = purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
        assertTrue requestCode?.requestCode == requestHeaderCode
    }

    /**
     * Test create With Invalid user
     */
    @Test(expected = BadCredentialsException.class)
    void testCreatePurchaseRequisitionDetailInvalidUser() {
        login 'Invalid_user', 'invalid_password'
        def reqDetailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
    }

    /**
     * Test create With invalid Requisition Header Code
     */
    @Test(expected = ApplicationException.class)
    void testCreatePurchaseRequisitionInvalidRequisitionCode() {
        def reqDetailDomainModel = getRequisitionDetails()
        reqDetailDomainModel.requestCode = 'R0000129182991'
        def domainModelMap = [requisitionDetail: reqDetailDomainModel]
        purchaseRequisitionCompositeService.createPurchaseRequisition( domainModelMap )
    }

    /**
     * Test case to test delete purchase requisition detail.
     */
    @Test
    void testDeletePurchaseRequisitionDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        purchaseRequisitionCompositeService.deletePurchaseRequisitionDetail( 'R0000561', '1' )
        try {
            requisitionDetailService.getRequisitionDetailByRequestCodeAndItem( 'R0000561', '1' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL
        }
    }

    /**
     * Test update
     */
    @Test
    void updatePurchaseDetail() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def detailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: detailDomainModel]
        def detail = purchaseRequisitionCompositeService.updateRequisitionDetail( domainModelMap, requestHeaderCode, '1' )
        detail.requestCode = requestHeaderCode
    }

    /**
     * Test update  with empty list
     */
    @Test
    void updatePurchaseDetailWithEmptyList() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def detailDomainModel = getRequisitionDetails()
        def domainModelMap = [requisitionDetail: detailDomainModel]
        try {
            purchaseRequisitionCompositeService.updateRequisitionDetail( domainModelMap, requestHeaderCode, '' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_ITEM_IS_REQUIRED
        }
    }

    /**
     * The method is used to get the RequisitionDetail object with all required values to insert/update.
     * @return RequisitionDetail.
     */
    private RequisitionDetail getRequisitionDetails() {
        def requisitionDetail = [
                'requestCode': requestHeaderCode,
                'userId': FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                'commodity': '2210000000',
                'commodityDescription': 'New Requisition Detail',
                'quantity': '2',
                'unitOfMeasure': 'EA',
                'unitPrice': '99.99',
                'suspenseIndicator': true,
                'textUsageIndicator': FinanceProcurementConstants.DEFAULT_FPBREQD_TEXT_USAGE,
                'discountAmount': '0',
                'additionalChargeAmount': '9',
                'taxGroup': 'NT',
                'dataOrigin': FinanceProcurementConstants.DEFAULT_REQUISITION_ORIGIN
        ]
        return requisitionDetail
    }
}

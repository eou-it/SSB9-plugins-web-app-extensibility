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
 * Test class for Requisition Details Composite Service
 */
class RequisitionInformationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionInformationCompositeService
    def requisitionHeaderCompositeService
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
     * Test fetchPurchaseRequisition
     */
    @Test
    void fetchPurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def code = requisitionHeaderCompositeService.createPurchaseRequisitionHeader([requisitionHeader: newRequisitionHeader()])
        assertTrue requisitionInformationCompositeService.fetchPurchaseRequisition(code, 'USD').header.requestCode == code
    }

    /**
     * Test fetchPurchaseRequisition
     */
    @Test
    void fetchPurchaseRequisitionHeaderCurrCheck() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def header = newRequisitionHeader()
        header.currency = null
        header.vendorAddressType = null
        header.vendorPidm = null
        header.taxGroup = null
        def code = requisitionHeaderCompositeService.createPurchaseRequisitionHeader([requisitionHeader: header])
        assertTrue requisitionInformationCompositeService.fetchPurchaseRequisition(code, 'USD').header.requestCode == code
    }

    /**
     * New object of Requisition Header
     * @return
     */
    private def newRequisitionHeader() {
        return [
                'requestCode'              : FinanceProcurementConstants.DEFAULT_REQUEST_CODE,
                'requestDate'              : new Date('18-Feb-2015'),
                'transactionDate'          : new Date('19-Feb-2015'),
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
                'deliveryDate'             : new Date('20-Feb-2015'),
                'privateComment'           : 'This is test comment and this comment is header level private comment. There are two types of comment. The one is Header leve and second one is commodity level comment.',
                'publicComment'            : 'This is test comment and this comment is header level public comment. There are two types of comment. The one is Header leve and second one is commodity level comment.'
        ]
    }
}

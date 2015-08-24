/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.BadCredentialsException

/**
 * Test class for Requisition Header Composite Service
 */
class RequisitionHeaderCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionHeaderCompositeService
    def requisitionHeaderService
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
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
    }
    /**
     * Test create
     */
    @Test
    void createPurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def requestCode = requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
        assertTrue requestCode != FinanceProcurementConstants.DEFAULT_REQUEST_CODE
    }

    /**
     * Test create where Tax processing Off
     */
    @Test
    void createPurchaseRequisitionWhereTaxProcessingOff() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        Session session = sessionFactory.getCurrentSession()
        session.createSQLQuery( "UPDATE FOBSYSC set FOBSYSC_TAX_PROCESSING_IND = 'N' WHERE FOBSYSC_STATUS_IND='A' " ).executeUpdate()
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def requestCode = requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
        assertTrue requestCode != FinanceProcurementConstants.DEFAULT_REQUEST_CODE
    }

    /**
     * Test delete requisition No force delete, bdm installed
     */
    /*@Test
    void deletePurchaseRequisitionNoForceDeleteBDMInstalled() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionHeaderCompositeService.deletePurchaseRequisition( 'RSED0005', false, 'MEP', true )
    }*/

    /**
     * Test delete requisition. Bdm not installed
     */
    /*@Test
    void deletePurchaseRequisitionNoForceDeleteBDMNotInstalled() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionHeaderCompositeService.deletePurchaseRequisition( 'RSED0005', false, 'MEP', false )
    }*/

    /**
     * Test delete requisition
     */
    @Test
    void deletePurchaseRequisitionForceDeleteCompleteReq() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        try {
            requisitionHeaderCompositeService.deletePurchaseRequisition( 'RSED0005', true, 'MEP', false )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_DELETE_REQUISITION_DRAFT_OR_DISAPPROVED_REQ_IS_REQUIRED )
        }

    }

    /**
     * Test delete requisition
     */
    @Test
    void deletePurchaseRequisitionForceDeletePendingReq() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        try {
            requisitionHeaderCompositeService.deletePurchaseRequisition( 'RSED0007', true, 'MEP', false )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_DELETE_REQUISITION_DRAFT_OR_DISAPPROVED_REQ_IS_REQUIRED )
        }
    }

    /**
     * Test delete requisition for draft req
     */
    @Test
    void deletePurchaseRequisitionForceDeleteDraftReq() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionHeaderCompositeService.deletePurchaseRequisition( 'RSED0001', true, 'MEP', false )
    }

    /**
     * Test delete requisition for dis-approved req
     */
    @Test
    void deletePurchaseRequisitionForceDeleteDisapprovedReq() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionHeaderCompositeService.deletePurchaseRequisition( 'RSED0006', true, 'MEP', false )
    }

    /**
     * Test create with tax null
     */
    @Test
    void createPurchaseRequisitionWithNoTax() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def financeSystemControlServiceMeta = requisitionHeaderCompositeService.financeSystemControlService.metaClass
        def requisitionHeaderServiceMeta = requisitionHeaderCompositeService.requisitionHeaderService.metaClass
        def requisitionHeaderMeta = RequisitionHeader.metaClass
        try {
            def headerDomainModel = newRequisitionHeader()
            requisitionHeaderCompositeService.financeSystemControlService.metaClass.findActiveFinanceSystemControl = {
                def map = [taxProcessingIndicator: FinanceValidationConstants.REQUISITION_INDICATOR_NO]
                return map
            }
            requisitionHeaderCompositeService.requisitionHeaderService.metaClass.create = {
                return [id: 1]
            }
            RequisitionHeader.metaClass.read {
                return [requestCode: headerDomainModel.requestCode]
            }
            def domainModelMap = [requisitionHeader: headerDomainModel]
            def requestCode = requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
            assertTrue requestCode != FinanceProcurementConstants.DEFAULT_REQUEST_CODE
        } finally {
            requisitionHeaderCompositeService.financeSystemControlService.metaClass = financeSystemControlServiceMeta
            requisitionHeaderCompositeService.requisitionHeaderService.metaClass = requisitionHeaderServiceMeta
            RequisitionHeader.metaClass = requisitionHeaderMeta
        }
    }

    /**
     * Test create With Invalid user
     */
    @Test(expected = BadCredentialsException.class)
    void createPurchaseRequisitionInvalidUser() {
        login 'Invalid_user', 'invalid_password'
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
    }

    /**
     * Test create With invalid Currency
     */
    @Test(expected = ApplicationException.class)
    void createPurchaseRequisitionInvalidCcy() {
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.currency = 'ABC'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
    }

    /**
     * Test create to test the invalid user.
     */
    @Test
    void createPurchaseRequisitionForInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        try {
            requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test delete
     */
    @Test
    void deletePurchaseRequisitionHeader() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def requestCode = requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
        requisitionHeaderCompositeService.deletePurchaseRequisitionHeader( requestCode )
        try {
            requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     * Test Delete Completed Requisition Header for a specified request code
     */
    @Test
    void testCompleteRequisitionInvalidReq() {
        try {
            requisitionHeaderService.completeRequisition( 'INVALID', 'no' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     * Test Delete Completed Requisition Header for a specified request code
     */
    @Test
    void testCompleteAlreadyCompletedRequisition() {
        try {
            requisitionHeaderService.completeRequisition( 'RSED0005', 'no' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        }
    }

    /**
     * Test Completed Requisition Header for a specified request code
     */
    @Test
    void testCompleteRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue requisitionHeaderService.completeRequisition( 'RSED0010', 'yes' ).completeIndicator
    }

    /**
     * Test update
     */
    @Test
    void updateAlreadyCompletedPurchaseRequisition() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.requesterName = 'Modified'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        try {
            requisitionHeaderCompositeService.updateRequisitionHeader( domainModelMap, 'RSED0005', 'USD' )
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        }
    }

    /**
     * Test update for changing document level to commodity level.
     */
    @Test
    void updateAlreadyCompletedPurchaseRequisitionForCommodityLvlAcc() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.isDocumentLevelAccounting = false
        headerDomainModel.requesterName = 'Modified'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        try {
            requisitionHeaderCompositeService.updateRequisitionHeader( domainModelMap, 'RSED0005', 'USD' )
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
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
        assert 'Modified' == requisitionHeaderCompositeService.updateRequisitionHeader( domainModelMap, 'RSED0001', 'USD' ).requesterName
    }

    /**
     * Test update to test invalid user.
     */
    @Test
    void updatePurchaseRequisitionInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        def headerDomainModel = newRequisitionHeader()
        headerDomainModel.requesterName = 'Modified'
        def domainModelMap = [requisitionHeader: headerDomainModel]
        try {
            springSecurityService.getAuthentication().user.oracleUserName = ''
            requisitionHeaderCompositeService.updateRequisitionHeader( domainModelMap, 'RSED0001', 'USD' )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test update to test invalid user.
     */
    @Test
    void updatePurchaseRequisitionWithText() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def headerDomainModel = newRequisitionHeader()
        def domainModelMap = [requisitionHeader: headerDomainModel]
        def requestCode = requisitionHeaderCompositeService.createPurchaseRequisitionHeader( domainModelMap )
        assert requestCode == requisitionHeaderCompositeService.updateRequisitionHeader( domainModelMap, requestCode, 'USD' ).requestCode
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
            requisitionHeaderCompositeService.updateRequisitionHeader( domainModelMap, 'INVALID', 'USD' )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER
        }
    }

    /**
     * Test case to get currency by requisition code which have currency.
     */
    @Test
    void testGetCurrencyDetailByReqCode() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def currency = requisitionHeaderCompositeService.getCurrencyDetailByReqCode( 'RSED0001', 'USD' );
        assertNotNull( currency )
    }

    /**
     * Test case to get currency by requisition code which have currency.
     */
    @Test
    void testGetCurrencyDetailByReqCodeWithNonDefaultCcy() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def currency = requisitionHeaderCompositeService.getCurrencyDetailByReqCode( 'RSED0010', 'USD' );
        assertNotNull( currency )
    }

    /**
     * Test case to get currency by requisition code which don't have currency.
     */
    @Test
    void testGetCurrencyDetailByReqCodeByNoCurInReq() {
        def currency = requisitionHeaderCompositeService.getCurrencyDetailByReqCode( 'RSED0001', 'USD' );
        assertNotNull( currency )
    }

    /**
     * New object of Requisition Header
     * @return
     */
    private def newRequisitionHeader() {
        return [
                'requestCode'              : FinanceProcurementConstants.DEFAULT_REQUEST_CODE,
                'requestDate'              : new Date(),
                'transactionDate'          : new Date(),
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
                'deliveryDate'             : new Date() + 1
        ]
    }
}

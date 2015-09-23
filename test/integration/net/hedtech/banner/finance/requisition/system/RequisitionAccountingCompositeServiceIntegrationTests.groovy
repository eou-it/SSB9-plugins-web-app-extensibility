/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class for PurchaseRequisitionCompositeService
 */
class RequisitionAccountingCompositeServiceIntegrationTests extends BaseIntegrationTestCase {


    def requisitionAccountingCompositeService
    def requisitionAccountingService
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
     * Test create Requisition Accounting.
     */
    @Test
    void testCreateRequisitionAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqAccountingDomainModel = getRequestAccounting()
        reqAccountingDomainModel.requisitionAmount = null
        reqAccountingDomainModel.requestCode = 'RSED0003'
        def domainModelMap = [requisitionAccounting: reqAccountingDomainModel]
        def requestCode = requisitionAccountingCompositeService.createPurchaseRequisitionAccounting( domainModelMap )
        assertTrue requestCode?.requestCode == reqAccountingDomainModel.requestCode
        assertTrue requestCode?.item == reqAccountingDomainModel.item
        assertTrue requestCode?.sequenceNumber == reqAccountingDomainModel.sequenceNumber
    }

    /**
     * Test create Requisition Accounting for CLA.
     */
    @Test
    void testCreateRequisitionAccountingForCLA() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def reqAccountingDomainModel = getRequestAccounting()
        reqAccountingDomainModel.requisitionAmount = null
        reqAccountingDomainModel.requestCode = 'RSED0004'
        reqAccountingDomainModel.item = 1
        def domainModelMap = [requisitionAccounting: reqAccountingDomainModel]
        def requestCode = requisitionAccountingCompositeService.createPurchaseRequisitionAccounting( domainModelMap )
        assertTrue requestCode?.requestCode == reqAccountingDomainModel.requestCode
        assertTrue requestCode?.item == reqAccountingDomainModel.item
        assertTrue requestCode?.sequenceNumber == reqAccountingDomainModel.sequenceNumber
    }

    /**
     * Test create Requisition Accounting by passing wrong user.
     */
    @Test
    void testCreateRequisitionAccountingByPassingWrongUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def reqAccountingDomainModel = getRequestAccounting()
        def domainModelMap = [requisitionAccounting: reqAccountingDomainModel]
        try {
            requisitionAccountingCompositeService.createPurchaseRequisitionAccounting( domainModelMap )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test case to test delete purchase requisition accounting information.
     */
    @Test
    void testDeletePurchaseRequisitionAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        requisitionAccountingCompositeService.deletePurchaseRequisitionAccountingInformation( 'RSED0003', 0, 1 )
        try {
            requisitionAccountingService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 1 )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING
        } catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING
        }
    }

    /**
     * Test update Requisition Accounting.
     */
    @Test
    void updatePurchaseAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = 0
        accountingDomainModel.sequenceNumber = 1
        accountingDomainModel.requestCode = 'RSED0003'
        accountingDomainModel.requisitionAmount = null
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        def accounting = requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
        assertTrue( accounting.requestCode == 'RSED0003' )
    }

    /**
     * Test update  with no item and sequence.
     */
    @Test
    void updatePurchaseAccountingWithNoItemAndSequence() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = null
        accountingDomainModel.sequenceNumber = null
        accountingDomainModel.requestCode = 'RSED0003'
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        try {
            requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED
        }
    }

    /**
     * Test update  with item and No sequence.
     */
    @Test
    void updatePurchaseAccountingWithItemAndNoSequence() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = 1
        accountingDomainModel.sequenceNumber = null
        accountingDomainModel.requestCode = 'RSED0003'
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        try {
            requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED
        }
    }

    /**
     * Test update  with No item but sequence.
     */
    @Test
    void updatePurchaseAccountingWithNoItemButSequence() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = null
        accountingDomainModel.sequenceNumber = 1
        accountingDomainModel.requestCode = 'RSED0003'
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        try {
            requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED
        }
    }

    /**
     * Test update already completed requisition.
     */
    @Test
    void updateAlreadyCompletedPurchaseAccounting() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = 1
        accountingDomainModel.sequenceNumber = 1
        accountingDomainModel.requestCode = 'RSED0005'
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        try {
            requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        }
    }

    /**
     * Test update  with no item and sequence.
     */
    @Test
    void updatePurchaseAccountingWithWrongItemAndSequence() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = 98999
        accountingDomainModel.sequenceNumber = 10221
        accountingDomainModel.requestCode = 'RSED0003'
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        try {
            requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING
        } catch (ApplicationException e) {
            assertApplicationException e, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING
        }
    }

    /**
     * Test update Requisition Accounting.
     */
    @Test
    void updatePurchaseAccountingForInvalidUser() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        def oracleUserName = springSecurityService.getAuthentication().user.oracleUserName
        springSecurityService.getAuthentication().user.oracleUserName = ''
        def accountingDomainModel = getRequestAccounting()
        accountingDomainModel.item = 0
        accountingDomainModel.sequenceNumber = 1
        accountingDomainModel.requestCode = 'RSED0003'
        def domainModelMap = [requisitionAccounting: accountingDomainModel]
        try {
            requisitionAccountingCompositeService.updateRequisitionAccounting( domainModelMap )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID )
        } finally {
            springSecurityService.getAuthentication().user.oracleUserName = oracleUserName
        }
    }

    /**
     * Test case method to find Requisition Accounting by request code, item and sequence number.
     */
    @Test
    public void testFindByRequestCodeItemAndSeq() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        assertTrue( requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 ).accounting.requestCode == 'RSED0003' )
    }

    /**
     * Test case method to find Requisition Accounting by request code, item and sequence number.
     * Invalidate key details
     */
    @Test
    public void testFindByRequestCodeItemAndSeqInvalidKeyDetails() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        Session session = sessionFactory.getCurrentSession()
        def requisitionAccounting = requisitionAccountingService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 )
        session.createSQLQuery( "UPDATE FTVACCI set FTVACCI_ACCI_CODE = 'INVALI' WHERE FTVACCI_ACCI_CODE='" + requisitionAccounting.accountIndex + "' " ).executeUpdate()
        assertNull requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 ).cifoapalp.index.title
        session.createSQLQuery( "UPDATE FTVFUND set FTVFUND_FUND_CODE = 'INVALI' WHERE FTVFUND_FUND_CODE='" + requisitionAccounting.fund + "' " ).executeUpdate()
        assertNull requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 ).cifoapalp.fund.title
        session.createSQLQuery( "UPDATE FTVORGN set FTVORGN_ORGN_CODE = 'INVALI' WHERE FTVORGN_ORGN_CODE='" + requisitionAccounting.organization + "' " ).executeUpdate()
        assertNull requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 ).cifoapalp.organization.title
        session.createSQLQuery( "UPDATE FTVACCT set ftvacct_acct_code = 'INVALI' WHERE ftvacct_acct_code='" + requisitionAccounting.account + "' " ).executeUpdate()
        assertNull requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 ).cifoapalp.account.accountingTitle
        session.createSQLQuery( "UPDATE FTVPROG set ftvprog_prog_code = 'INVALI' WHERE ftvprog_prog_code='" + requisitionAccounting.program + "' " ).executeUpdate()
        assertNull requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 ).cifoapalp.program.programTitle
    }

    /**
     * Test case method to find Requisition Accounting by request code, item and sequence number.
     */
    @Test
    public void testFindByRequestCodeItemAndSeqFailCase() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        try {
            requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 100, 2 )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING )
        }
    }

    /**
     * Test case method to find Requisition Accounting by request code, item and sequence number.
     */
    @Test
    public void testFindByRequestCodeItemAndSeqFailCaseWithException() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                    FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        try {
            requisitionAccountingCompositeService.findByRequestCodeItemAndSeq( 'RSED0003', 0, 2 )
        } catch (ApplicationException ae) {
            assertApplicationException( ae, FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING )
        }
    }

    /**
     * The method is used to get the RequisitionAccounting object with all required values to insert/update.
     * @return RequisitionAccounting.
     */
    private RequisitionAccounting getRequestAccounting() {
        def requestCode = 'RSED0003'
        def amount = 100.00
        def fiscalCode = '16'
        def period = '09'
        def ruleClassCode = 'REQP'
        def chartOfAccountsCode = 'B'
        def indexCode = 'EPHM54'
        def fundCode = 'EPMSF1'
        def orgnCode = '11007'
        def accountCode = '2320'
        def programCode = '10'
        def insufficientFundsOverrideIndicator = true
        def activityCode = ''
        def location = ''
        def projectCode = ''
        def percentage = '3'
        def discountAmount = ''
        def discountAmountPercent = ''
        def additionalChargeAmount = ''
        def additionalChargeAmountPct = ''
        def requestAccounting = [
                'requestCode'                       : requestCode,
                'activity'                          : activityCode,
                'location'                          : location,
                'project'                           : projectCode,
                'percentage'                        : percentage,
                'discountAmount'                    : discountAmount,
                'discountAmountPercent'             : discountAmountPercent,
                'additionalChargeAmount'            : additionalChargeAmount,
                'additionalChargeAmountPct'         : additionalChargeAmountPct,
                'requisitionAmount'                 : amount,
                'fiscalYearCode'                    : fiscalCode,
                'period'                            : period,
                'ruleClass'                         : ruleClassCode,
                'chartOfAccount'                    : chartOfAccountsCode,
                'accountIndex'                      : indexCode,
                'fund'                              : fundCode,
                'organization'                      : orgnCode,
                'account'                           : accountCode,
                'program'                           : programCode,
                'insufficientFundsOverrideIndicator': insufficientFundsOverrideIndicator
        ]
        return requestAccounting
    }
}

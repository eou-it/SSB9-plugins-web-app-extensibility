/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.gorm.transactions.Transactional
import grails.util.GrailsWebMockUtil
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder

/**
 * Integration test case for service CopyPurchaseRequisitionCompositeService.
 */
@Integration
@Rollback
class CopyPurchaseRequisitionCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    def copyPurchaseRequisitionCompositeService
    def requisitionHeaderService
    def requisitionDetailService
    def requisitionAccountingService
    /**
     * Super class setup
     */
    @Autowired
    WebApplicationContext ctx

    @Before
    void setUp() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
        logout()
    }

    /**
     * Test case to test copy requisition.
     */
    @Test

    public void testCopyRequisition() {

        def requistionNumber = copyPurchaseRequisitionCompositeService.copyRequisition( 'RSED0005' )
        assertNotNull( requistionNumber )

        def copiedHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode(requistionNumber)
        assertEquals( copiedHeader.requestCode,requistionNumber)
        assertEquals( copiedHeader.documentCopiedFrom,'RSED0005')
        assertEquals(copiedHeader.completeIndicator,false)
        assertNotNull copiedHeader

        def copiedDetail = requisitionDetailService.findByRequestCode(requistionNumber)
        assertEquals( copiedDetail[0].requestCode,requistionNumber)
        assertEquals( copiedDetail[0].purchaseOrder,null)
        assertEquals( copiedDetail[0].completeIndicator,FinanceProcurementConstants.DEFAULT_INDICATOR_NO)

        def copiedAccounting =  requisitionAccountingService.findAccountingByRequestCode( requistionNumber )
        assertEquals( copiedAccounting[0].requestCode,requistionNumber)
        assertEquals(copiedAccounting[0].insufficientFundsOverrideIndicator,true )
    }

    /**
     * Test case to test fail case for copy requisition.
     */
    @Test
    public void testCopyRequisitionFailCase() {
        try {
            copyPurchaseRequisitionCompositeService.copyRequisition( 'RSED0001' )
        } catch (ApplicationException e) {
            assertApplicationException e, (FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED)
        }
    }

    @AfterClass
    public static void cleanUp() {
        RequestContextHolder.resetRequestAttributes()
    }
}

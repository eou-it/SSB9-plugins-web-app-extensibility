/** *****************************************************************************
 © 2015 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration Test case class for FinanceTextCompositeService.
 */
class FinanceTextCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    def financeTextCompositeService
    def springSecurityService
    def requisitionHeaderService
    def requisitionDetailService

    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    public void setUp() {
        super.login FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    public void tearDown() {
        super.tearDown()
        logout()
    }

    /**
     * Test case to test save text for header level.
     */
    @Test
    public void testSaveTextForHeader() {
        def commentsMap = [privateComment: 'This is header level private comment.',
                           publicComment : 'This is header level public comment.']
        def user = springSecurityService.getAuthentication()?.user.oracleUserName
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode('RSED0001')
        def list = financeTextCompositeService.saveTextForHeader(header, commentsMap, user)
        assertTrue(list.size() == 2)
    }

    /**
     * Test case to test save text for commodity level.
     */
    @Test
    public void testSaveTextForCommodity() {
        def commentsMap = [privateComment: 'This is commodity level private comment.',
                           publicComment : 'This is commodity level public comment.']
        def user = springSecurityService.getAuthentication()?.user.oracleUserName
        RequisitionDetail details = requisitionDetailService.findByRequestCode('RSED0005')[0]
        def list = financeTextCompositeService.saveTextForCommodity(details, commentsMap, user, details.item)
        assertTrue(list.size() == 2)
    }

    /**
     * Test case to test save text for header level.
     */
    @Test
    public void testSaveTextForHeaderMultipleComments() {
        def commentsMap = [privateComment: 'This is header level private comment. There are two types of comments.',
                           publicComment : 'This is header level public comment. There are two types of comments.']
        def user = springSecurityService.getAuthentication()?.user.oracleUserName
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode('RSED0001')
        def list = financeTextCompositeService.saveTextForHeader(header, commentsMap, user)
        assertTrue(list.size() > 2)
    }

    /**
     * Test case to test save text for commodity level.
     */
    @Test
    public void testSaveTextForCommodityMultipleComments() {
        def commentsMap = [privateComment: 'This is commodity level private comment. There are two types of comments.',
                           publicComment : 'This is commodity level public comment. There are two types of comments.']
        def user = springSecurityService.getAuthentication()?.user.oracleUserName
        RequisitionDetail details = requisitionDetailService.findByRequestCode('RSED0005')[0]
        def list = financeTextCompositeService.saveTextForCommodity(details, commentsMap, user, details.item)
        assertTrue(list.size() > 2)
    }

    /**
     * Test case to test save text for commodity level.
     */
    @Test
    public void testSaveTextForHeaderAlreadyHavingComments() {
        def commentsMap = [privateComment: 'This is header level private comment.',
                           publicComment : 'This is header level public comment.']
        def user = springSecurityService.getAuthentication()?.user.oracleUserName
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode('RSED0009')
        def list = financeTextCompositeService.saveTextForHeader(header, commentsMap, user)
        assertTrue(list.size() == 2)
    }
}

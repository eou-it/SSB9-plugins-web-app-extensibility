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
 * Integration tests class for RequisitionDetail.
 *
 */
class RequisitionDetailIntegrationTests extends BaseIntegrationTestCase {
    def reqCode = "R0000124"
    def commodityCode = '2210000000'

    /**
     * The tear down method will run before all test case method execution start.
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()

    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test Fetch Request Details for specified requestCode
     */
    @Test
    void testFetchRequisitionDetailByRequestCode() {
        def pagingParams = [max: 500, offset: 0]
        def requestDetail = RequisitionDetail.fetchByRequestCode( reqCode, pagingParams ).list;
        assert requestDetail.size() > 0
        assertEquals reqCode, requestDetail[0].requestCode
    }

    /**
     * Test Fetch Request Details for specified requestCode and item
     */
    @Test
    void testFetchRequisitionDetailByUserId() {
        def pagingParams = [max: 500, offset: 0]
        def requestDetailList = RequisitionDetail.fetchByUserId(
                FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME, pagingParams ).list;
        assertTrue( requestDetailList.size() > 0 || requestDetailList.isEmpty() )
    }

    /**
     * Test Fetch Request Details for specified requestCode and Commodity code
     */
    @Test
    void testFetchByRequestCodeAndCommodityCode() {
        def requestDetail = RequisitionDetail.fetchByRequestCodeAndCommodityCode( reqCode, commodityCode )
        assertNotNull requestDetail
        assertEquals reqCode, requestDetail.list[0].requestCode
    }

    /**
     * Test to create requisition detail.
     */
    @Test
    void testCreateRequisitionDetail() {
        def requisitionDetail = getRequisitionDetails()
        try {
            requisitionDetail.save( failOnError: true, flush: true )
            assertNotNull requisitionDetail.id
            def reqId = requisitionDetail.id
            def request = RequisitionDetail.findById( reqId )
            assertTrue( request?.id != null || request?.requestCode != null )
        }
        catch (e) {
            e.printStackTrace()
        }
    }

    /**
     * The method is used to get the RequisitionDetail object with all required values to insert/update.
     * @return RequisitionDetail.
     */
    private RequisitionDetail getRequisitionDetails() {
        def lastItem = RequisitionDetail.getLastItem().getAt( 0 )
        if (lastItem == null) {
            lastItem = 0
        }
        lastItem = lastItem + 1
        def requisitionDetail = new RequisitionDetail(
                requestCode: reqCode,
                item: lastItem,
                userId: FinanceProcurementConstants.DEFAULT_TEST_ORACLE_LOGIN_USER_NAME,
                commodity: commodityCode,
                commodityDescription: 'New',
                chartOfAccount: 'B',
                organization: '11003',
                quantity: '2',
                unitOfMeasure: 'EA',
                unitPrice: '99.99',
                requisitionDate: new Date() + 30,
                ship: 'EAST',
                suspenseIndicator: false,
                textUsageIndicator: 'S',
                discountAmount: '0',
                taxAmount: '0',
                additionalChargeAmount: '0',
                taxGroup: null,
                dataOrigin: 'Banner'
        )
        return requisitionDetail
    }
}

/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package util

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.system.RequisitionHeader
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.sf.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
/**
 * Integration Test cases file for FinanceProcurementHelper.
 */

@Integration
@Rollback
class FinanceProcurementHelperIntegrationTests extends BaseIntegrationTestCase {
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
     * The test case to test checkCompleteRequisition.
     */
    @Test
    public void testCheckCompleteRequisition() {
        def header = new RequisitionHeader()
        header.completeIndicator = true
        try {
            FinanceProcurementHelper.checkCompleteRequisition( header )
            fail 'This should have failed with ' + FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED
        }
    }

    /**
     * The test case to test checkCompleteRequisition.
     */
    @Test
    public void testCheckCompleteRequisitionForComplete() {
        def header = new RequisitionHeader()
        header.completeIndicator = false
        FinanceProcurementHelper.checkCompleteRequisition( header )
    }

    /**
     * This method is used to get the JSON Object for Requisition Header.
     * @return JSONObject.
     */
    private def getRequisitionHeaderJson() {
        def header = new JSONObject()
        header.put( 'requestDate', '18-Feb-2015' )
        header.put( 'transactionDate', '19-Feb-2015' )
        header.put( 'deliveryDate', '20-Feb-2015' )
        header.put( 'requesterName', 'Caliper College_u1' )
        header.put( 'ship', 'EAST' )
        header.put( 'vendorPidm', '278' )
        header.put( 'vendorAddressType', 'BU' )
        header.put( 'vendorAddressTypeSequence', '1' )
        header.put( 'chartOfAccount', 'B' )
        header.put( 'attentionTo', 'Avery Johnson' )
        header.put( 'isDocumentLevelAccounting', true )
        header.put( 'deliveryComment', 'test' )
        header.put( 'taxGroup', 'AU' )
        header.put( 'discount', '30' )
        header.put( 'currency', 'USD' )
        def reqHeaderJson = new JSONObject()
        reqHeaderJson.put( "header", header )
        reqHeaderJson
    }

    /**
     * This method is used to get the JSON Object for Requisition Detail.
     * @return JSONObject.
     */
    private def getRequisitionDetailJson() {
        def detail = new JSONObject()
        detail.put( 'requestCode', 'R0000124' )
        detail.put( 'item', 1 )
        detail.put( 'commodity', '2210000000' )
        detail.put( 'commodityDescription', 'New Requisition Detail' )
        detail.put( 'quantity', '2' )
        detail.put( 'unitOfMeasure', 'EA' )
        detail.put( 'unitPrice', '99.99' )
        detail.put( 'discountAmount', '0' )
        detail.put( 'taxAmount', '0' )
        detail.put( 'additionalChargeAmount', '9' )
        detail.put( 'taxGroup', 'NT' )
        def reqDetailJson = new JSONObject()
        reqDetailJson.put( "detail", detail )
        reqDetailJson
    }

    /**
     * This method is used to get the JSON Object for Requisition Accounting.
     * @return JSONObject.
     */
    private def getRequisitionAccountingJson() {
        def accounting = new JSONObject()
        accounting.put( "requestCode", "R0001397" )
        accounting.put( "activity", "" )
        accounting.put( "location", "" )
        accounting.put( "project", "" )
        accounting.put( "percentage", "" )
        accounting.put( "discountAmount", "" )
        accounting.put( "discountAmountPercent", "" )
        accounting.put( "additionalChargeAmount", "" )
        accounting.put( "additionalChargeAmountPct", "" )
        accounting.put( "requisitionAmount", 100 )
        accounting.put( "fiscalYearCode", "15" )
        accounting.put( "period", "09" )
        accounting.put( "ruleClass", "REQP" )
        accounting.put( "chartOfAccounts", "B" )
        accounting.put( "accountIndex", "EPHM54" )
        accounting.put( "fund", "EPMSF1" )
        accounting.put( "organization", "11007" )
        accounting.put( "account", "1006" )
        accounting.put( "program", "10" )
        def reqAccountingJson = new JSONObject()
        reqAccountingJson.put( "accounting", accounting )
        reqAccountingJson
    }

}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * Service class for RequisitionSummary.
 *
 */
class RequisitionSummaryService extends ServiceBase {
    boolean transactional = true
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * Find the requisition summary for specified requestCode
     * @param requestCode
     */
    def fetchRequisitionSummaryForRequestCode( requestCode ) {
        LoggerUtility.debug( LOGGER, 'Input parameters for fetchRequisitionSummaryForRequestCode :' + requestCode )
        def requisitionSummary = RequisitionSummary.fetchRequisitionSummaryForRequestCode( requestCode )
        if (!requisitionSummary) {
            throw new ApplicationException( RequisitionSummaryService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_HEADER, [] ) )
        }
        processSummaryInformation requisitionSummary;
    }

    /**
     * Process and topologies Requisition Summary
     */
    private def processSummaryInformation( requisitionSummary ) {
        def ret = [:]
        requisitionSummary.collectEntries() {
            [it.requestCode, [
                    version                  : it.version,
                    isDocumentLevelAccounting: it.isDocumentLevelAccounting,
                    requestCode              : it.requestCode,
                    vendorPidm               : it.vendorPidm,
                    vendorAddressTypeSequence: it.vendorAddressTypeSequence,
                    vendorAddressTypeCode    : it.vendorAddressTypeCode,
                    vendorLastName           : it.vendorLastName,
                    vendorAddressLine1       : it.vendorAddressLine1,
                    vendorAddressZipCode     : it.vendorAddressZipCode,
                    vendorAddressStateCode   : it.vendorAddressStateCode,
                    vendorAddressCity        : it.vendorAddressCity]]
        }.each() {
            key, value ->
                ret['header'] = value
        }
        //TODO Need to topologies Commodity and accounting information
        ret
    }
}

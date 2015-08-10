/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinanceGeneralTickler domain.
 */
class FinanceGeneralTicklerService extends ServiceBase {
    /**
     * Find General Tickler by item reference number.
     * @param itemReferenceNo item reference number.
     * @return list of FinanceGeneralTickler.s
     */
    def findByReferenceNumber( itemReferenceNo ) {
        return FinanceGeneralTickler.findByReferenceNumber( itemReferenceNo )
    }
}

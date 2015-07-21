/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinancePendingApproverList domain.
 */
class FinancePendingApproverListService extends ServiceBase {
    /**
     * Method to find FinancePendingApproverList by document code.
     * @param documentCode Document code.
     * @return List of FinancePendingApproverList.
     */
    def findByDocumentCode(documentCode) {
        return FinancePendingApproverList.findByDocumentCode(documentCode)
    }
}

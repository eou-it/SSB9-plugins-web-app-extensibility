/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinanceApprovalHistory.
 */
class FinanceApprovalHistoryService extends ServiceBase {
    /**
     * Method is used to get FinanceApprovalHistory by document code.
     * @param documentCode document code.
     * @return list of FinanceApprovalHistory.
     */
    @Transactional(readOnly = true)
    def findByDocumentCode(documentCode) {
        return FinanceApprovalHistory.findByDocumentCode(documentCode)
    }
}

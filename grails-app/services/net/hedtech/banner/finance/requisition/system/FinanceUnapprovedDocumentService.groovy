/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinanceUnapprovedDocument.
 */
class FinanceUnapprovedDocumentService extends ServiceBase {
    boolean transactional = true
    /**
     * Method is used to get FinanceUnapprovedDocument by document code.
     * @param documentCode document code.
     * @return list of FinanceUnapprovedDocument.
     */
    @Transactional(readOnly = true)
    def findByDocumentCode(documentCode) {
        return FinanceUnapprovedDocument.fetchByDocumentCode(documentCode)
    }
}

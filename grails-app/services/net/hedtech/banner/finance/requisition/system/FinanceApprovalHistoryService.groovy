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
    boolean transactional = true
    /**
     * Method is used to get FinanceApprovalHistory by document code.
     * @param documentCode document code.
     * @return list of FinanceApprovalHistory.
     */
    @Transactional(readOnly = true)
    def findByDocumentCode( documentCode ) {
        return FinanceApprovalHistory.fetchByDocumentCode( documentCode )
    }

    /**
     * Finds document Approval history by document number and type
     * @param documentNumber
     * @param documentType
     * @return
     */
    @Transactional(readOnly = true)
    def findByDocumentNumberAndType( documentCode, documentType ) {
        return FinanceApprovalHistory.fetchByDocumentCodeAndDocType( documentCode, documentType as long )
    }
}

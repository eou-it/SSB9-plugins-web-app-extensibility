/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinanceApprovalsInProcess.
 */
class FinanceApprovalsInProcessService extends ServiceBase {
    boolean transactional = true
    /**
     * Method is used to find document FinanceApprovalInProcess by document number.
     * @param documentNumber document number.
     * @return List of FinanceApprovalInProcess.
     */
    @Transactional(readOnly = true)
    def findByDocumentNumber( documentNumber ) {
        return FinanceApprovalsInProcess.fetchByDocumentNumber( documentNumber )
    }

    /**
     * Finds document Approval in process by document number and type
     * @param documentNumber
     * @param documentType
     * @return
     */
    @Transactional(readOnly = true)
    def findByDocumentNumberAndType( documentNumber, documentType ) {
        return FinanceApprovalsInProcess.fetchByDocumentCodeAndDocType( documentNumber, documentType as long )
    }

}

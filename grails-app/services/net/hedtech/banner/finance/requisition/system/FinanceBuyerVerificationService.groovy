/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinanceBuyerVerification domain.
 */
class FinanceBuyerVerificationService extends ServiceBase {
    /**
     * Method to find buyer verification by request code.
     * @param requestCode Request code.
     * @return List of FinanceBuyerVerification
     */
    def findByDocumentCode(requestCode) {
        return FinanceBuyerVerification.findByDocumentCode(requestCode)
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

/**
 * Service class for FinanceRequestPOVerification domain.
 */
class FinanceRequestPOVerificationService {
    /**
     * Find FinanceRequestPOVerification by request code.
     * @param requestCode Requisition code.
     * @return list of FinanceRequestPOVerification.
     */
    def findByRequestCode(requestCode) {
        return FinanceRequestPOVerification.fetchByRequestCode(requestCode)
    }
}

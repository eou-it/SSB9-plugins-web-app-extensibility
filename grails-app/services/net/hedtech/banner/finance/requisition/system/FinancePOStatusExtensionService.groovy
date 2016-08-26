package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional

/**
 * Service class for FinancePOStatusExtension domain.
 */
class FinancePOStatusExtensionService {

    /**
     * Find FinancePOStatusExtension by pohd code.
     * @param pohdCode POHD code.
     * @return list of FinancePOStatusExtension.
     */
    def findByPOHDCode(pohdCode) {
        return FinancePOStatusExtension.fetchByPOHDCode(pohdCode)
    }
}

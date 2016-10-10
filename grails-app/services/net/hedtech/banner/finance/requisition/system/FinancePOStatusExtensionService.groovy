/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.service.ServiceBase

/**
 * Service class for FinancePOStatusExtension domain.
 */
class FinancePOStatusExtensionService extends ServiceBase {

    boolean transactional = true

    /**
     * Find FinancePOStatusExtension by pohd code.
     * @param pohdCode POHD code.
     * @return list of FinancePOStatusExtension.
     */
    @Transactional(readOnly = true)
    def findByPOHDCode(pohdCode) {
        return FinancePOStatusExtension.fetchByPOHDCode(pohdCode)
    }
}

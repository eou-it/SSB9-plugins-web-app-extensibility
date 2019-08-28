/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.service.ServiceBase

/**
 * Service class for RequisitionAccounting For Copy .
 *
 */
class RequisitionAccountingForCopyService extends ServiceBase {

    /**
     * Validate method for delete
     * @param map
     */
    def preDelete( map ) {
        log.debug('{}', map )
        throw new ApplicationException( RequisitionAccountingForCopy, FinanceValidationConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }

    /**
     * Validate method for update
     * @param map
     */
    def preUpdate( map ) {
        log.debug('{}', map )
        throw new ApplicationException( RequisitionAccountingForCopy, FinanceValidationConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }
}

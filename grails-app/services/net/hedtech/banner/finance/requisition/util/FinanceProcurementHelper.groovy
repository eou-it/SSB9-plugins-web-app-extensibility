/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.util

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.springframework.context.i18n.LocaleContextHolder

import java.text.NumberFormat

/**
 * Helper Class for Finance Procurement.
 */
class FinanceProcurementHelper {

    /**
     * Checks if requisition is already complete
     * @param requisitionHeader
     */
    static checkCompleteRequisition( requisitionHeader ) {
        if (requisitionHeader.completeIndicator) {
            throw new ApplicationException(
                    FinanceProcurementHelper,
                    new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED, [requisitionHeader.requestCode] ) )
        }
    }

    /**
     * Get Locale based formatted number
     * @param amount
     * @param fractionDigits
     * @return
     */
    static getLocaleBasedFormattedNumber( amount, fractionDigits ) {
        amount = amount ?: 0.0
        Locale fmtLocale = LocaleContextHolder.getLocale()
        NumberFormat formatter = NumberFormat.getInstance( fmtLocale );
        formatter.setMaximumFractionDigits( fractionDigits );
        formatter.setMinimumFractionDigits( fractionDigits );
        formatter.format( amount )
    }
}

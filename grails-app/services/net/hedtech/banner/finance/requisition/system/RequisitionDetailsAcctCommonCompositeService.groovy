/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Details and Accounting Composite Service
 */
class RequisitionDetailsAcctCommonCompositeService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    def requisitionDetailService
    boolean transactional = true

    /**
     * Adjusts Account Percentage And Amount
     * @param requisitionAccountingRequest
     * @return
     */
    public def adjustAccountPercentageAndAmount( RequisitionAccounting requisitionAccountingRequest ) {
        def requisitionDetail
        def orgPercentage = requisitionAccountingRequest.percentage
        def adjustedPercentage = orgPercentage;
        if (requisitionAccountingRequest.item == FinanceProcurementConstants.ZERO) {//DLA
            requisitionDetail = requisitionDetailService.findByRequestCode( requisitionAccountingRequest.requestCode )
        } else {
            requisitionDetail = requisitionDetailService.findByRequestCodeAndItem( requisitionAccountingRequest.requestCode, requisitionAccountingRequest.item )
        }
        def totalExtendedCommodity = 0.0, totalTax = 0.0, totalDiscount = 0.0, totalAdditionalCharge = 0.0
        requisitionDetail.each {
            totalExtendedCommodity += (it.quantity * it.unitPrice).setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            BigDecimal taxAmount = it.taxAmount ? it.taxAmount : new BigDecimal( FinanceProcurementConstants.ZERO )
            totalTax += taxAmount.setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            BigDecimal discount = it.discountAmount ? it.discountAmount : new BigDecimal( FinanceProcurementConstants.ZERO )
            totalDiscount += discount.setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
            BigDecimal additionalCharge = it.additionalChargeAmount ? it.additionalChargeAmount : new BigDecimal( FinanceProcurementConstants.ZERO )
            totalAdditionalCharge += additionalCharge.setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP )
        }


        boolean isAdjustmentNeededForTax = totalTax != ((totalTax * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalTax * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))
        boolean isAdjustmentNeededForDiscount = totalDiscount != ((totalDiscount * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalDiscount * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))

        boolean isAdjustmentNeededForAdditionalCharge = totalAdditionalCharge != ((totalAdditionalCharge * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalAdditionalCharge * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))

        boolean isAdjustmentNeededForExtendedAmount = totalExtendedCommodity != ((totalExtendedCommodity * orgPercentage / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ) + (totalExtendedCommodity * (FinanceProcurementConstants.HUNDRED - orgPercentage) / FinanceProcurementConstants.HUNDRED)
                .setScale( FinanceProcurementConstants.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP ))

        // Check If at least one of them is true, and also find out what the adjusted % value is going to be.
        if (isAdjustmentNeededForExtendedAmount) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalExtendedCommodity * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION_PERCENTAGE, BigDecimal.ROUND_HALF_UP )) / totalExtendedCommodity
        } else if (isAdjustmentNeededForTax) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalTax * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION_PERCENTAGE, BigDecimal.ROUND_HALF_UP )) / totalTax
        } else if (isAdjustmentNeededForDiscount) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalDiscount * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION_PERCENTAGE, BigDecimal.ROUND_HALF_UP )) / totalDiscount
        } else if (isAdjustmentNeededForAdditionalCharge) {
            adjustedPercentage = (FinanceProcurementConstants.HUNDRED * (totalAdditionalCharge * orgPercentage / FinanceProcurementConstants.HUNDRED)
                    .setScale( FinanceProcurementConstants.DECIMAL_PRECISION_PERCENTAGE, BigDecimal.ROUND_HALF_UP )) / totalAdditionalCharge
        }
        LoggerUtility.debug( LOGGER, 'adjustedPercentage before scaling  : ' + adjustedPercentage )

        adjustedPercentage = adjustedPercentage.setScale( FinanceProcurementConstants.DECIMAL_PRECISION_PERCENTAGE, BigDecimal.ROUND_HALF_UP )
        LoggerUtility.debug( LOGGER, 'adjustedPercentage after scaling : ' + adjustedPercentage )

        requisitionAccountingRequest.percentage = adjustedPercentage
        requisitionAccountingRequest.discountAmountPercent = adjustedPercentage
        requisitionAccountingRequest.additionalChargeAmountPct = adjustedPercentage
        requisitionAccountingRequest.taxAmountPercent = adjustedPercentage
    }
}

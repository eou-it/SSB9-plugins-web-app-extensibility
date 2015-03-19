/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.util

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

/**
 * Helper Class for Finance Procurement.
 */
class FinanceProcurementHelper {

    /**
     * This method used to get the Requisition Header domain model from the request.
     * @param requisitionHeaderJSON Requisition Header info from the request.
     * @return RequisitionHeader domain.
     */
    static def getHeaderDomainModel( requisitionHeaderJSON ) {
        return [
                requestCode              : FinanceProcurementConstants.DEFAULT_REQUEST_CODE,
                item                     : requisitionHeaderJSON.item ? requisitionHeaderJSON.item : null,
                requestDate              : new Date( requisitionHeaderJSON.requestDate ),
                transactionDate          : new Date( requisitionHeaderJSON.transactionDate ),
                requesterName            : requisitionHeaderJSON.requesterName,
                ship                     : requisitionHeaderJSON.ship,
                requesterPhoneNumber     : requisitionHeaderJSON.requesterPhoneNumber,
                requesterPhoneArea       : requisitionHeaderJSON.requesterPhoneArea,
                requesterPhoneExtension  : requisitionHeaderJSON.requesterPhoneExtension,
                vendorPidm               : requisitionHeaderJSON.vendorPidm,
                vendorAddressType        : requisitionHeaderJSON.vendorAddressType,
                vendorAddressTypeSequence: requisitionHeaderJSON.vendorAddressTypeSequence,
                chartOfAccount           : requisitionHeaderJSON.chartOfAccount,
                organization             : requisitionHeaderJSON.organization,
                attentionTo              : requisitionHeaderJSON.attentionTo,
                isDocumentLevelAccounting: requisitionHeaderJSON.isDocumentLevelAccounting,
                requestTypeIndicator     : 'P',
                matchRequired            : requisitionHeaderJSON.matchRequired,
                requesterFaxArea         : requisitionHeaderJSON.requesterFaxArea,
                requesterFaxNumber       : requisitionHeaderJSON.requesterFaxNumber,
                requesterFaxExtension    : requisitionHeaderJSON.requesterFaxExtension,
                requesterFaxCountry      : requisitionHeaderJSON.requesterFaxCountry,
                requesterEmailAddress    : requisitionHeaderJSON.requesterEmailAddress,
                deliveryComment          : requisitionHeaderJSON.deliveryComment,
                taxGroup                 : requisitionHeaderJSON.taxGroup,
                discount                 : requisitionHeaderJSON.discount,
                currency                 : requisitionHeaderJSON.currency,
                vendorContact            : requisitionHeaderJSON.vendorContact,
                vendorEmailAddress       : requisitionHeaderJSON.vendorEmailAddress,
                requisitionOrigination   : FinanceProcurementConstants.DEFAULT_REQUISITION_ORIGIN
        ]
    }

    /**
     * This method used to get the Requisition Detail domain model from the request.
     * @param requisitionHeaderJSON Requisition Detail info from the request.
     * @return RequisitionDetail domain.
     */
    static def getDetailDomainModel( requisitionDetailJSON ) {
        return [
                requestCode           : requisitionDetailJSON.requestCode,
                item                  : requisitionDetailJSON.item,
                commodity             : requisitionDetailJSON.commodity,
                commodityDescription  : requisitionDetailJSON.commodityDescription,
                quantity              : requisitionDetailJSON.quantity,
                unitOfMeasure         : requisitionDetailJSON.unitOfMeasure,
                unitPrice             : requisitionDetailJSON.unitPrice,
                suspenseIndicator     : true,
                textUsageIndicator    : FinanceProcurementConstants.DEFAULT_FPBREQD_TEXT_USAGE,
                discountAmount        : requisitionDetailJSON.discountAmount,
                taxAmount             : requisitionDetailJSON.taxAmount,
                additionalChargeAmount: requisitionDetailJSON.additionalChargeAmount,
                taxGroup              : requisitionDetailJSON.taxGroup,
                dataOrigin            : FinanceProcurementConstants.DEFAULT_REQUISITION_ORIGIN
        ]
    }

    /**
     * This method used to get the Requisition Accounting domain model from the request.
     * @param requisitionHeaderJSON Requisition Accounting info from the request.
     * @return RequisitionAccounting domain.
     */
    static def getAccountingDomainModel( requisitionAccountingJSON ) {
        return [
                requestCode                       : requisitionAccountingJSON.requestCode,
                item                              : requisitionAccountingJSON.item,
                sequenceNumber                    : requisitionAccountingJSON.sequenceNumber,
                activity                          : requisitionAccountingJSON.activity,
                location                          : requisitionAccountingJSON.location,
                project                           : requisitionAccountingJSON.project,
                percentage                        : requisitionAccountingJSON.percentage,
                discountAmount                    : requisitionAccountingJSON.discountAmount,
                discountAmountPercent             : requisitionAccountingJSON.discountAmountPercent,
                additionalChargeAmount            : requisitionAccountingJSON.additionalChargeAmount,
                additionalChargeAmountPct         : requisitionAccountingJSON.additionalChargeAmountPct,
                requisitionAmount                 : requisitionAccountingJSON.requisitionAmount,
                fiscalYearCode                    : requisitionAccountingJSON.fiscalYearCode,
                period                            : requisitionAccountingJSON.period,
                ruleClass                         : requisitionAccountingJSON.ruleClass,
                chartOfAccounts                   : requisitionAccountingJSON.chartOfAccounts,
                accountIndex                      : requisitionAccountingJSON.accountIndex,
                fund                              : requisitionAccountingJSON.fund,
                organization                      : requisitionAccountingJSON.organization,
                account                           : requisitionAccountingJSON.account,
                program                           : requisitionAccountingJSON.program,
                insufficientFundsOverrideIndicator: FinanceProcurementConstants.TRUE
        ]
    }

    /**
     * Gets the domain model from JSON
     * @param inputJSON
     * @return an object
     */
    static def getDomainModel( inputJSON ) {
        if (inputJSON.header) {
            def headerDomainModel = getHeaderDomainModel( inputJSON.header )
            if (inputJSON.header.deliveryDate) {
                headerDomainModel.deliveryDate = new Date( inputJSON.header.deliveryDate )
            }
            return [requisitionHeader: headerDomainModel]
        } else if (inputJSON.detail) {
            return [requisitionDetail: getDetailDomainModel( inputJSON.detail )]
        } else if (inputJSON.accounting) {
            return [requisitionAccounting: getAccountingDomainModel( inputJSON.accounting )]
        }
    }
}

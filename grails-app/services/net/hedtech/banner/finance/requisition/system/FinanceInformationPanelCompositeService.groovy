/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

/**
 * Composite service for Information panel.
 */
class FinanceInformationPanelCompositeService {
    def financeGeneralTicklerService
    def financePendingApproverListService
    def financeBuyerVerificationService
    def financeRequestPOVerificationService

    /**
     * Method to get information panel data by requisition status and requisition code.
     * @param status Requisition status.
     * @param requestCode Requisition Code.
     * @return information panel data.
     */
    def getInformationPanelData(status, requestCode) {
        def informationPanelData = []
        if (status == FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED) {
            def comment = (financeGeneralTicklerService.findByReferenceNumber(requestCode)
                    ? financeGeneralTicklerService.findByReferenceNumber(requestCode)
                    : FinanceProcurementConstants.EMPTY_STRING)
            informationPanelData = comment
        } else if (status == FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING) {
            informationPanelData = (financePendingApproverListService.findByDocumentCode(requestCode)
                    ? financePendingApproverListService.findByDocumentCode(requestCode) : [])
        } else if (status == FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER) {
            def list = financeBuyerVerificationService.findByDocumentCode(requestCode).collect { FinanceBuyerVerification buyerVerification ->
                [
                        buyerCode: buyerVerification.buyerCode,
                        buyerName: buyerVerification.buyerName
                ]
            }
            informationPanelData = (list ? list : [])
        } else if (status == FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO) {
            def list = financeRequestPOVerificationService.findByRequestCode(requestCode).collect {
                [pohdCode: it.pohdCode]
            }
            informationPanelData = (list ? list : [])
        }
        return informationPanelData
    }
}

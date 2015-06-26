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
        def informationPanelData
        switch (status) {
            case FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED:
                def comments = financeGeneralTicklerService.findByReferenceNumber(requestCode).collect {
                    [comment: it.comment]
                }
                informationPanelData = (comments ? comments : [])
                break
            case FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING:
                def list = financePendingApproverListService.findByDocumentCode(requestCode).collect {
                    [approverName: it.id.approverName,
                     description : it.description,
                     queueId     : it.id.queueId]
                }
                informationPanelData = (list ? list : [])
                break
            case FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER:
                def list = financeBuyerVerificationService.findByDocumentCode(requestCode).collect {
                    [buyerCode: it.buyerCode,
                     buyerName: it.buyerName]
                }
                informationPanelData = (list ? list : [])
                break
            case FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO:
                def list = financeRequestPOVerificationService.findByRequestCode(requestCode).collect {
                    [pohdCode: it.pohdCode]
                }
                informationPanelData = (list ? list : [])
                break
            default:
                informationPanelData = []

        }
        return informationPanelData
    }
}

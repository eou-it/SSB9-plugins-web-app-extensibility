/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.i18n.MessageHelper
/**
 * Composite service for Information panel.
 */
class FinanceInformationPanelCompositeService {
    def financeGeneralTicklerService
    def financePendingApproverListService
    def financeBuyerVerificationService
    def financeRequestPOVerificationService
    def financePOStatusExtensionService

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
                    [buyerCode: it.id.buyerCode,
                     buyerName: it.id.buyerName]
                }
                informationPanelData = (list ? list : [])
                break
            case FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO:
                def list = financeRequestPOVerificationService.findByRequestCode(requestCode).collect {
                    [pohdCode: it]
                }
                if(list) {
                    def msgStr = getStatusExtension(list.get(0).pohdCode)
                    list.get(0).put('pohdCode', list.get(0).pohdCode + msgStr)
                }
                informationPanelData = (list ? list : [])

                break
            default:
                informationPanelData = []

        }
        return informationPanelData
    }

    def getStatusExtension(def pohdCode){

        def msgrStr = ""

        if(pohdCode == null || pohdCode.trim().equals("")){
            return msgrStr;
        }

        def list = financePOStatusExtensionService.findByPOHDCode(pohdCode)

        if((list?.get(0)[0]?.equals('N') || list?.get(0)[0]?.equals('n')) && (list?.get(0)[1]?.equals('N') || list?.get(0)[1]?.equals('n'))){
            msgrStr = ' '+ MessageHelper.message( code:  FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_CONVERTED_TO_PO_DRAFT )
        }else if((list?.get(0)[0]?.equals('N') || list?.get(0)[0]?.equals('n')) && (list?.get(0)[1]?.equals('Y') || list?.get(0)[1]?.equals('y'))){
            msgrStr = ' '+ MessageHelper.message( code:  FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_CONVERTED_TO_PO_PENDING )
        }else if((list?.get(0)[0]?.equals('Y') || list?.get(0)[0]?.equals('y')) && (list?.get(0)[1]?.equals('Y') || list?.get(0)[1]?.equals('y'))){
            msgrStr = ' '+ MessageHelper.message( code:  FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_CONVERTED_TO_PO_COMPLETED )
        }else{
            msgrStr = ''
        }

        return msgrStr;
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger

/**
 * The service class which is used to have methods for copy purchase requisition.
 */
class CopyPurchaseRequisitionCompositeService {
    private static final def LOGGER = Logger.getLogger(this.getClass())
    boolean transactional = true

    def springSecurityService
    def requisitionHeaderService
    def requisitionDetailService
    def requisitionAccountingService
    def requisitionInformationCompositeService

    /**
     * This method is used to copy the requisition.
     */
    def copyRequisition(requestCode, institutionBaseCcy) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        def detailsList = requisitionDetailService.findByRequestCode(requestCode)
        def accountingList = requisitionAccountingService.findAccountingByRequestCode(requestCode)
        if (header && header.completeIndicator) {
            // Header.
            RequisitionHeader headerCopy = new RequisitionHeader()
            FinanceCommonUtility.copyProperties(header, headerCopy)
            headerCopy.completeIndicator = false
            headerCopy.requestCode = FinanceProcurementConstants.DEFAULT_REQUEST_CODE
            headerCopy.documentCopiedFrom = requestCode
            headerCopy.deliveryComment = FinanceProcurementConstants.COPY_REQUISITION_HEADER_COPIED_FROM + requestCode
            RequisitionHeader requisitionHeader = requisitionHeaderService.create([domainModel: headerCopy])
            LoggerUtility.debug LOGGER, "Requisition Header created " + requisitionHeader
            RequisitionHeader copy = requisitionHeader
            // Details.
            detailsList.each { detail ->
                RequisitionDetail detailsCopy = new RequisitionDetail()
                FinanceCommonUtility.copyProperties(detail, detailsCopy)
                detailsCopy.requestCode = copy.requestCode
                detailsCopy.id = null
                detailsCopy.completeIndicator = FinanceProcurementConstants.DEFAULT_INDICATOR_NO
                requisitionDetailService.create([domainModel: detailsCopy])
            }
            // Accounting.
            accountingList.each { accounting ->
                RequisitionAccounting accountingCopy = new RequisitionAccounting()
                FinanceCommonUtility.copyProperties(accounting, accountingCopy)
                accountingCopy.requestCode = copy.requestCode
                accountingCopy.id = null
                requisitionAccountingService.create([domainModel: accountingCopy])
            }
            return requisitionInformationCompositeService.fetchPurchaseRequisition(copy.requestCode, institutionBaseCcy)
        } else {
            LoggerUtility.error(LOGGER, 'Only completed requisition can be copied=' + header.requestCode)
            throw new ApplicationException(
                    CopyPurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED, []))
        }
    }
}

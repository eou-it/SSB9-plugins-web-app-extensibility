/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.commons.beanutils.BeanUtils
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Class for Purchase Requisition Header Composite Service
 */
class RequisitionHeaderCompositeService {
    private static final Logger LOGGER = Logger.getLogger(this.class)
    boolean transactional = true

    def requisitionHeaderService
    def requisitionAccountingService
    def springSecurityService
    def financeSystemControlService
    def institutionalDescriptionService
    def financeTextService
    def financeTextCompositeService

    /**
     * Create purchase requisition Header
     * @param map Map which contains the RequisitionHeader domain with values.
     * @return Requisition code.
     */
    def createPurchaseRequisitionHeader(map) {
        RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
        def user = springSecurityService.getAuthentication()?.user
        if (user.oracleUserName) {
            def oracleUserName = user?.oracleUserName
            requisitionHeaderRequest.userId = oracleUserName
            // Check for tax group
            if (financeSystemControlService.findActiveFinanceSystemControl()?.taxProcessingIndicator == FinanceValidationConstants.REQUISITION_INDICATOR_NO) {
                requisitionHeaderRequest.taxGroup = null
            }
            def requisitionHeader = requisitionHeaderService.create([domainModel: requisitionHeaderRequest])
            LoggerUtility.debug LOGGER, "Requisition Header created " + requisitionHeader
            def header = RequisitionHeader.read(requisitionHeader.id)
            financeTextCompositeService.saveTextForHeader(requisitionHeader,
                    [privateComment: map.requisitionHeader.privateComment, publicComment: map.requisitionHeader.publicComment],
                    user.oracleUserName)
            return header.requestCode
        } else {
            LoggerUtility.error(LOGGER, 'User' + user + ' is not valid')
            throw new ApplicationException(
                    RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException(FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, []))
        }
    }

    /**
     * Delete Purchase Requisition
     * @param requestCode
     */
    def deletePurchaseRequisition(requestCode) {
        def requisitionHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        FinanceProcurementHelper.checkCompleteRequisition(requisitionHeader)
        requisitionHeaderService.delete([domainModel: requisitionHeader])
        financeTextService.delete(financeTextService.listAllFinanceTextByCode(requestCode))
    }

    /**
     * Update Purchase requisition
     *
     * @param map the requisition map
     * @param requestCode
     */
    @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
    def updateRequisitionHeader(map, requestCode) {
        // Update header
        def existingHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        FinanceProcurementHelper.checkCompleteRequisition(existingHeader)
        def user = springSecurityService.getAuthentication()?.user
        if (map?.requisitionHeader && user?.oracleUserName) {
            RequisitionHeader requisitionHeaderRequest = map.requisitionHeader
            requisitionHeaderRequest.id = existingHeader.id
            requisitionHeaderRequest.version = existingHeader.version
            requisitionHeaderRequest.requestCode = existingHeader.requestCode
            if (requisitionHeaderRequest.isDocumentLevelAccounting != existingHeader.isDocumentLevelAccounting) {

                if (requisitionAccountingService.findAccountingSizeByRequestCode(existingHeader.requestCode) > 0) {
                    LoggerUtility.error(LOGGER, 'Document type cannot be modified once accounting associated with this')
                    throw new ApplicationException(RequisitionHeaderCompositeService,
                            new BusinessLogicValidationException(
                                    FinanceProcurementConstants.ERROR_MESSAGE_DOCUMENT_CHANGE, []))
                }
            }
            requisitionHeaderRequest.requestDate = existingHeader.requestDate
            requisitionHeaderRequest.userId = user?.oracleUserName
            def requisitionHeader = requisitionHeaderService.update([domainModel: requisitionHeaderRequest])
            LoggerUtility.debug LOGGER, "Requisition Header updated " + requisitionHeader
            financeTextCompositeService.saveTextForHeader(requisitionHeader,
                    [privateComment: map.requisitionHeader.privateComment, publicComment: map.requisitionHeader.publicComment],
                    user.oracleUserName)
            return requisitionHeader
        } else {
            LoggerUtility.error(LOGGER, 'User' + user + ' is not valid')
            throw new ApplicationException(RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, []))
        }
    }

    /**
     * Get Filtered CCY information
     * @param ccyCode
     * @return
     */
    def getCurrencyDetailByReqCode(requestCode) {
        def currencyCode = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode).currency
        if (!currencyCode) {
            currencyCode = institutionalDescriptionService.findByKey().baseCurrCode
        }
        return currencyCode
    }

    /**
     * Service Method to recall a purchase requisition.
     * @param requestCode request code.
     */
    def recallRequisition(requestCode) {
        def requestHeader = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        if (requestHeader && requestHeader.completeIndicator && !requestHeader.approvalIndicator) {
            requestHeader.completeIndicator = false
            RequisitionHeader requestHeaderUpdated = requisitionHeaderService.update([domainModel: requestHeader])
            return requestHeaderUpdated.requestCode
        } else {
            LoggerUtility.error(LOGGER, 'Only pending requisition can be recalled=' + requestCode)
            throw new ApplicationException(
                    RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_RECALL_REQUISITION_PENDING_REQ_IS_REQUIRED, []))
        }
    }

}

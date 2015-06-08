/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementHelper
import net.hedtech.banner.finance.system.FinanceText
import net.hedtech.banner.finance.util.LoggerUtility
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
            saveHeaderLevelText(map, requisitionHeader, user, true)
            return header.requestCode
        } else {
            LoggerUtility.error(LOGGER, 'User' + user + ' is not valid')
            throw new ApplicationException(
                    RequisitionHeaderCompositeService,
                    new BusinessLogicValidationException(FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, []))
        }
    }

    private void saveHeaderLevelText(map, requisitionHeader, user, save) {
        if (map.requisitionHeader.privateComment) {
            saveFinanceText(requisitionHeader, [privateComment: map.requisitionHeader.privateComment], user.oracleUserName, save)
        }
        if (map.requisitionHeader.publicComment) {
            saveFinanceText(requisitionHeader, [publicComment: map.requisitionHeader.publicComment], user.oracleUserName, save)
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
            saveHeaderLevelText(map, existingHeader, user, false)
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
     * The private method used to save a Finance Text when Header Save or Update.
     * @param header Requisition Header.
     * @param map Map which contains raw information regarding header.
     * @param user Logged in user.
     * @param save Boolean flag whether Save or Update (true = Save).
     */
    private void saveFinanceText(header, map, user, save) {
        // Save/Update Text Start.
        FinanceText financeText = new FinanceText()
        financeText.textCode = header.requestCode
        financeText.text = map.publicComment ?
                map.publicComment : map.privateComment
        financeText.printOptionIndicator = map.privateComment ?
                FinanceValidationConstants.REQUISITION_INDICATOR_YES :
                FinanceValidationConstants.REQUISITION_INDICATOR_NO
        financeText.activityDate = header.lastModified
        financeText.changeSequenceNumber = null
        financeText.lastModifiedBy = user
        financeText.dataOrigin = header.dataOrigin
        financeText.documentTypeSequenceNumber = FinanceValidationConstants.FINANCE_TEXT_DOCUMENT_TYPE_SEQ_NUMBER_REQUISITION
        financeText.pidm = header.vendorPidm
        financeText.textItem = null
        if (save) {
            financeTextService.saveText(financeText)
        } else {
            financeTextService.updateText(financeText, null, financeText.printOptionIndicator)
        }
        // Save/Update Text End.
    }
}

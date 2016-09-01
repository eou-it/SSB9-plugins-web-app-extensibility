/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.commons.collections.CollectionUtils


/**
 * Service class for FinanceText.
 */
class FinanceTextService extends ServiceBase {
    static transactional = true

    /**
     * Method is used to get FinanceText by text code and sequence number.
     * @param textCode text code.
     * @param commodity item number.
     * @return FinanceText.
     */
    @Transactional(readOnly = true)
    def getFinanceTextByCodeAndItemAndPrintOption(textCode, item, printOptionIndicator) {
        return FinanceText.getFinanceTextByCodeAndItemNumberAndPrintInd(textCode, item, printOptionIndicator)
    }

    /**
     * Method is used to get FinanceText by text code and sequence number.
     * @param dtypSeqNum
     * @param textCode text code.
     * @printOptionIndicator
     * @return FinanceText concatenated
     */
    @Transactional(readOnly = true)
    def getFinanceTextByDocumentTypeAndCodeAndPrintOption(dtypSeqNum, textCode, printOptionIndicator) {

       def comment = FinanceProcurementConstants.EMPTY_STRING
       FinanceText.getFinanceTextByDocumentTypeAndCodeAndPrintInd( dtypSeqNum,textCode, printOptionIndicator).each {
           comment = comment + (it.text ? it.text : FinanceProcurementConstants.EMPTY_STRING)
       }
        return ["comment": comment]

    }


    /**
     * Method is used to get Header FinanceText by text code.
     * @param textCode Text code.
     * @param printOptionIndicator Print option indicator.
     * @return list of finance text.
     */
    @Transactional(readOnly = true)
    def listHeaderLevelTextByCodeAndPrintOptionInd(textCode, printOptionIndicator) {
        return FinanceText.listHeaderLevelTextByCodeAndPrintOptionInd(textCode, printOptionIndicator)
    }

    /**
     * Method is used to list header level text by text code.
     * @param textCode text code.
     * @return list of finance text.
     */
    @Transactional(readOnly = true)
    def listHeaderLevelTextByCode(textCode) {
        return FinanceText.listHeaderLevelTextByCode(textCode)
    }

    /**
     * Method is used to get finance text by code and sequence number.
     * @param textCode text code.
     * @param item item number.
     * @return list of finance text.
     */
    @Transactional(readOnly = true)
    def getFinanceTextByCodeAndItemNumber(textCode, Integer item) {
        return FinanceText.getFinanceTextByCodeAndItemNumber(textCode, item)
    }

    /**
     * Method is used to get all finance text by text code.
     * @param textCode text code.
     * @return list of FinanceText.
     */
    @Transactional(readOnly = true)
    def listAllFinanceTextByCode(textCode) {
        return FinanceText.listAllFinanceTextByCode(textCode)
    }

}

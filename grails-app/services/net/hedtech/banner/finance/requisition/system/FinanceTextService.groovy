/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional

import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * Service class for FinanceText.
 */
class FinanceTextService extends ServiceBase {
    static transactional = true
    private static final def LOGGER = Logger.getLogger(this.getClass())

    /**
     * Method is used to get FinanceText by text code and sequence number.
     * @param textCode text code.
     * @param commodity item number.
     * @return FinanceText.
     */
    @Transactional(readOnly = true)
    def getFinanceTextByCodeAndItemAndPrintOption(textCode, item, printOptionIndicator) {
        return FinanceText.getFinanceTextByCodeAndSeqNumberAndPrintInd(textCode, item, printOptionIndicator)
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

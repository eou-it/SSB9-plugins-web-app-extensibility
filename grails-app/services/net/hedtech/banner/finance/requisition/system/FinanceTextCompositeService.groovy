/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.system.FinanceText
import grails.gorm.transactions.Transactional
/**
 * Composite service class for FinanceText.
 */

class FinanceTextCompositeService {

def financeTextService    /**
     * Method is used to save text for header.
     * @param header requisition header.
     * @param map map which having comments.
     * @param user User information.
     * @return
     */
    public saveTextForHeader( header, map, user ) {
        def listToSave = []
        def listToDelete = []
        financeTextService.listHeaderLevelTextByCode( 1, header.requestCode ).each {FinanceText financeTextToDelete ->
            listToDelete << financeTextToDelete
        }
        if (listToDelete.size() > 0) {
            financeTextService.delete( listToDelete, true )
        }
        if (map?.privateComment) {
            splitAndGetTextList( map.privateComment ).eachWithIndex {def textPart, int index ->
                prepareTextList( textPart, FinanceValidationConstants.REQUISITION_INDICATOR_NO, null, header, user, listToSave )
            }
        }
        if (map?.publicComment) {
            splitAndGetTextList( map.publicComment ).eachWithIndex {def textPart, int index ->
                prepareTextList( textPart, FinanceValidationConstants.REQUISITION_INDICATOR_YES, null, header, user, listToSave )
            }
        }
        listToSave.eachWithIndex {FinanceText entry, int i ->
            entry.sequenceNumber = (i + 1) * FinanceProcurementConstants.FINANCE_TEXT_SEQUENCE_NUMBER_INCREMENT
        }
        financeTextService.create(listToSave)

    }

    /**
     * Method is used to save text for commodity.
     * @param detail requisition detail.
     * @param map map having private and public comments.
     * @param user User information.
     * @param item item number.
     * @return
     */
    public saveTextForCommodity( detail, map, user, Integer item ) {
        def listToSave = []
        def listToDelete = []
        financeTextService.getFinanceTextByCodeAndItemNumber( 1, detail.requestCode, item ).each {FinanceText financeTextToDelete ->
            listToDelete << financeTextToDelete
        }
        financeTextService.delete( listToDelete, true )
        if (map?.privateComment) {
            splitAndGetTextList( map.privateComment ).eachWithIndex {def textPart, int index ->
                prepareTextList( textPart, FinanceValidationConstants.REQUISITION_INDICATOR_NO, item, detail, user, listToSave )
            }
        }
        if (map?.publicComment) {
            splitAndGetTextList( map.publicComment ).eachWithIndex {def textPart, int index ->
                prepareTextList( textPart, FinanceValidationConstants.REQUISITION_INDICATOR_YES, item, detail, user, listToSave )
            }
        }
        listToSave.eachWithIndex {FinanceText entry, int i ->
            entry.sequenceNumber = (i + 1) * FinanceProcurementConstants.FINANCE_TEXT_SEQUENCE_NUMBER_INCREMENT
        }
        financeTextService.create( listToSave ,false)
    }

    /**
     * Method is used to prepare text list for header/detail.
     * @param textPart Text part.
     * @param printOptionIndicator print option indicator.
     * @param textItem Text item.
     * @param headerOrDetail Header/Detail.
     * @param user User information.
     * @param listToSave List of finance text to save.
     */
    private void prepareTextList( textPart, printOptionIndicator, textItem, headerOrDetail, user, listToSave ) {
        FinanceText financeText = prepareFinanceTextForSave( headerOrDetail, user )
        financeText.text = textPart
        financeText.printOptionIndicator = printOptionIndicator
        financeText.textItem = textItem
        listToSave << financeText
    }

    /**
     * Method is used to prepare a finance text for saving header/commodity level comments.
     * @param headerOrDetail header or detail.
     * @param user user information.
     * @return
     */
    private FinanceText prepareFinanceTextForSave( headerOrDetail, user ) {
        FinanceText financeText = new FinanceText()
        financeText.textCode = headerOrDetail.requestCode
        financeText.activityDate = headerOrDetail.lastModified
        financeText.changeSequenceNumber = null
        financeText.lastModifiedBy = user
        financeText.dataOrigin = headerOrDetail.dataOrigin
        financeText.documentTypeSequenceNumber = FinanceProcurementConstants.FINANCE_TEXT_DOCUMENT_TYPE_SEQ_NUMBER_REQUISITION
        financeText.pidm = headerOrDetail.vendorPidm
        financeText
    }

    /**
     * Method is used to split and get text list.
     * @param text Finance text.
     * @return List of splitted the text.
     */
    private static final splitAndGetTextList( text ) {
        return text.toList().collate( FinanceProcurementConstants.FINANCE_TEXT_TEXT_LENGTH )*.join()
    }
}

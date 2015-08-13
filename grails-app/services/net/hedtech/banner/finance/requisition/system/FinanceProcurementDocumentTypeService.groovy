/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

import java.sql.SQLException

/**
 * This service helps to get document type for BDM attachments
 */
class FinanceProcurementDocumentTypeService extends ServiceBase {

    def LOGGER = Logger.getLogger( FinanceProcurementDocumentTypeService.name )

    /**
     * Gets doc types
     *
     * @param filterText
     * @param max
     * @param offset
     * @return
     */
    def getCommonMatchingDocs( filterText, max, offset ) {
        def inputMap = [filterText: filterText?.toUpperCase()]
        LoggerUtility.debug( LOGGER, 'inputMap ' + inputMap )
        FinanceCommonUtility.applyWildCard( inputMap, true, true )
        def documents = []
        try {
            def documentResult = sessionFactory.getCurrentSession().createSQLQuery( FinanceProcurementConstants.COMMON_MATCH_SQL )
                    .setString( FinanceProcurementConstants.BDM_DOC_TYPE_CODE, inputMap.filterText )
                    .setString( FinanceProcurementConstants.BDM_DOC_TYPE_DESC, inputMap.filterText )
                    .setMaxResults( max )
                    .setFirstResult( offset )
                    .list()
            documentResult.each() {document ->
                documents << [code: document[0], description: document[1]]
            }
        }
        catch (SQLException ae) {
            throw new ApplicationException( FinanceProcurementDocumentTypeService, ae )
        }
        documents
    }

}

/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.service.ServiceBase
import org.hibernate.HibernateException
import org.hibernate.Session
import grails.gorm.transactions.Transactional
/**
 * This service helps to get document type for BDM attachments
 */
class FinanceProcurementDocumentTypeService extends ServiceBase {


    /**
     * Gets doc types
     *
     * @param filterText
     * @param max
     * @param offset
     * @return
     */
    @Transactional(readOnly = true)
    def getCommonMatchingDocs( filterText, max, offset, defaultSQL = null ) {
        def inputMap = [filterText: filterText?.toUpperCase()]
        log.debug('inputMap {}', inputMap )
        FinanceCommonUtility.applyWildCard( inputMap, true, true )
        def documents = []
        Session session
        try {
            session = sessionFactory.openSession()
            def documentResult = session.createSQLQuery( defaultSQL ? defaultSQL : FinanceProcurementConstants.COMMON_MATCH_SQL )
                    .setString( FinanceProcurementConstants.BDM_DOC_TYPE_CODE, inputMap.filterText )
                    .setString( FinanceProcurementConstants.BDM_DOC_TYPE_DESC, inputMap.filterText )
                    .setMaxResults( max )
                    .setFirstResult( offset )
                    .list()
            documentResult.each() {document ->
                documents << [code: document[0], description: document[1]]
            }
        }
        catch (HibernateException ae) {
            throw new ApplicationException( FinanceProcurementDocumentTypeService, ae )
        }
        finally {
            session?.close()
        }
        documents
    }

}

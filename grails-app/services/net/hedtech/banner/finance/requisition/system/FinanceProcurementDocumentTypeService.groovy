/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException

import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.finance.util.FinanceCommonUtility
import org.apache.log4j.Logger

import java.sql.SQLException

/**
 * This service helps to get document type for BDM attachments
 */
class FinanceProcurementDocumentTypeService extends ServiceBase {

    def log = Logger.getLogger(FinanceProcurementDocumentTypeService.name)

    /**
     *
     * @param filterText
     * @param max
     * @param offset
     * @return
     */
    def getCommonMatchingDocs( filterText, max, offset ) {
           def inputMap = [filterText: filterText?.toUpperCase()]
           FinanceCommonUtility.applyWildCard( inputMap, true, true )
           def documents = []
           def sql
           try {
               def commonMatchSql = "SELECT e.ETVDTYP_CODE AS code, e.ETVDTYP_DESC AS DESCRIPTION \
                   FROM ETVDTYP e, otgmgr.ul506_2 \
                   WHERE e.ETVDTYP_CODE = otgmgr.ul506_2.item \
                   AND (UPPER(e.ETVDTYP_CODE) LIKE :docTypCode OR  UPPER(e.ETVDTYP_DESC) LIKE :docTypDesc)"
               def documentResult = sessionFactory.getCurrentSession().createSQLQuery( commonMatchSql )
                       .setString( 'docTypCode', inputMap.filterText )
                       .setString( 'docTypDesc', inputMap.filterText )
                       .setMaxResults( max )
                       .setFirstResult( offset )
                       .list()

               documentResult.each() {document ->
                   documents << ['code' : document[0], 'description' : document[1]]
               }
           }
           catch (
                   SQLException ae
                   ) {
               throw new ApplicationException( FinanceProcurementDocumentTypeService, ae )
           }
           finally {
               sql?.close()
           }
           println documents
           return documents
       }

}

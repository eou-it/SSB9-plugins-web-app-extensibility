/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementSQLConstants
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import org.hibernate.HibernateException
import org.hibernate.Session

/**
 * The service class which is used to have methods for copy purchase requisition.
 */
class CopyPurchaseRequisitionCompositeService {
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    boolean transactional = true

    def sessionFactory
    def requisitionHeaderService

    /**
     * This method is used to copy the requisition.
     */
    def copyRequisition( requestCode, defaultQuery = null ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (header.completeIndicator) {
            Session session
            try {
                session = sessionFactory.openSession()
                session.createSQLQuery( FinanceProcurementSQLConstants.QUERY_UPDATE_NEXT_REQ_SEQUENCE ).executeUpdate()
                def nextDocCode = session.createSQLQuery( FinanceProcurementSQLConstants.QUERY_NEXT_REQ_NUMBER ).list()[0]
                session.createSQLQuery( defaultQuery ? defaultQuery : FinanceProcurementSQLConstants.QUERY_COPY_REQUISITION )
                        .setParameter( FinanceProcurementConstants.NEXT_DOC_CODE, nextDocCode )
                        .setParameter( FinanceProcurementConstants.OLD_DOC_CODE, requestCode )
                        .executeUpdate()
                return nextDocCode
            } catch (HibernateException e) {
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $header.requestCode" )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, e )
            }
            finally {
                session?.close()
            }
        } else {
            LoggerUtility.error( LOGGER, "Only completed requisition can be copied = $header.requestCode" )
            throw new ApplicationException(
                    CopyPurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED, [] ) )
        }
    }
}

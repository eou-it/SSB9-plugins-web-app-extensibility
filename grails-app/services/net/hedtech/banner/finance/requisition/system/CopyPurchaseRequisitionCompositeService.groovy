/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementSQLUtils
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import org.hibernate.HibernateException
import org.hibernate.Session

import java.sql.SQLException

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
    def copyRequisition( requestCode ) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode( requestCode )
        if (header && header.completeIndicator) {
            Session session
            try {
                session = sessionFactory.getCurrentSession()
                session.createSQLQuery( FinanceProcurementSQLUtils.getUpdateReqNextQuery() ).executeUpdate()
                def nextDocCode = session.createSQLQuery( FinanceProcurementSQLUtils.getSelectGeneratedReqCodeQuery() ).list()[0]
                session.createSQLQuery( FinanceProcurementSQLUtils.getCopyRequisitionQuery() )
                        .setParameter( 'nextDocCode', nextDocCode )
                        .setParameter( 'oldDocCode', requestCode )
                        .executeUpdate()
                return nextDocCode
            } catch (HibernateException | SQLException e) {
                LoggerUtility.error( LOGGER, "Error While Copy Requisition $header.requestCode" )
                throw new ApplicationException( CopyPurchaseRequisitionCompositeService, e )
            }
            finally {
                try {
                    session?.close()
                }
                catch (HibernateException e) {
                    LoggerUtility.error( LOGGER, "Error While Copy Requisition $header.requestCode" )
                    throw new ApplicationException( CopyPurchaseRequisitionCompositeService, e )
                }
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

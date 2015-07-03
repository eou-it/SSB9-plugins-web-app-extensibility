/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.transaction.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.requisition.util.FinanceProcurementSQLUtils
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger

/**
 * The service class which is used to have methods for copy purchase requisition.
 */
class CopyPurchaseRequisitionCompositeService {
    private static final def LOGGER = Logger.getLogger(this.getClass())
    boolean transactional = true

    def sessionFactory
    def requisitionHeaderService
    def requisitionInformationCompositeService

    /**
     * This method is used to copy the requisition.
     */
    def copyRequisition(requestCode, institutionBaseCcy) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        if (header && header.completeIndicator) {
            try {
                def session = sessionFactory.getCurrentSession()
                session.createSQLQuery(FinanceProcurementSQLUtils.getUpdateReqNextQuery()).executeUpdate()
                def nextDocCode = session.createSQLQuery(FinanceProcurementSQLUtils.getSelectGeneratedReqCodeQuery()).list()[0]
                session.createSQLQuery(FinanceProcurementSQLUtils.getCopyRequisitionQuery())
                        .setParameter('nextDocCode', nextDocCode)
                        .setParameter('oldDocCode', requestCode)
                        .executeUpdate()
                return requisitionInformationCompositeService.fetchPurchaseRequisition(nextDocCode, institutionBaseCcy)
            } catch (Exception e) {
                LoggerUtility.error(LOGGER, "Error While Copy Requisition $header.requestCode")
                throw new ApplicationException(CopyPurchaseRequisitionCompositeService, e)
            }
        } else {
            LoggerUtility.error(LOGGER, "Only completed requisition can be copied = $header.requestCode")
            throw new ApplicationException(
                    CopyPurchaseRequisitionCompositeService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED, []))
        }
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

class FinanceObjectSequenceService extends ServiceBase {
    private static final def LOGGER = Logger.getLogger( this.getClass() )

    /**
     * Validate method for create
     * @param map
     */
    def preCreate( map ) {
        LoggerUtility.debug( log, map )
        throw new ApplicationException( FinanceObjectSequence, FinanceValidationConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }

    /**
     * Validate method for delete
     * @param map
     */
    def preDelete( map ) {
        LoggerUtility.debug( log, map )
        throw new ApplicationException( FinanceObjectSequence, FinanceValidationConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }

    def findNextSequenceNumber() {
        def financeObjectSequence = FinanceObjectSequence.findBySequenceType( FinanceProcurementConstants.FINANCE_OBJECT_SEQ_FOBSEQN_SEQNO_TYPE )
        financeObjectSequence.maxSeqNumber7 = financeObjectSequence.maxSeqNumber7 + FinanceProcurementConstants.ONE
        financeObjectSequence = update( financeObjectSequence )
        def nextRequisitionNumber = financeObjectSequence.seqNumberPrefix.padRight( FinanceProcurementConstants.EIGHT - financeObjectSequence.maxSeqNumber7.toString().length(), FinanceProcurementConstants.ZERO_STRING ) + financeObjectSequence.maxSeqNumber7
        LoggerUtility.debug( LOGGER, 'Next Requisition for Copy requisition: ' + nextRequisitionNumber )
        nextRequisitionNumber
    }
}

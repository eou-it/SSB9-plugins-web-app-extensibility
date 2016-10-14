/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

/**
 * The persistent class for the FOBAINP database table.
 *
 */
@Entity
@Table(name = FinanceProcurementConstants.FOBAINP)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOCUMENT_NUMBER,
                query = """FROM FinanceApprovalsInProcess approvalsInProcess
                    WHERE approvalsInProcess.documentNumber = :documentNumber"""),
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOC_NUMBER_DOC_TYPE,
                query = """FROM FinanceApprovalsInProcess  approvalsInProcess
                    WHERE approvalsInProcess.documentNumber = :documentNumber AND approvalsInProcess.documentType = :documentType""")
])
public class FinanceApprovalsInProcess implements Serializable {

    @Id
    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_SURROGATE_ID, precision = 19)
    Long id

    @Temporal(TemporalType.DATE)
    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_ACTIVITY_DATE, nullable = false)
    Date activityDate

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_APPROVAL_SEQUENCE, precision = 6)
    BigDecimal approvalSequence

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_CHG_SEQ_NUM, precision = 4)
    BigDecimal changeSequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DATA_ORIGIN, length = 30)
    String dataOrigin

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DOC_NUM, nullable = false, length = 8)
    String documentNumber

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DOC_TYPE, nullable = false, precision = 2)
    Long documentType

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_LEVEL, precision = 4)
    BigDecimal level

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_QUEUE_ID, length = 4)
    String queueId

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_SUBMISSION_NUM, precision = 2)
    BigDecimal submissionNumber

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_USER_ID, nullable = false, length = 30)
    String lastModifiedBy

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_VERSION, precision = 19)
    Long version

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_FOBAINP_VPDI_CODE, length = 6)
    String vpdiCode

    /**
     * Method is used to get FinanceApprovalsInProcess by document number.
     * @param documentCode document code.
     * @return list of FinanceApprovalsInProcess.
     */
    static def fetchByDocumentNumber( documentNumber ) {
        def list = FinanceApprovalsInProcess.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOCUMENT_NUMBER )
                    .setString( FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_QUERY_PARAM_DOCUMENT_NUMBER, documentNumber )
                    .list()
        }
        return list
    }

    /**
     * Method is used to get FinanceApprovals In process by document code and documentTypeCode.
     * @param documentCode document code.
     * @param documentType
     * @return list of FinanceApprovalHistory.
     */
    static def fetchByDocumentCodeAndDocType( documentCode, long documentType ) {
        def list = FinanceUnapprovedDocument.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOC_NUMBER_DOC_TYPE )
                    .setString( FinanceProcurementConstants.FINANCE_APPROVAL_IN_PROCESS_QUERY_PARAM_DOCUMENT_NUMBER, documentCode )
                    .setLong( FinanceProcurementConstants.FINANCE_QUERY_PARAM_DOCUMENT_TYPE_CODE, documentType )
                    .list()
        }
        return list
    }
}

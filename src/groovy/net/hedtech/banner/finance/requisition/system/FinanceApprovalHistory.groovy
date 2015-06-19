/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

/**
 * The persistent class for the FOBAPPH database table.
 *
 */
@Entity
@Table(name = FinanceProcurementConstants.FOBAPPH)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@NamedQuery(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_NAME_FIND_BY_DOCUMENT_CODE,
        query = """FROM FinanceApprovalHistory approvalHistory
                    WHERE approvalHistory.documentCode = :documentCode""")
public class FinanceApprovalHistory implements Serializable {

    @Id
    @SequenceGenerator(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_SEQ_GEN,
            sequenceName = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_SURROGATE_ID_SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_SEQ_GEN)
    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_SURROGATE_ID, precision = 19)
    Long id

    @Temporal(TemporalType.DATE)
    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_ACTIVITY_DATE, nullable = false)
    Date activityDate

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_CHG_SEQ_NUM, precision = 3)
    BigDecimal changeSequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_DATA_ORIGIN, length = 30)
    String dataOrigin

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_DOC_CODE, nullable = false, length = 15)
    String documentCode

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_QUEUE_ID, length = 4)
    String queueId

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_QUEUE_LEVEL, precision = 4)
    BigDecimal queueLevel

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_SEQ_NUM, nullable = false, precision = 2)
    BigDecimal sequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_SUBMISSION_NUMBER, precision = 2)
    BigDecimal submissionNumber

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_USER_ID, nullable = false, length = 30)
    String lastModifiedBy

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_VERSION, precision = 19)
    Long version

    @Column(name = FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_FOBAPPH_VPDI_CODE, length = 6)
    String vpdiCode

    /**
     * Method is used to get FinanceApprovalHistory by document code.
     * @param documentCode document code.
     * @return list of FinanceApprovalHistory.
     */
    static def findByDocumentCode(documentCode) {
        def list = FinanceUnapprovedDocument.withSession { session ->
            session.getNamedQuery(FinanceProcurementConstants.FINANCE_APPROVAL_HISTORY_QUERY_NAME_FIND_BY_DOCUMENT_CODE)
                    .setString(FinanceProcurementConstants.FINANCE_QUERY_PARAM_DOCUMENT_CODE, documentCode)
                    .list()
        }
        return list
    }

}
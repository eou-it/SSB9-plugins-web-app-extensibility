/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

/**
 * Domain class for Unapproved Documents Table FOBUAPP.
 */
@Entity
@Table(name = FinanceProcurementConstants.FOBUAPP)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@NamedQuery(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_QUERY_NAME_FIND_BY_DOCUMENT_CODE,
        query = """FROM FinanceUnapprovedDocument unapprovedDocument
                    WHERE unapprovedDocument.documentCode = :documentCode""")
class FinanceUnapprovedDocument implements Serializable {

    @Id
    @SequenceGenerator(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_SEQ_GEN,
            sequenceName = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_SURROGATE_ID_SEQUENCE,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_SEQ_GEN)
    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_SURROGATE_ID, precision = 19)
    Long id

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DOC_CODE, unique = true, nullable = false, length = 15)
    String documentCode

    @Temporal(TemporalType.DATE)
    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_ACTIVITY_DATE, nullable = false)
    Date activityDate

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_CHG_SEQ_NUM, precision = 3)
    BigDecimal changeSequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DATA_ORIGIN, length = 30)
    String dataOrigin

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DOC_AMT, nullable = false, precision = 17, scale = 2)
    BigDecimal documentAmount

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_ORIG_USER, nullable = false, length = 30)
    String originUser

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_STATUS_IND, length = 1)
    String statusIndicator

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_SUBMISSION_NUMBER, precision = 2)
    BigDecimal submissionNumber

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_USER_CODE, nullable = false, length = 30)
    String userCode

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_USER_ID, length = 30)
    String lastModifiedBy

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_VERSION, precision = 19)
    Long version

    @Column(name = FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_VPDI_CODE, length = 6)
    String vpdiCode

    /**
     * Method is used to get FinanceUnapprovedDocument by document code.
     * @param documentCode document code.
     * @return list of FinanceUnapprovedDocument.
     */
    static def fetchByDocumentCode(documentCode) {
        def list = FinanceUnapprovedDocument.withSession { session ->
            session.getNamedQuery(FinanceProcurementConstants.FINANCE_UNAPPROVED_DOCUMENT_QUERY_NAME_FIND_BY_DOCUMENT_CODE)
                    .setString(FinanceProcurementConstants.FINANCE_QUERY_PARAM_DOCUMENT_CODE, documentCode)
                    .list()
        }
        return list
    }

}

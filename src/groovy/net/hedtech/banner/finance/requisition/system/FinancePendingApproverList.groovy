/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

/**
 * The domain class for the FVQ_REQ_APPROVER_LIST table.
 */
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.FVQ_REQ_APPROVER_LIST_QUERY_FIND_BY_DOC_CODE,
                query = """FROM FinancePendingApproverList pendingApproverList
                                WHERE pendingApproverList.documentCode = :documentCode""")
])
@Entity
@Table(name = FinanceProcurementConstants.FVQ_REQ_APPROVER_LIST_VIEW)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
class FinancePendingApproverList implements Serializable {

    /**
     * Composite key.
     */
    @EmbeddedId
    FinancePendingApproverListKey id

    @Column(name = FinanceProcurementConstants.FINANCE_PENDING_APPROVER_LIST_DESCRIPTION)
    String description

    @Column(name = FinanceProcurementConstants.FINANCE_PENDING_APPROVER_LIST_DOC_CODE)
    String documentCode

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_PENDING_APPROVER_LIST_VERSION)
    Long version

    /**
     * Method to find FinancePendingApproverList by document code.
     * @param documentCode Document code.
     * @return List of FinancePendingApproverList.
     */
    static def findByDocumentCode( documentCode ) {
        def list = FinancePendingApproverList.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FVQ_REQ_APPROVER_LIST_QUERY_FIND_BY_DOC_CODE )
                    .setString( FinanceProcurementConstants.FINANCE_PENDING_APPROVER_LIST_QUERY_PARAM_DOC_CODE, documentCode )
                    .list()
        }
        return list
    }
}

/**
 * Primary class for Requisition Summary
 */
@Embeddable
@ToString(includeNames = true, ignoreNulls = true)
@EqualsAndHashCode(includeFields = true)
class FinancePendingApproverListKey implements Serializable {
    @Column(name = FinanceProcurementConstants.FINANCE_PENDING_APPROVER_LIST_APPROVER_NAME)
    String approverName

    @Column(name = FinanceProcurementConstants.FINANCE_PENDING_APPROVER_LIST_QUEUE_ID)
    String queueId
}

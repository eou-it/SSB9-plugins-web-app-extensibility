/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

/**
 * The domain class for the FTVBUYR database table.
 *
 */
@Entity
@Table(name = FinanceProcurementConstants.FTVBUYR_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_NAMED_QUERY_FIND_BY_REQUEST_CODE,
                query = """SELECT DISTINCT buyerVerification FROM FinanceBuyerVerification buyerVerification
                            JOIN buyerVerification.requisitionDetails requisitionDetails
                            WHERE requisitionDetails.requestCode = :requestCode""")
])
public class FinanceBuyerVerification implements Serializable {

    @Id
    @Column(name = FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_FTVBUYR_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_FTVBUYR_CODE)
    String buyerCode

    @Column(name = FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_FTVBUYR_NAME)
    String buyerName

    //bi-directional many-to-one association to RequisitionDetail
    @OneToMany(mappedBy = FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_BUYER)
    List<RequisitionDetail> requisitionDetails

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_FTVBUYR_VERSION)
    Long version

    /**
     * Method to find buyer verification by request code.
     * @param requestCode Request code.
     * @return List of FinanceBuyerVerification
     */
    static def findByDocumentCode(requestCode) {
        def list = FinanceBuyerVerification.withSession { session ->
            session.getNamedQuery(FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_NAMED_QUERY_FIND_BY_REQUEST_CODE)
                    .setString(FinanceProcurementConstants.FINANCE_BUYER_VERIFICATION_QUERY_PARAM_REQUEST_CODE, requestCode)
                    .list()
        }
        return list
    }

}
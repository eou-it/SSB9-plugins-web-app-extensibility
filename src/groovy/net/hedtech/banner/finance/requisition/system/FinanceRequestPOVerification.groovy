/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.hibernate.Session

import javax.persistence.*

/**
 * The domain class for the FTVRQPO database table.
 *
 */
@Entity
@Table(name = FinanceProcurementConstants.FTVRQPO_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_NAMED_QUERY_FIND_BY_REQ_CODE,
                query = """SELECT DISTINCT requestPOVerification.pohdCode FROM FinanceRequestPOVerification requestPOVerification
                                WHERE requestPOVerification.requestCode = :requestCode""")
])
public class FinanceRequestPOVerification implements Serializable {

    @Id
    @Column(name = FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_REQD_CODE)
    String requestCode

    @Column(name = FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_POHD_CODE)
    String pohdCode

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_VERSION)
    Long version

    /**
     * Find FinanceRequestPOVerification by request code.
     * @param requestCode Requisition code.
     * @return list of FinanceRequestPOVerification.
     */
    static def fetchByRequestCode(requestCode) {
        def list = FinanceRequestPOVerification.withSession { Session session ->
            session.getNamedQuery(FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_NAMED_QUERY_FIND_BY_REQ_CODE)
                    .setString(FinanceProcurementConstants.FINANCE_REQUEST_PO_VERIFICATION_QUERY_PARAM_REQUEST_CODE, requestCode)
                    .list()
        }
        return list
    }

}

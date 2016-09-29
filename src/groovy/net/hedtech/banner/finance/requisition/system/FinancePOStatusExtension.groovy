/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.hibernate.Session

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.Version

/**
 * The domain class for the FPBPOHD database table.
 *
 */
@Entity
@Table(name = FinanceProcurementConstants.FPBPOHD_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_NAMED_QUERY_FIND_BY_POHD_CODE,
                query = """SELECT poStatusExtension.apprInd, poStatusExtension.completeInd FROM FinancePOStatusExtension poStatusExtension
                                WHERE poStatusExtension.pohdCode = :pohdCode""")
])
public class FinancePOStatusExtension implements Serializable{

    @Id
    @Column(name = FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_FPBPOHD_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_FPBPOHD_APPR_IND)
    String apprInd

    @Column(name = FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_FPBPOHD_COMPLETE_IND)
    String completeInd

    @Column(name = FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_FPBPOHD_POHD_CODE)
    String pohdCode

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_FPBPOHD_VERSION)
    Long version

    /**
     * Find FinancePOStatusExtension by pohd code.
     * @param pohdCode POHD code.
     * @return list of FinancePOStatusExtension.
     */
    static def fetchByPOHDCode(pohdCode) {
        def list = FinancePOStatusExtension.withSession { Session session ->
            session.getNamedQuery(FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_NAMED_QUERY_FIND_BY_POHD_CODE)
                    .setString(FinanceProcurementConstants.FINANCE_PO_STATUS_EXTENSION_QUERY_PARAM_POHD_CODE, pohdCode)
                    .list()
        }
        return list
    }
}

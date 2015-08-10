/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.*

/**
 * The persistent class for the GURTKLR database table.
 */
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.GURTKLR_NAMED_QUERY_FIND_BY_ITEM_REFERENCE,
                query = """FROM FinanceGeneralTickler ticklerTable
                WHERE ticklerTable.itemReferenceNo = :itemReferenceNo""")
])
@Entity
@Table(name = FinanceProcurementConstants.GURTKLR_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
public class FinanceGeneralTickler implements Serializable {

    @Id
    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_USER_ID)
    String lastModifiedBy

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_CREATOR)
    String creator

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_SEQNO)
    long sequenceNumber

    @Temporal(TemporalType.DATE)
    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_ACTIVITY_DATE)
    Date activityDate

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_COMMENT)
    String comment

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_CONFID_IND)
    String confidentialIndicator

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_DATA_ORIGIN)
    String dataOrigin

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_IDEN_CODE)
    String identificationCode

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_ITEM_REFNO)
    String itemReferenceNo

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_PIDM)
    BigDecimal pidm

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_SOURCE)
    String source

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_STATUS)
    String status

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_SYSTEM_IND)
    String systemIndicator

    @Temporal(TemporalType.DATE)
    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_TODO_DATE)
    Date toDoDate

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_TODO_TIME)
    BigDecimal toDoTime

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_VERSION)
    Long version

    @Column(name = FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_GURTKLR_VPDI_CODE)
    String vpdiCode

    /**
     * Find General Tickler by item reference number.
     * @param itemReferenceNo item reference number.
     * @return list of FinanceGeneralTickler.s
     */
    static def findByReferenceNumber( itemReferenceNo ) {
        def list = FinanceGeneralTickler.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.GURTKLR_NAMED_QUERY_FIND_BY_ITEM_REFERENCE )
                    .setString( FinanceProcurementConstants.FINANCE_GENERAL_TICKLER_QUERY_PARAM_ITEM_REF_NO, itemReferenceNo )
                    .list()
        }
        return list
    }

}

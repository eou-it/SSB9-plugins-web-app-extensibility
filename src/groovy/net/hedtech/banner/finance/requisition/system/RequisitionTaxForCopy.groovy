/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

@Entity
@Table(name = FinanceProcurementConstants.FPRRQTX_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
class RequisitionTaxForCopy implements Serializable {
/**
 * Surrogate ID for FPRRQTX
 */
    @Id
    @SequenceGenerator(name = FinanceProcurementConstants.FIELD_FPRRQTX_SEQ_GEN, allocationSize = 1,
            sequenceName = FinanceProcurementConstants.FIELD_FPRRQTX_SURROGATE_ID_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.FIELD_FPRRQTX_SEQ_GEN)
    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_REQH_CODE)
    String requestCode

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_ITEM)
    BigDecimal item

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_PRIORITY_NUM)
    BigDecimal proximityNumber

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_TRAT_CODE)
    String taxRateCode

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_TAX_AMT)
    BigDecimal taxAmount

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_TAXABLE_AMT)
    BigDecimal taxableAmount

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_PAY_TAX_TO)
    String payTaxTo

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_ACTIVITY_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_USER_ID)
    String userId

    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_DATA_ORIGIN)
    String dataOrigin

    @Version
    @Column(name = FinanceProcurementConstants.FIELD_FPRRQTX_VERSION)
    Long version

    /**
     * All required constraints for the table columns.
     */
    static constraints = {
        requestCode( nullable: false )
        item( nullable: false )
        proximityNumber( nullable: false )
        taxRateCode( nullable: false )
        taxAmount( nullable: false )
        taxableAmount( nullable: false )
        payTaxTo( nullable: false )
        lastModified( nullable: true )
        userId( nullable: true, maxSize: 30 )
        dataOrigin( nullable: true, maxSize: 30 )
    }
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

@Entity
@Table(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)

class FinanceObjectSequence implements Serializable {

    /**
     * Surrogate ID for FOBSEQN
     */
    @Id
    @Column(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_SURROGATE_ID)
    @SequenceGenerator(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_SEQ_GENERATOR, allocationSize = 1, sequenceName =
            FinanceProcurementConstants.FINANCE_OBJECT_SEQ_SEQ_FOBSEQN_SURROGATE_ID_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_SEQ_GENERATOR)
    Long id

    /**
     * SEQUENCE TYPE
     */
    @Column(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_SEQNO_TYPE)
    String sequenceType

    /**
     * SEQUENCE NUMBER PREFIX
     */
    @Column(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_SEQNO_PREFIX)
    String seqNumberPrefix

    /**
     * MAX SEQUENCE NUMBER
     */
    @Column(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_MAXSEQNO_7)
    Integer maxSeqNumber7

    /**
     * VERSION
     */
    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_VERSION)
    Long version

    static constraints = {
        sequenceType( nullable: false, maxSize: 1 )
        seqNumberPrefix( nullable: true, maxSize: 1 )
        maxSeqNumber7( nullable: false )
    }
}

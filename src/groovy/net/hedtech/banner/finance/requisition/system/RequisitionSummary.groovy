/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.service.DatabaseModifiesState
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 *  Persistence class for Requisition Summary (FVQ_REQ_SUMMARY).
 */
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FINDER_BY_REQUEST_CODE,
                query = """FROM RequisitionSummary summary
                            WHERE summary.requestCode = :requestCode""")
])
@Entity
@Table(name = FinanceProcurementConstants.REQUISITION_SUMMARY_VIEW)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@DatabaseModifiesState
class RequisitionSummary implements Serializable {

    /**
     * Composite: Immutable unique key.
     */
    @EmbeddedId
    RequisitionSummaryKey id

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_VERSION)
    Long version

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_ITEM)
    Long accountingItem

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_SEQ_NUM)
    Long accountingSequenceNumber

    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_SINGLE_ACCTG_IND)
    Boolean isDocumentLevelAccounting

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_PCT)
    BigDecimal accountingPercentage

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_AMT)
    BigDecimal accountingAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_COAS_CODE)
    String accountingCoaCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_ACCI_CODE)
    String accountingIndexCode

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COAS_CODE)
    String chartOfAccountCode

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ORGN_CODE)
    String organizationCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_FUND_CODE)
    String accountingFundCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_ORGN_CODE)
    String accountingOrgCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_ACCT_CODE)
    String accountingAccountCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_ACTV_CODE)
    String accountingActivityCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_SHIP_TO_CODE)
    String shipToCode

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQD_DATE)
    @Temporal(TemporalType.DATE)
    Date deliveryDate

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TRANS_DATE)
    @Temporal(TemporalType.DATE)
    Date transactionDate


    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_PROJ_CODE)
    String accountingProjectCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_ATTENTION_TO)
    String attentionTo

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_PROG_CODE)
    String accountingProgramCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_ID)
    String vendorCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_DISC_AMT)
    BigDecimal accountingDiscountAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_ADDL_CHRG_AMT)
    BigDecimal accountingAdditionalChargeAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_TAX_AMT)
    BigDecimal accountingTaxAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_COMM_CODE)
    String commodityCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_COMM_DESC)
    String commodityDescription

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FTVCOMM_DESC)
    String commodityCodeDesc

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_QTY)
    BigDecimal commodityQuantity

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_ITEM)
    Long commodityItem

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_DISC_AMT)
    BigDecimal commodityDiscountAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_ADDL_CHRG_AMT)
    BigDecimal commodityAdditionalChargeAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_TAX_AMT)
    BigDecimal commodityTaxAmount

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_UNIT_PRICE)
    BigDecimal commodityUnitPrice

    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_UOMS_CODE)
    String unitOfMeasure

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_CURR_CODE)
     String ccyCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_CODE)
    String requestCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_VEND_PIDM)
    String vendorPidm

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_ATYP_SEQ_NUM)
    String vendorAddressTypeSequence

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_ATYP_CODE)
    String vendorAddressTypeCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_LAST_NAME)
    String vendorLastName

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE1)
    String vendorAddressLine1

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE2)
    String vendorAddressLine2
    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE3)
    String vendorAddressLine3
    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_ZIP)
    String vendorAddressZipCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STAT_CODE)
    String vendorAddressStateCode

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_CITY)
    String vendorAddressCity

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_NUMBER)
    String vendorPhoneNumber
    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_EXT)
    String vendorPhoneExtension

    /**
     * This method is used to fetch requisition detail by requisition code.
     * @param requestCode Requisition code.
     * @return requisition Summary.
     */
    static def fetchRequisitionSummaryForRequestCode( requestCode ) {
        RequisitionDetail.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_SUMMARY_FINDER_BY_REQUEST_CODE )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .list()
        }
    }
}

/**
 * Primary class for Requisition Summary
 */
@Embeddable
@ToString(includeNames = true, ignoreNulls = true)
@EqualsAndHashCode(includeFields = true)
class RequisitionSummaryKey implements Serializable {
    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPBREQH_SURROGATE_ID)
    Long headerId

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQD_SURROGATE_ID)
    Long commodityId

    @Column(name = FinanceProcurementConstants.REQUISITION_SUMMARY_FIELD_FPRREQA_SURROGATE_ID)
    Long accountingId
}

/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 *  Requisition Header Domain object to store the details of requisition header information
 */

@Entity
@Table(name = 'FPBREQH')
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
class RequisitionHeaderForCopy implements Serializable {

    /**
     * Surrogate ID for FPBREQH
     */
    @Id
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SURROGATE_ID)
    @SequenceGenerator(name = FinanceProcurementConstants.REQUISITION_HEADER_SEQ_GENERATOR, allocationSize = 1, sequenceName =
            FinanceProcurementConstants.REQUISITION_HEADER_SEQ_FPBREQH_SURROGATE_ID_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.REQUISITION_HEADER_SEQ_GENERATOR)
    Long id

    /**
     * CODE
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CODE)
    String requestCode

    /**
     * Activity Date
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ACTIVITY_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last Modified By
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_USER_ID)
    String userId

    /**
     * Requisition Date
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQH_DATE)
    @Temporal(TemporalType.DATE)
    Date requestDate

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TRANS_DATE)
    @Temporal(TemporalType.DATE)
    Date transactionDate

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_NAME)
    String requesterName

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PHONE_AREA)
    String requesterPhoneArea

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PHONE_NUM)
    String requesterPhoneNumber

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PHONE_EXT)
    String requesterPhoneExtension

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VEND_PIDM)
    Integer vendorPidm

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ATYP_CODE)
    String vendorAddressType

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ATYP_SEQ_NUM)
    Integer vendorAddressTypeSequence

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COAS_CODE)
    String chartOfAccount

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ORGN_CODE)
    String organization

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQD_DATE)
    @Temporal(TemporalType.DATE)
    Date deliveryDate

    /**
     * Indicator to indicate if header is complete
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COMPLETE_IND)
    Boolean completeIndicator

    /**
     * Indicator to indicate if header is printable
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PRINT_IND)
    String printIndicator

    /**
     * Indicator to indicate if header is encumbranced
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ENCUMB_IND)
    String encumbranceIndicator

    /**
     * Indicator to indicate if header is suspended
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SUSP_IND)
    Boolean suspenseIndicator

    /**
     * Indicator to indicate if header is cancelled
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_IND)
    String cancelIndicator

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_DATE)
    @Temporal(TemporalType.DATE)
    Date cancellationDate

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_POST_DATE)
    @Temporal(TemporalType.DATE)
    Date postingDate

    /**
     * Indicator to indicate if header is approved
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_APPR_IND)
    Boolean approvalIndicator

    /**
     * Indicator to indicate if header has text
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TEXT_IND)
    Boolean textIndicator

    /**
     * This indicator is used to defer editing of the transaction until posting.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_EDIT_DEFER_IND)
    Boolean deferEditIndicator

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_RECOMM_VEND_NAME)
    String recommVendName

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CURR_CODE)
    String currency

    /**
     * NSF ON OFF Indicator: If Y, available balance checking will be done at entry time for this particular requisition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_NSF_ON_OFF_IND)
    Boolean nsfOnOffIndicator

    /**
     * Document level accounting: If Y, the document will have an accounting or accounting distributions for the document not for specific commodities
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SINGLE_ACCTG_IND)
    Boolean isDocumentLevelAccounting

    /**
     * close level indicator. If Y, all items for this requisition are closed, and no more activity will be done against this requisition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_IND)
    Boolean closedIndicator

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SHIP_CODE)
    String ship

    /**
     * REQUEST TYPE INDICATOR: Type which defines the kind of requisition this document is.  Valid types are (P)rocurement and (S)tores requisition.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_RQST_TYPE_IND)
    String requestTypeIndicator

    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_INVENTORY_REQ_IND)
    Boolean inventoryRequisitionIndicator

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CRSN_CODE)
    String reason

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DELIVERY_COMMENT)
    String deliveryComment

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_EMAIL_ADDR)
    String requesterEmailAddress

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_FAX_AREA)
    String requesterFaxArea

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_FAX_NUMBER)
    String requesterFaxNumber

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_FAX_EXT)
    String requesterFaxExtension

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ATTENTION_TO)
    String attentionTo

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VENDOR_CONTACT)
    String vendorContact

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DISC_CODE)
    String discount

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VEND_EMAIL_ADDR)
    String vendorEmailAddress

    /**
     * Number of requisition document copied.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COPIED_FROM)
    String documentCopiedFrom

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TGRP_CODE)
    String taxGroup

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQ_PRINT_DATE)
    @Temporal(TemporalType.DATE)
    Date requisitionPrintDate

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_DATE)
    @Temporal(TemporalType.DATE)
    Date closedDate

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_MATCH_REQUIRED)
    String matchRequired

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ORIGIN_CODE)
    String requisitionOrigination

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DOC_REF_CODE)
    String requisitionReference

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_PHONE)
    String telephoneCountry

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_FAX)
    String requesterFaxCountry

    @Version
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VERSION)
    Long version

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DATA_ORIGIN)
    String dataOrigin

    static constraints = {
        requestCode( nullable: false, maxSize: 8 )
        lastModified( nullable: true )
        userId( nullable: true, maxSize: 30 )
        requestDate( nullable: false )
        transactionDate( nullable: false )
        requesterName( nullable: true, maxSize: 35 )
        requesterPhoneArea( nullable: true, maxSize: 6 )
        requesterPhoneNumber( nullable: true, maxSize: 12 )
        requesterPhoneExtension( nullable: true, maxSize: 10 )
        vendorPidm( nullable: true )
        vendorAddressType( nullable: true, maxSize: 2 )
        vendorAddressTypeSequence( nullable: true )
        chartOfAccount( nullable: true, maxSize: 1 )
        organization( nullable: true, maxSize: 6 )
        deliveryDate( nullable: true )
        completeIndicator( nullable: true )
        printIndicator( nullable: true, maxSize: 1 )
        encumbranceIndicator( nullable: true, maxSize: 1 )
        suspenseIndicator( nullable: true )
        cancelIndicator( nullable: true, maxSize: 1 )
        cancellationDate( nullable: true )
        postingDate( nullable: true )
        approvalIndicator( nullable: true )
        textIndicator( nullable: true )
        deferEditIndicator( nullable: true )
        recommVendName( nullable: true, maxSize: 60 )
        currency( nullable: true, maxSize: 4 )
        nsfOnOffIndicator( nullable: true )
        isDocumentLevelAccounting( nullable: true )
        closedIndicator( nullable: true )
        ship( nullable: true, maxSize: 6 )
        requestTypeIndicator( nullable: false, maxSize: 1, inList: ["P", "S"] )
        inventoryRequisitionIndicator( nullable: true )
        reason( nullable: true, maxSize: 4 )
        deliveryComment( nullable: true, maxSize: 30 )
        requesterEmailAddress( nullable: true, maxSize: 128 )
        requesterFaxArea( nullable: true, maxSize: 6 )
        requesterFaxNumber( nullable: true, maxSize: 12 )
        requesterFaxExtension( nullable: true, maxSize: 10 )
        attentionTo( nullable: true, maxSize: 35 )
        vendorContact( nullable: true, maxSize: 35 )
        discount( nullable: true, maxSize: 2 )
        vendorEmailAddress( nullable: true, maxSize: 128 )
        documentCopiedFrom( nullable: true, maxSize: 8 )
        taxGroup( nullable: true, maxSize: 4 )
        requisitionPrintDate( nullable: true )
        closedDate( nullable: true )
        matchRequired( nullable: false, maxSize: 1 )
        requisitionOrigination( nullable: true, maxSize: 26 )
        requisitionReference( nullable: true, maxSize: 20 )
        telephoneCountry( nullable: true, maxSize: 4 )
        requesterFaxCountry( nullable: true, maxSize: 4 )
        dataOrigin( nullable: true, maxSize: 30 )
    }
}

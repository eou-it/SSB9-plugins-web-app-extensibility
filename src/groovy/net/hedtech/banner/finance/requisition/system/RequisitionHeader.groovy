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
 * Class for Requisiton Header
 */
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_HEADER_FINDER_BY_REQUEST_CODE,
                query = """FROM RequisitionHeader a
             WHERE a.requestCode = :requestCode
   """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_HEADER_FINDER_BY_USER,
                query = """FROM RequisitionHeader a
                     WHERE a.userId = :userId
                    order by lastModified desc
           """)
])

/**
 *  Requisition Header Domain object to store the details of requisition header information
 */
@Entity
@Table(name = FinanceProcurementConstants.REQUISITION_HEADER_VIEW)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@DatabaseModifiesState

class RequisitionHeader implements Serializable {

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
     * Last Modified By column for FPBREQH
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_USER_ID)
    String userId

    /**
     * REQUISITION DATE:
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQH_DATE)
    @Temporal(TemporalType.DATE)
    Date requestDate

    /**
     * TRANSACTION DATE:  The date the transaction was processed.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TRANS_DATE)
    @Temporal(TemporalType.DATE)
    Date transactionDate

    /**
     * INSTITUTION NAME:  The name of the separate accountable institution.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_NAME)
    String institutionName

    /**
     * PHONE AREA:  The telephone area code for this particular record.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PHONE_AREA)
    String phoneArea

    /**
     * SHIP TO PHONE NUMBER:  The ship to phone number.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PHONE_NUM)
    String shipToPhoneNumber

    /**
     * PHONE EXTENSION:  The telephone extension number for this particular record.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PHONE_EXT)
    String phoneExtension

    /**
     * VENDOR PERSONAL IDENTIFICATION MASTER:  The unique personal identification master of a vendor.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VEND_PIDM)
    Integer vendorPidm

    /**
     * ACCOUNT TYPE CODE:  Classifies an account type i.e., asset, liabilities, control, fund balance, revenue, and labor expenses are used for reporting       purposes.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ATYP_CODE)
    String accountType

    /**
     * ACCOUNT TYPE SEQUENCE NUMBER:  The internal account type that is predefined on the FTVSDAT table.  This number is used for collecting data on financial     reports.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ATYP_SEQ_NUM)
    Integer accountTypeSequenceNumber

    /**
     * CHART OF ACCOUNTS CODE:  The primary identification code for any chart of accounts that uniquely identifies that chart from any other in a multi-chart    environment.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COAS_CODE)
    String chartOfAccount

    /**
     * ORGANIZATION CODE:  Identifies the organization code that appears on a transaction.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ORGN_CODE)
    String organization

    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQD_DATE)
    @Temporal(TemporalType.DATE)
    Date deliveryDate

    /**
     * COMPLETE INDICATOR:  Indicates if the document has been completed.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COMPLETE_IND)
    Boolean completeIndicator

    /**
     * PRINT INDICATOR
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_PRINT_IND)
    String printIndicator

    /**
     * ENCUMBRANCE INDICATOR:
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ENCUMB_IND)
    String encumbranceIndicator

    /**
     * SUSPENSE INDICATOR:  An indicator used to inform the user that a document is being held in suspense due to missing or erroneous data content.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SUSP_IND)
    Boolean suspenseIndicator

    /**
     * CANCEL INDICATOR:  The indicator associated with the check cancellation processing.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_IND)
    String cancelIndicator

    /**
     * CANCELLATION DATE:  The date that a check was cancelled.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_DATE)
    @Temporal(TemporalType.DATE)
    Date cancellationDate

    /**
     * POSTING DATE:
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_POST_DATE)
    @Temporal(TemporalType.DATE)
    Date postingDate

    /**
     * APPROVAL INDICATOR:  An indicator that tells whether the document has been approved. This value is set to Y on final approval.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_APPR_IND)
    Boolean approvalIndicator

    /**
     * TEXT INDICATOR:  The view text or enter text indicator.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TEXT_IND)
    Boolean textIndicator

    /**
     * DEFER EDIT INDICATOR:  This indicator is used to defer editing of the transaction until posting.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_EDIT_DEFER_IND)
    Boolean deferEditIndicator

    /**
     * Header recommended vendor name
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_RECOMM_VEND_NAME)
    String recommVendName

    /**
     * CURRENCY CODE: The currency code associated with this requisition.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CURR_CODE)
    String currency

    /**
     * NSF ON OFF INDICATOR: If Y, available balance checking will be done at entry time for this particular requisition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_NSF_ON_OFF_IND)
    Boolean nsfOnOffIndicator

    /**
     * SINGLE ACCTG DISTR INDICATOR: If Y, the document will have an accounting or accounting distributions for the document not for specific commodities
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SINGLE_ACCTG_IND)
    Boolean singleAcctgDistrIndicator

    /**
     * CLOSED INDICATOR: If Y, all items for this requisition are closed, and no more activity will be done against this requisition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_IND)
    Boolean closedIndicator

    /**
     * SHIP CODE: The ship_to code of the desired delivery location. Validated against the Ship To Validation table (FTVSHIP).
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_SHIP_CODE)
    String ship

    /**
     * REQUEST TYPE INDICATOR: Type which defines the kind of requisition this document is.  Valid types are (P)rocurement and (S)tores requisition.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_RQST_TYPE_IND)
    String requestTypeIndicator

    /**
     * INVENTORY REQUISITION INDICATOR: Indicator set by the com for an inventory requisition.  Valid values are (Y) Inventory Requisition and (NULL) otherwise. The value is determined by the use of General Ledger Accounts on the requisition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_INVENTORY_REQ_IND)
    Boolean inventoryRequisitionIndicator

    /**
     * REASON CODE: Cancel reason code.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CRSN_CODE)
    String reason

    /**
     * DELIVERY COMMENT: Comments for delivery date.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DELIVERY_COMMENT)
    String deliveryComment

    /**
     * EMAIL ADDRESS: Requestor email address.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_EMAIL_ADDR)
    String emailAddress

    /**
     * FAX AREA CODE: Fax number area code.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_FAX_AREA)
    String faxArea

    /**
     * FAX NUMBER: Ship To fax telephone number.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_FAX_NUMBER)
    String faxNumber

    /**
     * FAX EXTENTION: Fax number extension.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_FAX_EXT)
    String faxExtension

    /**
     * ATTENTION TO: Attention To line for ship to address.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ATTENTION_TO)
    String attentionTo

    /**
     * VENDOR CONTACT: The vendor contact.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VENDOR_CONTACT)
    String vendorContact

    /**
     * DISCOUNT CODE: Discount code assigned to vendor.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DISC_CODE)
    String discount

    /**
     * EMAIL ADDRESS: Requestor email address.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VEND_EMAIL_ADDR)
    String vendorEmailAddress

    /**
     * DOCUMENT COPIED FROM: Number of requisition document copied.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_COPIED_FROM)
    String documentCopiedFrom

    /**
     * TAX GROUP CODE: The tax group code used with a ship-to-address to determine the taxes to be computed.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_TGRP_CODE)
    String taxGroup

    /**
     * REQUISITION PRINT DATE: The date on which the requisition was printed.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_REQ_PRINT_DATE)
    @Temporal(TemporalType.DATE)
    Date requisitionPrintDate

    /**
     * CLOSED DATE: The date that the requisition is closed.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_DATE)
    @Temporal(TemporalType.DATE)
    Date closedDate

    /**
     * Match Required: Used to indicate whether Invoices associated with this Requisition (through Purchase Orders) will require Matching..  Valid values are [N]o matching, [Y]es matching, [U] for no matching at all set on FOBSYSC.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_MATCH_REQUIRED)
    String matchRequired

    /**
     * REQUISITION ORIGINATION CODE: This will be set to EPROC-SYSTEM CODE if the eRequisition has been created via an eprocurement com.  The default is BANNER or SELF_SERVICE.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_ORIGIN_CODE)
    String requisitionOrigination

    /**
     * REQUISITION REFERENCE CODE: This code is the third party eproc com requisition number.  Banner will always create a new requisition number and store this reference.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DOC_REF_CODE)
    String requisitionReference

    /**
     * TELEPHONE COUNTRY CODE: Code designating the region or country.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_PHONE)
    String telephoneCountry

    /**
     * FAX COUNTRY CODE: Code designating the region or country.
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_FAX)
    String faxCountry

    /**
     * Version column which is used as an optimistic lock token for FPBREQH
     */
    @Version
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VERSION)
    Long version

    /**
     * Data Origin column for FPBREQH
     */
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_DATA_ORIGIN)
    String dataOrigin

    static constraints = {
        requestCode( nullable: false, maxSize: 8 )
        lastModified( nullable: true )
        userId( nullable: true, maxSize: 30 )
        requestDate( nullable: false )
        transactionDate( nullable: false )
        institutionName( nullable: true, maxSize: 35 )
        phoneArea( nullable: true, maxSize: 6 )
        shipToPhoneNumber( nullable: true, maxSize: 12 )
        phoneExtension( nullable: true, maxSize: 10 )
        vendorPidm( nullable: true )
        accountType( nullable: true, maxSize: 2 )
        accountTypeSequenceNumber( nullable: true )
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
        singleAcctgDistrIndicator( nullable: true )
        closedIndicator( nullable: true )
        ship( nullable: true, maxSize: 6 )
        requestTypeIndicator( nullable: false, maxSize: 1, inList: ["P", "S"] )
        inventoryRequisitionIndicator( nullable: true )
        reason( nullable: true, maxSize: 4 )
        deliveryComment( nullable: true, maxSize: 30 )
        emailAddress( nullable: true, maxSize: 128 )
        faxArea( nullable: true, maxSize: 6 )
        faxNumber( nullable: true, maxSize: 12 )
        faxExtension( nullable: true, maxSize: 10 )
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
        faxCountry( nullable: true, maxSize: 4 )
        dataOrigin( nullable: true, maxSize: 30 )
    }


    public static readonlyProperties = ['requestCode']

    /**
     * Fetches the requisitionHeader by user
     * @userId
     * @param pagingParams
     * @return list of
     */
    public static def fetchByUser( userId, pagingParams ) {
        def headerList = RequisitionHeader.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_HEADER_FINDER_BY_USER ).setString(
                    FinanceProcurementConstants.REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_USER_ID, userId )
                    .setMaxResults( pagingParams.max )
                    .setFirstResult( pagingParams.offset )
                    .list()
        }
        return [list: headerList]
    }

    /**
     * Fetches the requisitionHeader by request Code
     * @param requestCode
     * @return
     */
    public static RequisitionHeader fetchByRequestCode( requestCode ) {
        def requestHeader = RequisitionHeader.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_HEADER_FINDER_BY_REQUEST_CODE ).setString(
                    FinanceProcurementConstants.REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_REQUEST_CODE, requestCode ).list()[0]
        }
        return requestHeader
    }
}

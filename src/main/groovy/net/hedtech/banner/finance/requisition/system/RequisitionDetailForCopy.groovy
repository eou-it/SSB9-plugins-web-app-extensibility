/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 *  Persistence class for Requisition Detail (FPVREQD).
 */
@Entity
@Table(name = FinanceProcurementConstants.FPRREQD_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
class RequisitionDetailForCopy implements Serializable {

    /**
     * Surrogate ID for FPRREQD
     */
    @Id
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_SURROGATE_ID)
    @SequenceGenerator(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_SEQ_GEN, allocationSize = 1,
            sequenceName = FinanceProcurementConstants.FIELD_FPRREQD_COPY_SURROGATE_ID_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.FIELD_FPRREQD_COPY_SEQ_GEN)
    Long id

    /**
     * REQUISITION HEADER CODE:  The user specified or system generated number which identifies the
     * requisition document.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_REQH_CODE)
    String requestCode

    /**
     * ITEM:  The system generated item number which specifies the commodity within the requisition document.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_ITEM)
    Integer item

    /**
     * Activity Date
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_ACTIVITY_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last Modified By column for FPRREQD
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_USER_ID)
    String userId

    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_COMM_CODE)
    String commodity

    /**
     * COMMODITY DESCRIPTION : The user-defined commodity name/classification/category for this description.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_COMM_DESC)
    String commodityDescription

    /**
     * CHART OF ACCOUNTS:  The primary identification code for any of the chart of accounts which uniquely
     * identifies that chart froma any other in a multi-chart  environment.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_COAS_CODE)
    String chartOfAccount

    /**
     * organization CODE:  Identifies the individual organization code which appears  on a transaction.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_ORGN_CODE)
    String organization


    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_BUYR_CODE)
    String buyer

    /**
     * QUANTITY:  The quantity requested for the specific line item.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_QTY)
    BigDecimal quantity

    /**
     * UNIT OF MEASURE CODE:  The unit-of-measure applicable to the selected commodity.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_UOMS_CODE)
    String unitOfMeasure

    /**
     * UNIT PRICE:  Unit price for the requisition item.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_UNIT_PRICE)
    BigDecimal unitPrice

    /**
     * AGREEMENT CODE:  A unique code which identifies an established agreement with a vendor.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_AGRE_CODE)
    String agreement

    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_REQD_DATE)
    @Temporal(TemporalType.DATE)
    Date deliveryDate

    /**
     * SHIP CODE:  The ship to address within the institution to which the goods are to be delivered.
     * Must exist on FTVSHIP.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_SHIP_CODE)
    String ship

    /**
     * VENDOR PIDM:  The personal identification number for the vendor.  Vendor is maintained on FTVVEND.
     * Vendor PIDM is maintained on SATURN.SPRIDEN.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_VEND_PIDM)
    Integer vendorPidm

    /**
     * VENDOR REFERENCE NUMBER:  The vendor's part or item number for the commodity.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_VEND_REF_NUM)
    String vendorReferenceNumber

    /**
     * PROJECT CODE:  The optional project code for which this requisition was issued.  Must exist on FTVPROJ.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_PROJ_CODE)
    String project

    /**
     * PURCHASE ORDER CODE:  The user-defined purchase order number issued to this vendor.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_POHD_CODE)
    String purchaseOrder

    /**
     * PURCHASE ORDER ITEM:  The purchase order item number which is assigned to the specific requisition item.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_POHD_ITEM)
    Integer purchaseOrderItem

    /**
     * BIDS CODE:  A unique code which identifies a bid.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_BIDS_CODE)
    String bid

    /**
     * COMPLETE INDICATOR:  Indicates if the document has been completed.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_COMPLETE_IND)
    String completeIndicator

    /**
     * SUSPENSE INDICATOR:  An indicator which tells the user that the requisition has been
     * suspended for missing or erroneous data.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_SUSP_IND)
    Boolean suspenseIndicator

    /**
     * CANCELLATION INDICATOR:  A indicator that the requisition was cancelled.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CANCEL_IND)
    Boolean cancellationIndicator

    /**
     * CANCELLATION DATE:  The date when the requisition was cancelled.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CANCEL_DATE)
    @Temporal(TemporalType.DATE)
    Date cancellationDate

    /**
     * CLOSED INDICATOR:  An indicator which flags the requisition as closed once it has been received.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CLOSED_IND)
    Boolean closedIndicator

    /**
     * POST DATE:  The date on which the requisition was posted to the financial ledgers.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_POST_DATE)
    @Temporal(TemporalType.DATE)
    Date postDate

    /**
     * TEXT USAGE INDICATOR:  Indicates whether or not text has been used for the specific item.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_TEXT_USAGE)
    String textUsageIndicator

    /**
     * Address type code for commodity vendor
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_ATYP_CODE)
    String atyp

    /**
     * Address sequence number for commodity vendor
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_ATYP_SEQ_NUM)
    Integer atypSeqNum

    /**
     * Commodity recommended vendor name
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_RECOMM_VEND_NAME)
    String recommVendName

    /**
     * CURRENCY CODE: The currency code associated with this commodity record.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CURR_CODE)
    String currency

    /**
     * CONVERTED UNIT PRICE: The base unit price calculated for this commodity record.
     * Used when a currency code is established for this commodity record.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CONVERTED_UNIT_PRICE)
    BigDecimal convertedUnitPrice

    /**
     * DISCOUNT AMOUNT: The dollar value of the discount being taken on the invoice.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_DISC_AMT)
    BigDecimal discountAmount

    /**
     * TAX AMOUNT: The amount of tax computed or entered for a commodity.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_TAX_AMT)
    BigDecimal taxAmount

    /**
     * ADDITIONAL CHARGE AMOUNT: The amount of additional charges for the commodity record.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_ADDL_CHRG_AMT)
    BigDecimal additionalChargeAmount

    /**
     * CONVERTED DISCOUNT AMOUNT: The base discount amount calculated for this accounting record.
     * Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CONVERT_DISC_AMT)
    BigDecimal convertedDiscountAmount

    /**
     * CONVVERTED TAX AMOUNT: The base tax amount calculated for this accounting record.
     * Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CONVERT_TAX_AMT)
    BigDecimal convertedTaxAmount

    /**
     * CONVERTED ADDITIONAL CHARGE AMOUNT: The base additional charge amount calculated for this accounting record.
     * Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_CONVERT_ADDL_CHRG_AMT)
    BigDecimal convertedAdditionalChargeAmount

    /**
     * TAX GROUP: The tax group code used with a ship-to-address to determine the taxes to be computed.
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_TGRP_CODE)
    String taxGroup

    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_AMT)
    BigDecimal amt

    /**
     * DESCRIPTION CHANGED INDICATOR: Indicates when the default commodity description has been overwritten
     * in the requisition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_DESC_CHGE_IND)
    Boolean descriptionChangedIndicator

    /**
     * Version column which is used as an optimistic lock token for FPRREQD
     */
    @Version
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_VERSION)
    Long version

    /**
     * Data Origin column for FPRREQD
     */
    @Column(name = FinanceProcurementConstants.FIELD_FPRREQD_COPY_DATA_ORIGIN)
    String dataOrigin

    static constraints = {
        importFrom RequisitionDetail
    }


}

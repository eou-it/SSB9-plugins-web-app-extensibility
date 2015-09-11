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
 *  Request Accounting Table
 */
@Entity
@Table(name = FinanceProcurementConstants.FPRREQA_TABLE)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
class RequisitionAccountingForCopy implements Serializable {

    /**
     * Surrogate ID for FPRREQA
     */
    @Id
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_SURROGATE_ID)
    @SequenceGenerator(name = FinanceProcurementConstants.REQ_ACC_GEN_FPRREQA_SEQ_GEN, allocationSize = 1,
            sequenceName = FinanceProcurementConstants.REQ_ACC_SEQ_FPRREQA_SURROGATE_ID_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.REQ_ACC_GEN_FPRREQA_SEQ_GEN)
    Long id

    /**
     * REQUISTION CODE:  The unique identifier of a requisition document in the        system.  The code may be system generated by entering NEXT in the code field.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_REQH_CODE)
    String requestCode

    /**
     * ITEM:  The system generated item number which specifies the commodity on the    requisition.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ITEM)
    Integer item

    /**
     * SEQUENCE NUMBER:  A system generated sequence number relating to transaction    within a document.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_SEQ_NUM)
    Integer sequenceNumber

    /**
     * Activity Date
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ACTIVITY_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last Modified By column for FPRREQA
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_USER_ID)
    String userId

    /**
     * PERCENTAGE:  The discount percentage.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_PCT)
    BigDecimal percentage

    /**
     * REQUISITION AMOUNT:  The dollar amount for the specific requisition accounting  sequence.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_AMT)
    BigDecimal requisitionAmount

    /**
     * FISCAL YEAR CODE:  The unique two digit numeric identifier code, for the        fiscal year being set-up.  Ususally the last two digits of the second year of   the accounting period.  For example, FY90-91 = 91.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_FSYR_CODE)
    String fiscalYear

    /**
     * PERIOD:  Refers to an accounting period within a user-defined fiscal year.      Expressed as an numeric value i.e., 01, 06, 12 or 13.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_PERIOD)
    String period

    /**
     * RULE CLASS CODE: The four character alpha/numeric code identifying an           accounting transaction rule class.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_RUCL_CODE)
    String ruleClass

    /**
     * CHART OF ACCOUNTS CODE:  The primary identification code for any chart of       accounts that uniquely identifies that chart from any other in a multi-chart    environment.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_COAS_CODE)
    String chartOfAccount

    /**
     * ACCOUNT INDEX CODE:  The user-defined value representing a summerization of     any combination of user-defined FOAPAL elements.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ACCI_CODE)
    String accountIndex

    /**
     * FUND CODE:  A code which uniquely identifies a fiscal entity.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_FUND_CODE)
    String fund

    /**
     * ORGANIZATION CODE:  Identifies the individual organization code which appears   on a transaction.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ORGN_CODE)
    String organization

    /**
     * ACCOUNT CODE:  The user-defined value representing an account.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ACCT_CODE)
    String account

    /**
     * PROGRAM CODE:  The user-defined value to the program classification structure   code defined by the National Council of Higher Education Management Systems.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_PROG_CODE)
    String program

    /**
     * ACTIVITY CODE:  A user-defined code attribute which may be used to identify     accounting data by activity.  This is an optional field.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ACTV_CODE)
    String activity

    /**
     * LOCATION CODE: The physical location of the institution's financial manager.    For example, Vice President, Finance, Suite 10, Adams Hall, Main Campus.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_LOCN_CODE)
    String location

    /**
     * SUSPENSE INDICATOR:  Used to inform that a document is being held in suspense   dut to missing or erroneous data content.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_SUSP_IND)
    Boolean suspenseIndicator

    /**
     * NONSUFFICIENT FUNDS SUSPENSE INDICATOR.  An indicator which informs the user    that a transaction would result in an over budget condition.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_NSF_SUSP_IND)
    Boolean nsfSuspInd

    /**
     * CANCEL INDICATOR:  This indicator associated with the check cancellation        process.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CANCEL_IND)
    Boolean cancelIndicator

    /**
     * CANCELLATION DATE:  The date that the check was cancelled.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CANCEL_DATE)
    @Temporal(TemporalType.DATE)
    Date cancellationDate

    /**
     * PROJECT CODE:  A  unique code to identify a cost accounting project.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_PROJ_CODE)
    String project

    /**
     * APPROVAL INDICATOR:  An indicator which tells whether the document has been     approved.  This value is set to Y on final approval.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_APPR_IND)
    Boolean approvalIndicator

    /**
     * INSUFFICENT FUNDS OVERRIDE INDICATOR:  This indicator permits authorized        users to by-pass funding deficiencies.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_NSF_OVERRIDE_IND)
    Boolean insufficientFundsOverrideIndicator

    /**
     * AVAILABLE BUDGET OVERRIDE:  This indicator is used to override the availability  of funds checking process, if the user has the authority.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ABAL_IND)
    String availableBudgetOverride

    /**
     * CONVERTED AMOUNT: The base amount calculated for this accounting record. Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CONVERTED_AMT)
    BigDecimal convertedAmount

    /**
     * CLOSED INDICATOR: If Y, this accounting distribution is closed to further activity.
     */
    @Type(type = "yes_no")
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CLOSED_IND)
    Boolean closedIndicator

    /**
     * DISCOUNT AMOUNT: The dollar value of the discount being taken on the invoice.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_DISC_AMT)
    BigDecimal discountAmount

    /**
     * TAX AMOUNT: The amount of tax computed or entered for a commodity.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_TAX_AMT)
    BigDecimal taxAmount

    /**
     * ADDITIONAL CHARGE AMOUNT: The amount of additional charges for the commodity record.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ADDL_CHRG_AMT)
    BigDecimal additionalChargeAmount

    /**
     * CONVERTED DISCOUNT AMOUNT: The base discount amount calculated for this accounting record.  Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CONVERT_DISC_AMT)
    BigDecimal convertedDiscountAmount

    /**
     * CONVVERTED TAX AMOUNT: The base tax amount calculated for this accounting record. Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CONVERT_TAX_AMT)
    BigDecimal convvertedTaxAmount

    /**
     * CONVERTED ADDITIONAL CHARGE AMOUNT:The base additional charge amount calculated for this accounting record.  Used when currency code is established at the header record.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_CONVERT_ADDL_CHRG_AMT)
    BigDecimal convertedAdditionalChargeAmount

    /**
     * DISCOUNT AMOUNT PERCENT: The percentage which is entered in the discount amount percent field on the Requisition.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_DISC_AMT_PCT)
    BigDecimal discountAmountPercent

    /**
     * ADDITIONAL CHARGE AMOUNT:  The percentage which is entered in the additional charge amount percent field on the Requisition.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ADDL_AMT_PCT)
    BigDecimal additionalChargeAmountPct

    /**
     * TAX AMOUNT PERCENT:  The percentage which is entered in the tax amount percent field on the Requisition.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_TAX_AMT_PCT)
    BigDecimal taxAmountPercent

    /**
     * DISCOUNT RULE CLASS CODE: The rule class code which is used for posting discounts to the ledgers.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_DISC_RUCL_CODE)
    String discountRuleClass

    /**
     * TAX RULE CLASS CODE: The rule class code which is used for posting the accounting event for taxes.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_TAX_RUCL_CODE)
    String taxRuleClass

    /**
     * ADDITIONAL CHARGE RULE CLASS CODE: he rule class code which will be posted to the ledgers as a result of purchasing accounts payable activity.
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_ADDL_RUCL_CODE)
    String additionalChargeRuleClass

    /**
     * LIQUIDATION RULE CLASS CODE:
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_RUCL_CODE_LIQ)
    String liquidationRuleClass

    /**
     * Version column which is used as an optimistic lock token for FPRREQA
     */
    @Version
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_VERSION)
    Long version

    /**
     * Data Origin column for FPRREQA
     */
    @Column(name = FinanceProcurementConstants.REQ_ACC_FIELD_FPRREQA_DATA_ORIGIN)
    String dataOrigin

    static constraints = {
        requestCode( nullable: false, maxSize: 8 )
        item( nullable: false )
        sequenceNumber( nullable: false )
        lastModified( nullable: true )
        userId( nullable: true, maxSize: 30 )
        percentage( nullable: true )
        requisitionAmount( nullable: true )
        fiscalYear( nullable: true, maxSize: 2 )
        period( nullable: true, maxSize: 2 )
        ruleClass( nullable: true, maxSize: 4 )
        chartOfAccount( nullable: true, maxSize: 1 )
        accountIndex( nullable: true, maxSize: 6 )
        fund( nullable: true, maxSize: 6 )
        organization( nullable: true, maxSize: 6 )
        account( nullable: true, maxSize: 6 )
        program( nullable: true, maxSize: 6 )
        activity( nullable: true, maxSize: 6 )
        location( nullable: true, maxSize: 6 )
        suspenseIndicator( nullable: true )
        nsfSuspInd( nullable: true )
        cancelIndicator( nullable: true )
        cancellationDate( nullable: true )
        project( nullable: true, maxSize: 8 )
        approvalIndicator( nullable: true )
        insufficientFundsOverrideIndicator( nullable: true )
        availableBudgetOverride( nullable: true, maxSize: 1 )
        convertedAmount( nullable: true )
        closedIndicator( nullable: true )
        discountAmount( nullable: true )
        taxAmount( nullable: true )
        additionalChargeAmount( nullable: true )
        convertedDiscountAmount( nullable: true )
        convvertedTaxAmount( nullable: true )
        convertedAdditionalChargeAmount( nullable: true )
        discountAmountPercent( nullable: true )
        additionalChargeAmountPct( nullable: true )
        taxAmountPercent( nullable: true )
        discountRuleClass( nullable: true, maxSize: 4 )
        taxRuleClass( nullable: true, maxSize: 4 )
        additionalChargeRuleClass( nullable: true, maxSize: 4 )
        liquidationRuleClass( nullable: true, maxSize: 4 )
        dataOrigin( nullable: true, maxSize: 30 )
    }

    public static readonlyProperties = ['requestCode', 'item', 'sequenceNumber']

    /**
     * Method to fetch requisition accounting by requestCode, ite and sequenceNumber.
     * @param requestCode Request code.
     * @param item Item.
     * @param sequenceNumber Sequence Number.
     * @return RequisitionAccounting.
     */
    static def fetchByRequestCodeItemAndSeq( requestCode, Integer item, Integer sequenceNumber ) {
        def requestAccounting = RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_BY_CODE )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .setInteger( FinanceProcurementConstants.QUERY_PARAM_REQUISITION_ACCOUNTING_ITEM, item )
                    .setInteger( FinanceProcurementConstants.QUERY_PARAM_REQUISITION_ACCOUNTING_SEQ_NUM, sequenceNumber )
                    .list()
        }
        return [list: requestAccounting]
    }

    /**
     * This method is used to called named query for get last sequence number generated in requisition accounting.
     * @return last generated sequence number.
     */
    static def fetchLastSequenceNumberByRequestCode( requestCode, int item ) {
        def lastSequenceNumber = RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_GET_LAST_SEQ )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .setInteger( FinanceProcurementConstants.QUERY_PARAM_REQUISITION_DETAIL_ITEM, item )
                    .list()
        }
        return lastSequenceNumber
    }

    /**
     *
     * @param requestCode
     * @param item
     * @return
     */
    static def getSplittingPercentage( requestCode, int item ) {
        def lastSequenceNumber = RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_GET_SUM_PERCENTAGE )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .setInteger( FinanceProcurementConstants.QUERY_PARAM_REQUISITION_DETAIL_ITEM, item )
                    .list()
        }
        return lastSequenceNumber
    }

    /**
     * This method is used to called named query for get last sequence number generated in requisition accounting.
     * @return last generated item number.
     */
    static def fetchLastItemNumberByRequestCode( requestCode ) {
        def lastItemNumber = RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_GET_LAST_ITEM )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .list()
        }
        return lastItemNumber
    }

    /**
     * List accounting by Request Code
     * @param requestCode
     * @return
     */
    static def findAccountingByRequestCode( requestCode ) {
        RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_BY_REQUEST_CODE )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .list()
        }
    }

    /**
     * This method is used to fetch the requisition detail list by user.
     * @param userId User id.
     * @param paginationParams pagination parameters.
     * @return List of requisition details.
     */
    static def fetchByUserId( userId, paginationParams ) {
        def requestAccountingList = RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_BY_USER )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_USER_ID, userId )
                    .setMaxResults( paginationParams.max )
                    .setFirstResult( paginationParams.offset )
                    .list()
        }
        return [list: requestAccountingList]
    }

    /**
     * List accounting by Request Code and Item
     * @param requestCode
     * @param item
     * @return
     */
    static def findAccountingByRequestCodeAndItem( requestCode, item ) {
        RequisitionAccounting.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQ_ACC_NAMED_QUERY_BY_REQUEST_CODE_AND_ITEM )
                    .setString( FinanceProcurementConstants.QUERY_PARAM_REQUEST_CODE, requestCode )
                    .setInteger( FinanceProcurementConstants.QUERY_PARAM_REQUISITION_DETAIL_ITEM, item )
                    .list()
        }
    }
}

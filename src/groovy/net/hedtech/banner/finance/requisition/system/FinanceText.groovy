/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants

import javax.persistence.*

/**
 * The persistent class for the FOBTEXT database table.
 *
 */
@Entity
@Table(name = FinanceProcurementConstants.FINANCE_TEXT_TABLE)
@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_LIST_ALL_FINANCE_TEXT_BY_CODE,
                query = """FROM FinanceText financeText
                                    WHERE financeText.textCode = :textCode
                                    AND financeText.documentTypeSequenceNumber = 1 """),
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO,
                query = """FROM FinanceText financeText
                            WHERE financeText.textCode = :textCode
                            AND financeText.textItem = :textItem
                            AND financeText.documentTypeSequenceNumber = 1
                            ORDER BY financeText.sequenceNumber ASC"""),
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO_AND_PRINT_IND,
                query = """FROM FinanceText financeText
                            WHERE financeText.textCode = :textCode
                            AND financeText.textItem = :textItem
                            AND financeText.printOptionIndicator = :printOptionIndicator
                            AND financeText.documentTypeSequenceNumber = 1
                            ORDER BY financeText.sequenceNumber ASC"""),
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE_AND_PRINT_OPTION_IND,
                query = """FROM FinanceText financeText
                            WHERE financeText.textCode = :textCode
                            AND financeText.textItem IS NULL
                            AND financeText.printOptionIndicator = :printOptionIndicator
                            AND financeText.documentTypeSequenceNumber = 1
                            ORDER BY financeText.sequenceNumber ASC"""),
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE,
                query = """FROM FinanceText financeText
                            WHERE financeText.textCode = :textCode
                            AND financeText.textItem IS NULL
                            AND financeText.documentTypeSequenceNumber = 1"""),
        @NamedQuery(name = FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_DTYP_SEQ_AND_CODE_AND_PRINT_IND ,
        query = """FROM FinanceText financeText
                            WHERE financeText.documentTypeSequenceNumber = :dtypSeqNum
                            AND financeText.textCode = :textCode
                            AND financeText.printOptionIndicator = :printOptionIndicator
                            ORDER BY financeText.sequenceNumber ASC""")
])
@ToString(includeNames = true, ignoreNulls = true)
@EqualsAndHashCode(includeFields = true)
class FinanceText implements Serializable {
    @Id
    @SequenceGenerator(name = FinanceProcurementConstants.FINANCE_TEXT_SEQ_GEN,
            sequenceName = FinanceProcurementConstants.FINANCE_TEXT_SURROGATE_ID_SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FinanceProcurementConstants.FINANCE_TEXT_SEQ_GEN)
    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_SURROGATE_ID)
    Long id

    @Temporal(TemporalType.DATE)
    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_ACTIVITY_DATE)
    Date activityDate

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_CHG_SEQ_NUM)
    Integer changeSequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_CLA_NUM)
    String classNumber

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_CODE)
    String textCode

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_DATA_ORIGIN)
    String dataOrigin

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_DTYP_SEQ_NUM)
    Integer documentTypeSequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_ITEM)
    Integer textItem

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_PIDM)
    Integer pidm

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_PRT_IND)
    String printOptionIndicator

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_SEQ_NUM)
    Integer sequenceNumber

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_TEXT)
    String text

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_USER_ID)
    String lastModifiedBy

    @Version
    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_VERSION)
    Long version

    @Column(name = FinanceProcurementConstants.FINANCE_TEXT_FIELD_FOBTEXT_VPDI_CODE)
    String vpdiCode

    static constraints = {
        activityDate( nullable: false )
        changeSequenceNumber( nullable: true )
        classNumber( nullable: true, maxSize: 8 )
        textCode( nullable: false, maxSize: 20 )
        dataOrigin( nullable: true, maxSize: 30 )
        documentTypeSequenceNumber( nullable: false )
        textItem( nullable: true )
        pidm( nullable: true )
        printOptionIndicator( nullable: false, maxSize: 1 )
        sequenceNumber( nullable: false )
        text( nullable: true, maxSize: 50 )
        lastModifiedBy( nullable: false, maxSize: 30 )
        version( nullable: true )
        vpdiCode( nullable: true, maxSize: 6 )
    }

    /**
     * Method is used to get FinanceText by text code, commodity item number and print option indicator.
     * @param textCode Text code.
     * @param sequenceNumber Commodity Sequence number.
     * @param printOptionIndicator Print option indicator.
     * @return list of finance text.
     */
    static def getFinanceTextByCodeAndItemNumber( textCode, Integer item ) {
        def list = FinanceText.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_CODE, textCode )
                    .setInteger( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_ITEM, item )
                    .list()
        }
        return list
    }

    /**
     * Method is used to get FinanceText by text code, commodity item number and print option indicator.
     * @param textCode Text code.
     * @param sequenceNumber Commodity Sequence number.
     * @param printOptionIndicator Print option indicator.
     * @return list of finance text.
     */
    static def getFinanceTextByCodeAndItemNumberAndPrintInd( textCode, Integer item, printOptionIndicator ) {
        def list = FinanceText.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO_AND_PRINT_IND )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_CODE, textCode )
                    .setInteger( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_ITEM, item )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_PRINT_INDICATOR, printOptionIndicator )
                    .list()
        }
        return list
    }

    /**
     * Method is used to get FinanceText by text code, commodity item number and print option indicator.
     * @param textCode Text code.
     * @param sequenceNumber Commodity Sequence number.
     * @param printOptionIndicator Print option indicator.
     * @return list of finance text.
     */

    static def getFinanceTextByDocumentTypeAndCodeAndPrintInd( Integer dtypSeqNum, textCode, printOptionIndicator ) {
        def list = FinanceText.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_DTYP_SEQ_AND_CODE_AND_PRINT_IND )
                    .setInteger( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_DTYP_SEQ_NUM, dtypSeqNum )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_CODE, textCode )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_PRINT_INDICATOR, printOptionIndicator )
                    .list()
        }
        return list
    }
    /**
     * Method is used to get Header FinanceText by text code and print option indicator.
     * @param textCode Text code.
     * @param printOptionIndicator Print option indicator.
     * @return list of finance text.
     */
    static def listHeaderLevelTextByCodeAndPrintOptionInd( textCode, printOptionIndicator ) {
        def list = FinanceText.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE_AND_PRINT_OPTION_IND )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_CODE, textCode )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_PRINT_INDICATOR, printOptionIndicator )
                    .list()
        }
        return list
    }

    /**
     * The method is used to list header level FinanceText by text code.
     * @param textCode Text Code.
     * @return List of FinanceText.
     */
    static def listHeaderLevelTextByCode( textCode ) {
        def list = FinanceText.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_CODE, textCode )
                    .list()
        }
        return list
    }

    /**
     * Method is used to get all finance text by text code.
     * @param textCode text code.
     * @return list of FinanceText.
     */
    static def listAllFinanceTextByCode( textCode ) {
        def list = FinanceText.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.FINANCE_TEXT_NAMED_QUERY_LIST_ALL_FINANCE_TEXT_BY_CODE )
                    .setString( FinanceProcurementConstants.FINANCE_TEXT_QUERY_PARAM_TEXT_CODE, textCode )
                    .list()
        }
        return list
    }

}

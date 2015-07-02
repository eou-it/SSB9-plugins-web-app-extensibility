/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.finance.requisition.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.service.DatabaseModifiesState

import javax.persistence.*

/**
 *  Persistence class for Requisition information view (FPVREQINFO).
 */

@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_STATUS,
                query = """FROM RequisitionInformation reqInfo
                            WHERE reqInfo.status in :status
                            AND reqInfo.lastModifiedBy = :userId
                            order by reqInfo.activityDate desc """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_COUNT_FINDER_BY_STATUS,
                query = """select count(reqInfo.id) FROM RequisitionInformation reqInfo
                                    WHERE reqInfo.status in :status
                                    AND reqInfo.lastModifiedBy = :userId """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_CODE_USER,
                query = """FROM RequisitionInformation reqInfo
                                           WHERE reqInfo.requisitionCode = :requisitionCode
                                           AND reqInfo.lastModifiedBy = :userId """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_SEARCH_PARAM,
                query = """FROM RequisitionInformation reqInfo
                            WHERE (UPPER(reqInfo.requisitionCode) LIKE :searchParam OR UPPER(reqInfo.organizationCode) LIKE :searchParam
                                    OR UPPER(reqInfo.organizationTitle) LIKE :searchParam OR UPPER(reqInfo.vendorName) LIKE :searchParam
                                    OR UPPER(reqInfo.amount) LIKE :searchParam OR UPPER(reqInfo.currency) LIKE :searchParam
                                    OR UPPER(reqInfo.status) LIKE :searchParam)
                            AND reqInfo.lastModifiedBy = :userId
                            order by reqInfo.activityDate desc """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_COUNT_FINDER_BY_SEARCH_PARAM,
                query = """select count(reqInfo.id) FROM RequisitionInformation reqInfo
                                    WHERE (UPPER(reqInfo.requisitionCode) LIKE :searchParam OR UPPER(reqInfo.organizationCode) LIKE :searchParam
                                            OR UPPER(reqInfo.organizationTitle) LIKE :searchParam OR UPPER(reqInfo.vendorName) LIKE :searchParam
                                            OR UPPER(reqInfo.amount) LIKE :searchParam OR UPPER(reqInfo.currency) LIKE :searchParam
                                            OR UPPER(reqInfo.status) LIKE :searchParam )
                                    AND reqInfo.lastModifiedBy = :userId """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_SEARCH_BY_TRANSACTION_DATE,
                query = """FROM RequisitionInformation reqInfo
                            WHERE TRUNC(reqInfo.transactionDate) = TRUNC(:searchParam)
                            AND reqInfo.lastModifiedBy = :userId
                            order by reqInfo.activityDate desc """),
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_SEARCH_COUNT_FINDER_BY_TRANSACTION_DATE,
                query = """select count(reqInfo.id) FROM RequisitionInformation reqInfo
                                    WHERE TRUNC(reqInfo.transactionDate) = TRUNC(:searchParam)
                                    AND reqInfo.lastModifiedBy = :userId """)
])
@Entity
@Table(name = FinanceProcurementConstants.VIEW_FVQ_REQ_DASHBOARD_INFO)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@DatabaseModifiesState
class RequisitionInformation implements Serializable {

    /**
     * Surrogate ID for FPVREQINFO
     */
    @Id
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_FPBREQH_CODE)
    String requisitionCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_TRANS_DATE)
    Date transactionDate
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_REQUEST_DATE)
    Date requestDate
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_DELIVERY_DATE)
    Date deliveryDate
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_ACTIVITY_DATE)
    Date activityDate

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_USER_ID)
    String lastModifiedBy

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_ORIGIN_CODE)
    String origin

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_CURR_CODE)
    String currency

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_AMOUNT)
    String amount

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_REQUESTOR_NAME)
    String requesterName

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_VEND_PIDM)
    String vendorPidm
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_VENDOR_NAME)
    String vendorName


    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_ORGN_CODE)
    String organizationCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_ORG_TITLE)
    String organizationTitle

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPBREQH_COAS_CODE)
    String coasCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_STATUS)
    String status

    @Version
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_VERSION)
    Long version

    /**
     * List all requisitions by user and specified status
     * @param userId
     * @param paginationParams
     * @param status
     * @return
     */
    static def listRequisitionsByStatus( userId, paginationParams, status ) {
        return RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_STATUS )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                    .setParameterList( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS, status )
                    .setMaxResults( paginationParams.max )
                    .setFirstResult( paginationParams.offset )
                    .list()
        }
    }

    /**
     * List number of all requisitions by user and specified status
     * @param userId
     * @param paginationParams
     * @param status
     * @return
     */
    static def fetchRequisitionsCountByStatus( userId, status ) {
        def requisitionsCount = RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_COUNT_FINDER_BY_STATUS )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                    .setParameterList( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS, status )
                    .list()
        }
        return requisitionsCount[0]
    }

    /**
     * Fetch Requisition for specified requisition no
     * @param userId
     * @param requisitionCode
     * @return
     */
    static def fetchRequisitionsByReqNumber( userId, requisitionCode ) {
        def requisitions = RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_CODE_USER )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                    .setParameterList( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_REQ_CODE, requisitionCode )
                    .list()
        }
        return requisitions[0]
    }

    /**
     * List all requisitions by user and specified search param
     * @param userId
     * @param paginationParams
     * @param status
     * @return
     */
    static def listRequisitionsBySearchParam( userId, searchParam, paginationParams ) {
        return RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_SEARCH_PARAM )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_SEARCH_PARAM, searchParam )
                    .setMaxResults( paginationParams.max )
                    .setFirstResult( paginationParams.offset )
                    .list()
        }
    }

    /**
     * count number of all requisitions by user and search param
     * @param userId
     * @param paginationParams
     * @param searchParam
     * @return
     */
    static def fetchRequisitionsCountBySearchParam( searchParam, userId ) {
        def requisitionsCount = RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_COUNT_FINDER_BY_SEARCH_PARAM )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_SEARCH_PARAM, searchParam )
                    .list()
        }
        return requisitionsCount[0]
    }

    /**
         * List all requisitions by user and specified search param
         * @param userId
         * @param paginationParams
         * @param searchParam
         * @return
         */
        static def listRequisitionsByTransactionDate( userId, searchParam, paginationParams ) {
            return RequisitionInformation.withSession {session ->
                session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_SEARCH_BY_TRANSACTION_DATE )
                        .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                        .setDate( FinanceProcurementConstants.REQUISITION_INFO_SEARCH_PARAM, searchParam )
                        .setMaxResults( paginationParams.max )
                        .setFirstResult( paginationParams.offset )
                        .list()
            }
        }

        /**
         * count number of all requisitions by user and search param as date
         * @param userId
         * @param paginationParams
         * @param searchParam
         * @return
         */
        static def fetchRequisitionsCountByTransactionDate( searchParam, userId ) {
            def requisitionsCount = RequisitionInformation.withSession {session ->
                session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_SEARCH_COUNT_FINDER_BY_TRANSACTION_DATE )
                        .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                        .setDate( FinanceProcurementConstants.REQUISITION_INFO_SEARCH_PARAM, searchParam )
                        .list()
            }
            return requisitionsCount[0]
        }
}

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
 *  Persistence class for Requisition information view (FPVREQLIST).
 */

@NamedQueries(value = [
        @NamedQuery(name = FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_STATUS,
                query = """FROM RequisitionInformation reqInfo
                            WHERE reqInfo.status in :status
                            AND reqInfo.lastModifiedBy = :userId
                            order by reqInfo.activityDate desc """)
])
@Entity
@Table(name = FinanceProcurementConstants.VIEW_FPVREQLIST)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@DatabaseModifiesState
class RequisitionInformation implements Serializable {

    /**
     * Surrogate ID for FPVREQLIST
     */
    @Id
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_FPBREQH_CODE)
    String requisitionCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_TRANS_DATE)
    String transactionDate
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_REQUEST_DATE)
    String requestDate
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_DELIVERY_DATE)
    String deliveryDate
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_ACTIVITY_DATE)
    String activityDate

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_USER_ID)
    String lastModifiedBy

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_ORIGIN_CODE)
    String origin

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_AMOUNT)
    String amount

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_REQUESTOR_NAME)
    String requesterName

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_VEND_PIDM)
    String vendorPidm
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_VENDOR_NAME)
    String vendorName


    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_ORGN_CODE)
    String organizationCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_ORG_TITLE)
    String organizationTitle

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_FPBREQH_COAS_CODE)
    String coasCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQLIST_STATUS)
    String status

    @Version
    @Column(name = FinanceProcurementConstants.REQUISITION_HEADER_FIELD_FPBREQH_VERSION)
    Long version

    /**
     *
     * @param userId
     * @param paginationParams
     * @param status
     * @return
     */
    static def listRequisitionsByStatus( userId, paginationParams, status ) {
        def requisitions = RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_STATUS )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
                    .setParameterList( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS, status )
                    .setMaxResults( paginationParams.max )
                    .setFirstResult( paginationParams.offset )
                    .list()
        }
        return [list: requisitions]
    }

}

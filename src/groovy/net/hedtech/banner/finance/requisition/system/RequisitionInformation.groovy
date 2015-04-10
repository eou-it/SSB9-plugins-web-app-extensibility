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
                            WHERE reqInfo.lastModifiedBy = :userId
                            order by reqInfo.activityDate desc """)

])
@Entity
@Table(name = FinanceProcurementConstants.VIEW_FPVREQINFO)
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, ignoreNulls = true)
@DatabaseModifiesState
class RequisitionInformation implements Serializable {

    /**
     * Surrogate ID for FPVREQINFO
     */
    @Id
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_SURROGATE_ID)
    Long id

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_FPBREQH_CODE)
    String requisitionCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_TRANS_DATE)
    Date transactionDate

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_REQUEST_DATE)
    Date requestDate

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_DELIVERY_DATE)
    Date deliveryDate

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_ACTIVITY_DATE)
    Date activityDate

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_USER_ID)
    String lastModifiedBy

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_ORIGIN_CODE)
    String origin

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_CURR_CODE)
    String currency

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_AMOUNT)
    String amount

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_REQUESTOR_NAME)
    String requesterName

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_VEND_PIDM)
    String vendorPidm
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_VENDOR_NAME)
    String vendorName


    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_ORGN_CODE)
    String organizationCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_ORG_TITLE)
    String organizationTitle

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_COAS_CODE)
    String coasCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_COMP_IND)
    String reqHeaderCompletedInd

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_APPR_IND)
    String reqHeaderApprovedInd

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FTVRQPO_REQD_CODE)
    String purchaseOrderReqDCode

    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_FPRREQD_BUYR_CODE)
    String reqDetailBuyerCode

    @Version
    @Column(name = FinanceProcurementConstants.REQUISITION_INFO_FIELD_FPVREQINFO_VERSION)
    Long version

    /**
     * Method to get the status of the requisition.
     * @return status property key.
     */
    def getStatus() {
        if (reqHeaderCompletedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                && reqHeaderApprovedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                && purchaseOrderReqDCode) {
            FinanceProcurementConstants.REQUISITION_INFO_STATUS_CONVERTED_TO_PO
        } else if (reqHeaderCompletedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                && reqHeaderApprovedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                && reqDetailBuyerCode) {
            FinanceProcurementConstants.REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER
        } else if (reqHeaderCompletedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                && reqHeaderApprovedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES) {
            FinanceProcurementConstants.REQUISITION_INFO_STATUS_COMPLETED
        } else if (reqHeaderCompletedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_YES
                && reqHeaderApprovedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_NO) {
            FinanceProcurementConstants.REQUISITION_INFO_STATUS_PENDING
        } else if (!reqHeaderCompletedInd
                && reqHeaderApprovedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_NO) {
            FinanceProcurementConstants.REQUISITION_INFO_STATUS_DISAPPROVED
        } else if (reqHeaderCompletedInd == FinanceProcurementConstants.DEFAULT_INDICATOR_NO) {
            FinanceProcurementConstants.REQUISITION_INFO_STATUS_DRAFT
        }
    }

    /**
     * List all requisitions by user and specified status
     * @param userId
     * @param paginationParams
     * @param status
     * @return list
     */
    static def listRequisitionsByUser( userId ) {
        def infoList = RequisitionInformation.withSession {session ->
            session.getNamedQuery( FinanceProcurementConstants.REQUISITION_INFO_FINDER_BY_STATUS )
                    .setString( FinanceProcurementConstants.REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID, userId )
//                    .setMaxResults( paginationParams.max )
//                    .setFirstResult( paginationParams.offset )
                    .list()
        }
        return infoList
    }

}

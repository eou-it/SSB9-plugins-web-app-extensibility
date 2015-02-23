/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * Service class for RequisitionDetail.
 *
 */
class RequisitionDetailService extends ServiceBase {
    static transactional = true
    def log = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * This method is used to find RequisitionDetail list by requisition code.
     * @param requisitionCode Requisition code.
     * @return List of requisition code.
     */
    def findRequisitionDetailListByRequistionCode( requisitionCode ) {
        log.debug( 'Input parameter for findRequisitionDetailByRequestionCode :' + requisitionCode )
        def requisitionDetails = RequisitionDetail.fetchByRequestCode( requisitionCode )
        return requisitionDetails
    }

    /**
     * This method is used to find RequisitionDetail by code and commodity code.
     * @param map Map which contains requisitionCode and commodityCode.
     * @return RequisitionDetail.
     */
    def findRequisitionDetailByCodeAndCommodityCode( map ) {
        def requisitionCode = map.requisitionCode
        def commodityCode = map.commodityCode
        return RequisitionDetail.fetchByRequestCodeAndCommodityCode( requisitionCode, commodityCode ).list[0]
    }

    /**
     * This method is used to find RequisitionDetail list for logged in user.
     * @param paginationParam Map containing Pagination parameters.
     * @return List of RequisitionDetail.
     */
    def findRequisitionDetailListByUser( paginationParam ) {
        def loggedInUser = springSecurityService.getAuthentication()?.user
        if (loggedInUser) {
            def oracleUserName = loggedInUser?.oracleUserName
            def requisitionDetailList = RequisitionDetail.fetchByUserId( oracleUserName, paginationParam )
            if (requisitionDetailList?.isEmpty()) {
                throw new ApplicationException(
                        RequisitionDetailService,
                        new BusinessLogicValidationException(
                                FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL, [] ) )
            }
            return requisitionDetailList
        } else {
            log.debug( 'User' + loggedInUser + ' is not valid' )
            throw new ApplicationException( RequisitionDetailService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_USER_NOT_VALID, [] ) )
        }
    }

    /**
     * This method is used get last inserted 'item' from RequisitionDetail.
     * @return last item.
     */
    def getLastItem () {
        def lastItem = 0
        if (!RequisitionDetail.getLastItem()?.empty) {
            lastItem = RequisitionDetail.getLastItem().getAt( 0 )
        }
        return lastItem
    }
}

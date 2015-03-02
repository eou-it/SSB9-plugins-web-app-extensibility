/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.service.ServiceBase
import org.apache.commons.lang.StringUtils
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
    def fetchByRequestCodeAndItem( requisitionCode, item, paginationParams ) {
        log.debug( 'Input parameter for fetchByRequestCodeAndItem :' + requisitionCode )
        if (StringUtils.isBlank( requisitionCode )) {
            requisitionCode = FinanceProcurementConstants.WILDCARD_STR_PERCENTAGE
        } else if (!(requisitionCode =~ /FinanceProcurementConstants.WILDCARD_STR_PERCENTAGE/)) {
            requisitionCode += FinanceProcurementConstants.WILDCARD_STR_PERCENTAGE
        }
        if (StringUtils.isBlank( item )) {
            item = FinanceProcurementConstants.WILDCARD_STR_PERCENTAGE
        } else if (!(item =~ /FinanceProcurementConstants.WILDCARD_STR_PERCENTAGE/)) {
            item += FinanceProcurementConstants.WILDCARD_STR_PERCENTAGE
        }
        def requisitionDetails = RequisitionDetail.fetchByRequestCodeAndItem( requisitionCode, item, paginationParams ).list
        if (requisitionDetails.isEmpty()) {
            throw new ApplicationException(
                    RequisitionDetailService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL, [] ) )
        }
        return requisitionDetails
    }

    /**
     * This method is used to find RequisitionDetail list for logged in user.
     * @param paginationParam Map containing Pagination parameters.
     * @return List of RequisitionDetail.
     */
    def findRequisitionDetailListByUser( paginationParam ) {
        def loggedInUser = springSecurityService.getAuthentication()?.user
        if (loggedInUser?.oracleUserName) {
            def oracleUserName = loggedInUser.oracleUserName
            def requisitionDetailList = RequisitionDetail.fetchByUserId( oracleUserName, paginationParam ).list
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
     * @param requestCode Requisition Code.
     * @return last item.
     */
    def getLastItem( requestCode ) {
        def lastItem = RequisitionDetail.getLastItem( requestCode ).getAt( 0 )
        return lastItem ? lastItem : 0
    }


    def getRequisitionDetailByRequestCodeAndItem( requestCode, item ) {
        def pagingParams = [max: 500, offset: 0]
        def requisitionDetail = RequisitionDetail.fetchByRequestCodeAndItem( requestCode, item, pagingParams ).list.getAt( 0 )
        if (!requisitionDetail) {
            log.debug( 'Requisition Detail Not found for Request Code :' + requestCode + ' and Item : ' + item )
            throw new ApplicationException( RequisitionDetailService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL, [] ) )
        }
        return requisitionDetail
    }

}

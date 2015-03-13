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
    private static final def LOGGER = Logger.getLogger( this.getClass() )
    def springSecurityService

    /**
     * This method is used to find RequisitionDetail list by requisition code.
     * @param requisitionCode Requisition code.
     * @return List of requisition code.
     */
    def findByRequestCodeAndItem( requisitionCode, Integer item ) {
        LOGGER.debug( 'Input parameter for findByRequestCodeAndItem :' + requisitionCode )
        def inputMap = [requisitionCode: requisitionCode, item: item]
        def requisitionDetails = RequisitionDetail.fetchByRequestCodeAndItem( inputMap.requisitionCode, inputMap.item ).list
        if (requisitionDetails.isEmpty()) {
            LOGGER.error( 'Requisition Commodity Details are empty for requestCode=' + requisitionCode )
            throw new ApplicationException(
                    RequisitionDetailService,
                    new BusinessLogicValidationException(
                            FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL, [] ) )
        }
        return requisitionDetails.getAt( 0 )
    }

    /**
     * This method is used to find RequisitionDetail list for logged in user.
     * @param paginationParam Map containing Pagination parameters.
     * @return List of RequisitionDetail.
     */
    def fetchRequisitionDetailListByUser( paginationParam ) {
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
            LOGGER.error( 'User' + loggedInUser + ' is not valid' )
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

    /**
     * Service method to get requisition detail by request code and item number.
     * @param requestCode Requisition code
     * @param item Item number.
     * @return Requisition Commodity Details.
     */
    def getRequisitionDetailByRequestCodeAndItem( requestCode, Integer item ) {
        def requisitionDetail = RequisitionDetail.fetchByRequestCodeAndItem( requestCode, item ).list.getAt( 0 )
        if (!requisitionDetail) {
            LOGGER.error( 'Requisition Detail Not found for Request Code :' + requestCode + ' and Item : ' + item )
            throw new ApplicationException( RequisitionDetailService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_MISSING_REQUISITION_DETAIL, [] ) )
        }
        return requisitionDetail
    }
}

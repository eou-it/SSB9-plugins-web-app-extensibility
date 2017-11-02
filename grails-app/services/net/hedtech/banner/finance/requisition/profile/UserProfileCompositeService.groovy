/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.profile

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.FinanceCommonUtility
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Class for User Profile Composite Service
 */
class UserProfileCompositeService {
    boolean transactional = false
    def financeUserProfileService
    def shipToCodeService
    def chartOfAccountsService
    def financeOrganizationCompositeService
    private static final Logger LOGGER = Logger.getLogger( this.class )

    /**
     * Fetches User profile and associated information
     * @param user
     * @param effectiveDate
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    def getUserProfileDetail( user, effectiveDate ) {
        def coa = [:], shipToCode = [:], org = [:], userProfile = [:]

        def userProfileObj = financeUserProfileService.getUserProfileByUserId( user.oracleUserName )
        userProfile = [userId           : userProfileObj.userId, requesterName: userProfileObj.requesterName, requesterOrgCode: userProfileObj.requesterOrgCode,
                       requesterShipCode: userProfileObj.requesterShipCode, requesterCaosCode: userProfileObj.requesterCaosCode, requesterEmailAddress: userProfileObj.requesterEmailAddress]
        if (userProfile.requesterShipCode) {
            try {
                def shipTo = shipToCodeService.findShipToCodesByCode( userProfile.requesterShipCode, effectiveDate )
                shipToCode = [zipCode     : shipTo.zipCode, state: shipTo.state, city: shipTo.city, shipCode: shipTo.shipCode, addressLine1: shipTo.addressLine1,
                              addressLine2: shipTo.addressLine2, addressLine3: shipTo.addressLine3, contact: shipTo.contact]
            } catch (ApplicationException ae) {
                LoggerUtility.error( LOGGER, FinanceCommonUtility.maskErrorMessage( ae ) )
            }
        }
        if (userProfile.requesterOrgCode) {
            try {
                def organization = financeOrganizationCompositeService.
                        findOrganizationListByEffectiveDateAndSearchParam( [searchParam: userProfile.requesterOrgCode, effectiveDate: effectiveDate, coaCode: userProfile.requesterCaosCode],
                                                                           [offset: FinanceProcurementConstants.ZERO, max: FinanceProcurementConstants.ONE] )
                org = [coaCode: organization[0].coaCode, orgnCode: organization[0].orgnCode, orgnTitle: organization[0].orgnTitle]
            } catch (ApplicationException ae) {
                LoggerUtility.error( LOGGER, FinanceCommonUtility.maskErrorMessage( ae ) )
            }
        }
        try {
            def coaObj = chartOfAccountsService.getChartOfAccountByCode( userProfile.requesterCaosCode, effectiveDate )
            coa = [title: coaObj.title, chartOfAccountsCode: coaObj.chartOfAccountsCode]
        } catch (ApplicationException ae) {
            LoggerUtility.error( LOGGER, FinanceCommonUtility.maskErrorMessage( ae ) )
        }
        return [baseUserProfile: userProfile, shipTo: shipToCode, organization: org, coa: coa];

    }
}

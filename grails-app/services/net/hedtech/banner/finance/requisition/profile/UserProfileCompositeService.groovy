/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.profile
/**
 * Class for User Profile Composite Service
 */
class UserProfileCompositeService {

    def springSecurityService
    def financeUserProfileService
    def shipToCodeService
    def chartOfAccountsService
    def financeOrganizationCompositeService

    /**
     * Fetches User profile and associated information
     * @param requestCode
     */
    def getUserProfileDetail( user ) {
        def userProfile = financeUserProfileService.getUserProfileByUserId( user?.oracleUserName )
        def shipTo = shipToCodeService.findShipToCodesByCode( userProfile.requesterShipCode )
        def organization = financeOrganizationCompositeService.
                findOrganizationListByEffectiveDateAndSearchParam( [searchParam: userProfile.requesterOrgCode, coaCode: userProfile.requesterCaosCode], [offset: 0, max: 1] )
        def coa = chartOfAccountsService.getChartOfAccountByCode( userProfile.requesterCaosCode )
        return [baseUserProfile: userProfile, shipTo: [id            : shipTo.id, version: shipTo.version, shipCode: shipTo.shipCode, zipCode: shipTo.zipCode, phoneArea: shipTo.phoneArea,
                                                       phoneExtension: shipTo.phoneExtension, phoneNumber: shipTo.phoneNumber, addressLine1: shipTo.addressLine1,
                                                       addressLine2  : shipTo.addressLine2, addressLine3: shipTo.addressLine3, contact: shipTo.contact], organization: organization[0], coa: coa];
    }
}

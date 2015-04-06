/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

/**
 * The composite service RequisitionConfigurationCompositeService
 * used for getting Configuration values.
 *
 */
class RequisitionConfigurationCompositeService {
    def institutionalDescriptionService
    def financeCurrencyService

    /**
     * The method is used to get the institutional level currency code and title from the Institution Description.
     * @return  map which has values for currencyCode and title.
     */
    def getInstitutionBasedCurrency() {
        def configuration = institutionalDescriptionService.findByKey(  )
        def currencyCode = configuration?.baseCurrCode
        def currency = financeCurrencyService.findCurrencyByCurrencyCode( currencyCode )
        def map = [:]
        map.currencyCode = configuration?.baseCurrCode
        map.currencyTitle = currency.title
        return map
    }
}

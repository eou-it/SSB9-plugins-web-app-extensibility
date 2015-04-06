/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

/**
 * The composite service FinanceConfigurationCompositeService
 * used for getting Configuration values.
 *
 */
class FinanceConfigurationCompositeService {
    def institutionalDescriptionService
    def financeCurrencyService

    /**
     * The method is used to get the institutional level currency code and title from the Institution Description.
     * @return map which has values for currencyCode and title.
     */
    def getInstitutionBasedCurrency() {
        def currency = financeCurrencyService.findCurrencyByCurrencyCode( institutionalDescriptionService.findByKey().baseCurrCode )
        [currencyCode: currency.currencyCode, currencyTitle: currency.title]
    }
}

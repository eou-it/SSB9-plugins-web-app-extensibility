/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.procurement.common.FinanceValidationConstants
import net.hedtech.banner.finance.util.LoggerUtility
import org.apache.log4j.Logger

/**
 * Class for Purchase Requisition Information Composite
 */
class RequisitionInformationCompositeService {
    private static final Logger LOGGER = Logger.getLogger(this.class)
    boolean transactional = true

    def requisitionHeaderService
    def shipToCodeService
    def financeOrganizationCompositeService
    def chartOfAccountsService
    def financeTaxGroupService
    def financeVendorService
    def financeDiscountService
    def financeCurrencyService
    def financeCurrencyCompositeService
    def financeTextService

    /**
     * Fetches Requisition Information
     * @param requestCode
     */
    def fetchPurchaseRequisition(requestCode, baseCcy) {
        def header = requisitionHeaderService.findRequisitionHeaderByRequestCode(requestCode)
        LoggerUtility.debug(LOGGER, 'Header: ' + header)
        def shipTo = shipToCodeService.findShipToCodesByCode(header.ship)
        LoggerUtility.debug(LOGGER, 'shipTo: ' + shipTo)
        def organization = financeOrganizationCompositeService.
                findOrganizationListByEffectiveDateAndSearchParam([searchParam: header.organization, coaCode: header.chartOfAccount], [offset: 0, max: 1])
        LoggerUtility.debug(LOGGER, 'organization: ' + organization)
        def coa = chartOfAccountsService.getChartOfAccountByCode(header.chartOfAccount)
        LoggerUtility.debug(LOGGER, 'coa: ' + coa)
        def taxGroup, vendor, discount = [], currency = []
        if (header.taxGroup) {
            taxGroup = financeTaxGroupService.findTaxGroupsByTaxGroupCode(header.taxGroup)
        }
        if (header.vendorPidm) {
            vendor = financeVendorService.fetchFinanceVendor([vendorPidm: header.vendorPidm, vendorAddressType: header.vendorAddressType, vendorAddressTypeSequence: header.vendorAddressTypeSequence])
        }
        if (header.discount) {
            def discountObj = financeDiscountService.findDiscountByDiscountCode(header.discount)
            discount = [discountCode: discountObj.discountCode, discountDescription: discountObj.discountDescription]
        }
        if (header.currency) {
            def currencyObj = financeCurrencyService.findCurrencyByCurrencyCode(header.currency)
            currency = [currencyCode: currencyObj.currencyCode, title: currencyObj.title]
        } else {
            currency = financeCurrencyCompositeService.getFilteredCurrencyDetail(baseCcy)
        }
        def privateComment = ''
        def publicComment = ''
        financeTextService.listHeaderLevelTextByCode(header.requestCode, FinanceValidationConstants.REQUISITION_INDICATOR_YES).each {
            privateComment = privateComment + it.text
        }
        financeTextService.listHeaderLevelTextByCode(header.requestCode, FinanceValidationConstants.REQUISITION_INDICATOR_NO).each {
            publicComment = publicComment + it.text
        }
        LoggerUtility.debug(LOGGER, 'taxGroup: ' + taxGroup)
        LoggerUtility.debug(LOGGER, 'discount: ' + discount)
        LoggerUtility.debug(LOGGER, 'currency: ' + currency)
        return [header              : header,
                shipTo              : [zipCode     : shipTo.zipCode, state: shipTo.state, city: shipTo.city,
                                       shipCode    : shipTo.shipCode, addressLine1: shipTo.addressLine1,
                                       addressLine2: shipTo.addressLine2, addressLine3: shipTo.addressLine3,
                                       contact     : shipTo.contact],
                organization        : [coaCode  : organization[0].coaCode, orgnCode: organization[0].orgnCode,
                                       orgnTitle: organization[0].orgnTitle],
                coa                 : [title: coa.title, chartOfAccountsCode: coa.chartOfAccountsCode],
                taxGroup            : [taxGroupCode: taxGroup?.taxGroupCode, taxGroupTitle: taxGroup?.taxGroupTitle],
                vendor              : vendor,
                discount            : discount,
                currency            : currency,
                headerPrivateComment: privateComment,
                headerPublicComment : publicComment]
    }
}

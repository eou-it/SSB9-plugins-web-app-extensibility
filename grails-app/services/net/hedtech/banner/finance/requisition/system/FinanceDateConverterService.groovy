/*******************************************************************************
 Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.i18n.LocalizeUtil
import org.apache.commons.lang.StringUtils

/**
 * FinanceDateConverterService.
 *
 * Date: 6/16/2015
 * Time: 5:46 PM
 */
class FinanceDateConverterService {

    def dateConverterService

    /**
     * Converts give date into localized formatted date. If user want to have default Date to current date if date
     * not provide, need to pass needDefaultDate true
     *
     * @param strDate
     * @param needDefaultDate
     * @return
     */
    def convertToDate( strDate, boolean needDefaultDate ) {
        String inputDate = strDate
        print 'Current date in local format' + LocalizeUtil.formatDate( new Date() )
        if (StringUtils.isBlank( inputDate ) && needDefaultDate) {
            inputDate = LocalizeUtil.formatDate( new Date() )
        }
        try {
            return LocalizeUtil.parseDate( dateConverterService.parseDefaultCalendarToGregorian( inputDate ) )
        }
        catch (Exception e) {
            throw new ApplicationException( "", "@@r1:default.invalidDate@@" )
        }
    }
}

/*******************************************************************************
 Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.i18n.LocalizeUtil
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger

/**
 * FinanceDateConverterService. The service for Date conversion for localization
 */
class FinanceDateConverterService {
    private static final def LOGGER = Logger.getLogger( this.getClass() )
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
        LoggerUtility.info( LOGGER, 'Current date in local format : ' + LocalizeUtil.formatDate( new Date() ) )
        if (StringUtils.isBlank( inputDate ) && needDefaultDate) {
            inputDate = LocalizeUtil.formatDate( new Date() )
        }
        try {
            return LocalizeUtil.parseDate( dateConverterService.parseDefaultCalendarToGregorian( inputDate ) )
        }
        catch (Exception e) {
            throw new ApplicationException( "", FinanceProcurementConstants.ERROR_MESSAGE_INVALID_DATE_FORMAT )
        }
    }
}

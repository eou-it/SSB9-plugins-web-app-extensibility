/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.util

import com.ibm.icu.text.DateFormat
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.i18n.DateConverterService
import org.apache.log4j.Logger

/**
 * Finance Date Format Service. It derives all methods of DataConverter Service with override methods
 */
class FinanceDateFormatService extends DateConverterService {
    private static final def LOGGER = Logger.getLogger( this.getClass() )


    @Override
    def convert(
            def fromDateValue, String fromULocaleString, String toULocaleString, String fromDateFormatString, String toDateFormatString, String adjustDays = null ) {
        assert fromDateValue
        assert fromULocaleString
        assert toULocaleString
        assert fromDateFormatString
        assert toDateFormatString
        try {
            Date fromDate
            ULocale fromULocale = new ULocale( fromULocaleString )
            Calendar fromCalendar = Calendar.getInstance( fromULocale )
            if (fromDateValue instanceof String) {
                DateFormat fromDateFormat = fromCalendar.handleGetDateFormat( fromDateFormatString, fromULocale )
                fromDate = fromDateFormat.parse( fromDateValue )
            } else if (fromDateValue instanceof Date) {
                fromCalendar.setTime( fromDateValue )
                DateFormat fromDateFormat = fromCalendar.handleGetDateFormat( fromDateFormatString, fromULocale )
                fromDateValue = fromDateFormat.format( fromCalendar )
                fromDate = fromDateFormat.parse( fromDateValue )
            }
            if (fromULocaleString == toULocaleString && fromDateFormatString == toDateFormatString) {
                return fromDateValue
            }
            ULocale toULocale = new ULocale( toULocaleString )
            Calendar toCalendar = Calendar.getInstance( toULocale )
            toCalendar.setTime( fromDate )
            if (adjustDays) {
                toCalendar = adjustDate( toCalendar, adjustDays )
            }
            DateFormat toDateFormat = toCalendar.handleGetDateFormat( toDateFormatString, toULocale )
            if (fromDateValue instanceof Date) {
                return toCalendar.getTime()
            }
            return arabicToDecimal( toDateFormat.format( toCalendar ) )
        } catch (Exception exception) {
            LoggerUtility.warn( LOGGER, 'Unable to perform conversion --  date: ' + fromDateValue + ', fromULocaleString: ' +
                    fromULocaleString + ', toULocaleString: ' + toULocaleString + ', fromDateFormatString: ' +
                    fromDateFormatString + ', toDateFormatString: ' + toDateFormatString )
            return "error"
        }
    }

    /**
     * Parse Date
     * @param value
     * @return
     */
    public parseDefaultCalendarToGregorian( value ) {
        super.parseDefaultCalendarToGregorian( value )
    }
}

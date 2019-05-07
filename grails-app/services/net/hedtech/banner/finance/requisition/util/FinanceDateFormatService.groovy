/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.util

import com.ibm.icu.text.DateFormat
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale
import net.hedtech.banner.i18n.DateConverterService
import java.text.ParseException

/**
 * Finance Date Format Service. It derives all methods of DataConverter Service with override methods
 */
class FinanceDateFormatService extends DateConverterService {


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
        } catch (ParseException exception) {
            log.warn('Unable to perform conversion --  date: {} , fromULocaleString: {} , toULocaleString: {} , fromDateFormatString: {} , toDateFormatString: {}'
                    ,fromDateValue,fromULocaleString,toULocaleString,fromDateFormatString,toDateFormatString)
            return "error"
        }
    }

}

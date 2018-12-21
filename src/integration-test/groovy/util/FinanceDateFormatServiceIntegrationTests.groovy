/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package util

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.sf.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration Test cases file for FinanceDateFormatService.
 */
class FinanceDateFormatServiceIntegrationTests extends BaseIntegrationTestCase {

    def financeDateFormatService

    /**
     * Super class setup
     */
    @Before
    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    /**
     * Tear Down actions
     */
    @After
    void tearDown() {
        super.tearDown()
    }

    /**
     * Test scenarios
     */
    @Test
    void testGregorianToIslamicDateConversion() {
        assertEquals financeDateFormatService.convert( "2012/01/01", "en_US@calendar=gregorian", "en_AR@calendar=islamic", "yyyy/MM/dd", "yyyy/MM/dd" ), "1433/02/08"
        assertEquals financeDateFormatService.convert( "01/01/2012", "en_US@calendar=gregorian", "en_AR@calendar=islamic", "MM/dd/yyyy", "yyyy/MM/dd" ), "1433/02/08"
        assertEquals financeDateFormatService.convert( "01/01/2012", "en_US@calendar=gregorian", "en_AR@calendar=islamic", "MM/dd/yyyy", "MM/dd/yyyy" ), "02/08/1433"
    }

    /**
     * Test scenarios
     */
    @Test
    void testIslamicToGregorianDateConversion() {
        assertEquals financeDateFormatService.convert( "1433/02/08", "en_AR@calendar=islamic", "en_US@calendar=gregorian", "yyyy/MM/dd", "yyyy/MM/dd" ), "2012/01/01"
        assertEquals financeDateFormatService.convert( "02/08/1433", "en_AR@calendar=islamic", "en_US@calendar=gregorian", "MM/dd/yyyy", "yyyy/MM/dd" ), "2012/01/01"
        assertEquals financeDateFormatService.convert( "02/08/1433", "en_AR@calendar=islamic", "en_US@calendar=gregorian", "MM/dd/yyyy", "MM/dd/yyyy" ), "01/01/2012"
    }

    /**
     * Test scenarios
     */
    @Test
    void testConvertGregorianToDefaultCalendar() {
        assertEquals( financeDateFormatService.parseDefaultCalendarToGregorian( "01/01/2012" ), "01/01/2012" )
        assertEquals( financeDateFormatService.parseDefaultCalendarToGregorian( "01-Jan-2012" ), "01-Jan-2012" )
        assertEquals( financeDateFormatService.parseDefaultCalendarToGregorian( "01-January-2012" ), "01-January-2012" )
    }
}

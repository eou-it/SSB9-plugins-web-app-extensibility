/*******************************************************************************
 Copyright 2018-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

import org.grails.testing.GrailsUnitTest
import spock.lang.Specification

class ExtensionUtilitySpec extends Specification implements GrailsUnitTest {

    def setup() {
    }

    def cleanup() {
    }

    void "test extensionUtility"(){
        when:
        def res = ExtensionUtility.derivePageName("/EXTZ/Test.json")
        then:
        noExceptionThrown()
        res.equals("Test")
        when:
        def res2 = ExtensionUtility.deriveApplicationName("/EXTZ/Test.json")
        then:
        noExceptionThrown()
        res2.equals("EXTZ")
    }
}

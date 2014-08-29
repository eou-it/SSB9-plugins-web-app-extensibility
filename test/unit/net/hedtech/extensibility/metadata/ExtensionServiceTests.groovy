package net.hedtech.extensibility.metadata


import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ExtensionService)
class ExtensionServiceTests {

    def extensionService = new ExtensionService()
    def params = [unitTest:true, dummy:1]

    void testList() {
        def result = extensionService.list(params)
        assertNotNull(result)
    }

    void testCount() {
        def count=extensionService.count(params)
        assertEquals(count,1)
    }
}

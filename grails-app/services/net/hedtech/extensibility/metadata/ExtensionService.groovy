package net.hedtech.extensibility.metadata
import grails.converters.JSON

class ExtensionService {
    def static extensionsPath = grails.util.Holders.getConfig().webAppExtensibility.locations.extensions
    def static mockData = [
            [
                    subpage:'dashboard',
                    modifications: [
                            remove: [  // each entry is a field name to remove
                                       // do we need to remove sections?  Or remove fields from sections?
                                       'id'
                            ],
                            add: [   // each entry is a "Field" record to add
                                     /* problem with templating is that it may not fit well within the application being extended
                                     [
                                             name: 'employeeProfile.employeeSummary.extendedField',  // name-within-scope of data field
                                             section: 'employee-summary', // do we need to add to specific sections within the page? probably a CSS selector?
                                             after: 'firstWorkDate',  // if 'after' is blank, insert as first child.  Could also provide a selector and an operation (e.g., after/before/append/prepend)
                                             //index: 3, // alternatively, provide an index - insert as the nth-child of section
                                             label: 'Extension', // localized label, or internationalized identifier?
                                             template: 'static',
                                             tooltip: 'Field added by extensibility-1'
                                     ],
                                     */
                                     [   //Example html text
                                         after: 'firstWorkDate',
                                         html: """
                                    <div style="color:blue" class="col-md-5 col-lg-5 col-sm-12 col-xs-12 no-margin break-word" data-xe-field='extension'>
                                    Ext: {{employeeProfile.employeeSummary.extendedField}}
                                    </div>
                                  """
                                     ],
                                     [   //Example literal text
                                         name:  'pageDescription',
                                         label: 'Description',
                                         value: """
                                    This is a literal text added to the beginning of the extended sub page as there is no after attribute.
                                    Can use scope variables in the value using AngularJS extrapolation:
                                    {{employeeProfile.employeeProfile.name}} {{employeeProfile.employeeProfile.bannerId}}.
                                    """,
                                         template: 'literal'
                                     ]
                            ]
                    ]
            ],
            [
                    subpage:'leave-balance',
                    modifications: [],
                    description: 'nothing'
            ]
    ]

    def list(params) {

        if (params.page) {
            def md = loadFromFile(params.page)
            if (md)
                return md
        }
        //When nothing found return mockData - TODO remove in final version
        return mockData
    }

    def count(params) {
        return 1
    }

    private def saveToFile(page, metadata){
        def json = new JSON(metadata)
        def jsonStr = json.toString(true)
        def file = new File("${extensionsPath}/${page}.json")
        file.text=jsonStr
    }

    private def loadFromFile(page){
        def result = null
        def file = new File("${extensionsPath}/${page}.json")
        def jsonStr = null
        if (file) {
            jsonStr =file.text
        }
        if (jsonStr) {
            JSON.use("deep")
            result = JSON.parse(jsonStr)
        }
        result
    }
}

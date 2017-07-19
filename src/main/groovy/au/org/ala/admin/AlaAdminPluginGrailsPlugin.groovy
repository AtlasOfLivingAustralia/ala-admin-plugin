class AlaAdminPluginGrailsPlugin {
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.0 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "ALA Admin Plugin" // Headline display name of the plugin
    def author = "Atlas of Living Australia"
    def authorEmail = ""
    def description = "Grails plugin containing common administrative functionality."

    // URL to the plugin's documentation
    def documentation = "https://github.com/AtlasOfLivingAustralia/ala-admin-plugin"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "MPL-2.0"

    // Details of company behind the plugin (if there is one)
    def organization = [name: "Atlas of Living Australia", url: "http://ala.org.au"]

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { ctx ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}

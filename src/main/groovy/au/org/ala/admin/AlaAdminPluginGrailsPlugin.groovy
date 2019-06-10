/*
 * Copyright (C) 2017 Atlas of Living Australia
 * All Rights Reserved.
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.admin

import grails.plugins.*

class AlaAdminPluginGrailsPlugin extends Plugin {
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.0 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // security.cas.uriFilterPattern will be updated with the admin page when it is missing. This must occur before
    // ala-auth is loaded.
    def loadBefore = ['ala-auth']

    def title = "ALA Admin Plugin" // Headline display name of the plugin
    def author = "Atlas of Living Australia"
    def authorEmail = "info@ala.org.au"
    def description = "Grails plugin containing common administrative functionality."

    // URL to the plugin's documentation
    def documentation = "https://github.com/AtlasOfLivingAustralia/ala-admin-plugin"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "MPL-1.1"

    // Details of company behind the plugin (if there is one)
    def organization = [name: "Atlas of Living Australia", url: "http://ala.org.au"]

    def doWithWebDescriptor = { xml ->
    }

    Closure doWithSpring() {
        { ->
            // inject admin page into security.cas.uriFilterPattern
            def casFilter = grailsApplication.config.security.cas.uriFilterPattern
            def adminPageFilter = '/alaAdmin/?.*'
            if (!casFilter.split(',').contains(adminPageFilter)) {
                if (casFilter) {
                    casFilter += ','
                }
                casFilter += adminPageFilter

                grailsApplication.config.security.cas.uriFilterPattern = casFilter
            }
        }
    }

    def onChange = { event ->
    }

    @Override
    void onConfigChange(Map<String, Object> event) {
        super.onConfigChange(event)

        // re-init the configService
        ConfigService configService = applicationContext.getBean(ConfigService)
        if (configService) {
            configService.init()
        }
    }

    def onShutdown = { event ->
    }
}

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
    def grailsVersion = "7.1.1 > *"

    def title = "ALA Admin Plugin"
    def author = "Atlas of Living Australia"
    def authorEmail = "info@ala.org.au"
    def description = "Grails plugin containing common administrative functionality."
    def documentation = "https://github.com/AtlasOfLivingAustralia/ala-admin-plugin"
    def license = "MPL-1.1"
    def organization = [name: "Atlas of Living Australia", url: "http://ala.org.au"]
}

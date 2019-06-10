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

/**
 * Controller for ALA admin functions (requires ROLE_ADMIN)
 */
class AlaAdminController {

    SystemMessageService systemMessageService
    ConfigService configService

    def index() {
        render view: "/ala-admin"
    }

    private Map flatten(Map m, String separator = '.') {
        m.collectEntries { key, value ->
            value instanceof Map ?
                    flatten(value, separator).collectEntries { nestedKey, nestedValue ->
                        [(key + separator + nestedKey): nestedValue]
                    } : [(key): value]
        }
    }


    def reloadConfig() {
        try {
            configService.reloadConfig(flash)
        } catch (Exception e) {
            log.error "Unable to reload configuration. Please correct problem and try again: ${e}", e
            flash.message = "Unable to reload configuration - " + e.getMessage()
        }

        redirect(action: 'index')
    }

    def viewConfig() {
        try {
            ConfigObject config = grailsApplication.getConfig()
            Map flattened = flatten(config, ".")
            render view: "/view-config", model: [config: flattened]
        } catch (Exception e) {
            log.error "Unable to view configuration. Please correct problem and try again: ${e}", e
            flash.message = "Unable to view configuration - " + e.getMessage()
            redirect(action: 'index')
        }
    }

    def systemMessage() {
        SystemMessage message = new SystemMessage(
                text: params.message,
                severity: params.message ? params.severity : "",
                timestamp: new Date(),
                user: request.userPrincipal?.name
        )

        systemMessageService.setSystemMessage(message)
        flash.message = "System message has been saved"

        redirect(action: 'index')
    }

    def clearMessage() {
        systemMessageService.setSystemMessage(null)
        flash.message = "System message has been cleared"

        redirect(action: 'index')
    }
}

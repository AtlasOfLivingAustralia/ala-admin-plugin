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

import grails.util.Environment
import org.grails.config.PropertySourcesConfig
import org.grails.config.yaml.YamlPropertySourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * Controller for ALA admin functions (requires ROLE_ADMIN)
 */
class AlaAdminController {

    SystemMessageService systemMessageService

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
            grailsApplication.getConfig().merge(getConfig())
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

    /**
     * Attempts to load external config files, which are specified by the config var:
     * grails.config.locations which is also used by the 'external-config' grails plugin.
     *
     * @return ConfigObject
     */
    private ConfigObject getConfig() {
        def configLocations = grailsApplication.config.getProperty("grails.config.locations") ? grailsApplication.config.grails.config.locations : []
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver()
        ConfigObject config = new ConfigObject()

        configLocations.each { location ->
            log.info "reloading config file: ${location}"
            Resource resource = resolver.getResource(location)
            InputStream stream = null

            try {
                stream = resource.getInputStream()
                ConfigSlurper configSlurper = new ConfigSlurper(Environment.current.name)
                def props = null

                if (resource.filename.endsWith('.groovy')) {
                    props = stream.text
                } else if (resource.filename.endsWith('.properties')) {
                    props = new Properties()
                    props.load(stream)
                } else if (resource.filename.endsWith('.yml')) {
                    def mapPropertySource = new YamlPropertySourceLoader().load( "yml config", resource, null )
                    props = new PropertySourcesConfig(mapPropertySource.getSource()).toProperties()
                }

                if (props) {
                    config.merge(configSlurper.parse(props))
                }

                def msg = "External config (${location}) has been reloaded"
                flash.message = (flash.message) ? "${flash.message}<br>${msg}" : msg
            } catch (Exception ex) {
                flash.error = (flash.error) ? "${flash.error}<br>${ex}" : ex.message
                log.warn ex.message
            } finally {
                stream?.close()
            }
        }

        config
    }

}

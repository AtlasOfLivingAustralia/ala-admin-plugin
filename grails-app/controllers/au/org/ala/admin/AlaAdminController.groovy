package au.org.ala.admin

import grails.util.Environment
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

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


    private ConfigObject getConfig() {
        def configLocations = grailsApplication.config.grails?.config?.locations?:[] 
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver()
        ConfigObject config

        configLocations.each { location ->
            Resource resource = resolver.getResource(location)
            InputStream stream = null

            try {
                stream = resource.getInputStream()
                ConfigSlurper configSlurper = new ConfigSlurper(Environment.current.name)
                if (resource.filename.endsWith('.groovy')) {
                    config = configSlurper.parse(stream.text)
                } else if (resource.filename.endsWith('.properties')) {
                    Properties props = new Properties()
                    props.load(stream)
                    config = configSlurper.parse(props)
                }
                flash.message = "External config (${location}) has been reloaded"
            } catch (Exception ex) {
                log.warn ex.message
            } finally {
                stream?.close()
            }
        }

        config
    }

}

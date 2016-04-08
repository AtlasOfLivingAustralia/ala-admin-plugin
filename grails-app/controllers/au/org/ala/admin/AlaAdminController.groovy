package au.org.ala.admin

import grails.util.Environment
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

class AlaAdminController {

    def index() {
        render view: "/ala-admin"
    }

    def reloadConfig() {
        def configLocation = "file:${grailsApplication.config.default_config}"
        def resolver = new PathMatchingResourcePatternResolver()
        def resource = resolver.getResource(configLocation)
        def stream = null

        try {
            stream = resource.getInputStream()
            ConfigSlurper configSlurper = new ConfigSlurper(Environment.current.name)
            if (resource.filename.endsWith('.groovy')) {
                def newConfig = configSlurper.parse(stream.text)
                grailsApplication.getConfig().merge(newConfig)
            } else if (resource.filename.endsWith('.properties')) {
                def props = new Properties()
                props.load(stream)
                def newConfig = configSlurper.parse(props)
                grailsApplication.getConfig().merge(newConfig)
            }
            flash.message = "External config has been reloaded"
        } catch (FileNotFoundException e) {
            log.error "No external config to reload configuration. Looking for ${configLocation}", e
            flash.message = "No external config to reload configuration. Looking for ${configLocation}"
            redirect(action: 'index')
        } catch (Exception e) {
            log.error "Unable to reload configuration. Please correct problem and try again: ${e}", e
            flash.message = "Unable to reload configuration - " + e.getMessage()
            redirect(action: 'index')
        } finally {
            stream?.close()
        }

        render view: "/ala-admin"
    }
}

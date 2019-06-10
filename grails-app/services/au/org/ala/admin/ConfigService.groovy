package au.org.ala.admin

import grails.boot.config.GrailsAutoConfiguration
import grails.core.GrailsApplication
import grails.plugins.GrailsPlugin
import grails.util.Environment
import org.grails.config.PropertySourcesConfig
import org.grails.config.yaml.YamlPropertySourceLoader
import org.grails.io.watch.DirectoryWatcher
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

class ConfigService {

    def grailsApplication
    def pluginManager

    def directoryWatcher

    /**
     * Use DirectoryWatcher to monitor grails.config.locations file changes.
     *
     * @return
     */
    def init() {
        def autoreload = grailsApplication.config.grails.config.autoReload
        if (autoreload ? autoreload.toBoolean() : true) {
            // cancel the directoryWatcher if it exists
            if (directoryWatcher) {
                directoryWatcher.active = false
            }

            // create a new directoryWatcher
            directoryWatcher = new DirectoryWatcher()
            grailsApplication.config.grails.config.locations.each {
                directoryWatcher.addWatchFile(new File(new URI(it).toURL().getFile()))
            }
            directoryWatcher.addListener(new DirectoryWatcher.FileChangeListener() {

                @Override
                void onChange(File file) {
                    fireConfigChange()
                }

                @Override
                void onNew(File file) {
                    fireConfigChange()
                }
            })
            directoryWatcher.active = true
            directoryWatcher.start()
        }
    }

    def reloadConfig() {
        try {
            grailsApplication.getConfig().merge(getConfig())
        } catch (Exception e) {
            log.error("Failed to merge config", e)
        }

        fireConfigChange()
    }

    private def fireConfigChange() {

        try {
            // notify application class
            if (grailsApplication.applicationClass instanceof GrailsAutoConfiguration) {
                grailsApplication.applicationClass.onConfigChange(grailsApplication.config)
            }
        } catch (Exception e) {
            log.error("failed to notify grailsApplication.applicationClass of the config change", e)
        }

        try {
            // notify grailsApplication
            if (grailsApplication instanceof GrailsApplication) {
                grailsApplication.configChanged()
            }
        } catch (Exception e) {
            log.error("failed to notify grailsApplication of the config change", e)
        }

        try {
            // notify plugins
            pluginManager.allPlugins.each {
                it.notifyOfEvent(GrailsPlugin.EVENT_ON_CONFIG_CHANGE, grailsApplication.config)
            }
        } catch (Exception e) {
            log.error("failed to notify plugins of the config change", e)
        }
    }

    /**
     * Attempts to load external config files, which are specified by the config var:
     * grails.config.locations which is also used by the 'external-config' grails plugin.
     *
     * @return ConfigObject
     */
    private ConfigObject getConfig(flash) {
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
                    def mapPropertySource = new YamlPropertySourceLoader().load("yml config", resource, null)
                    props = new PropertySourcesConfig(mapPropertySource.getSource()).toProperties()
                }

                if (props) {
                    config.merge(configSlurper.parse(props))
                }

                def msg = "External config (${location}) has been reloaded"
                if (flash) {
                    flash.message = (flash.message) ? "${flash.message}<br>${msg}" : msg
                }
            } catch (Exception ex) {
                if (flash) {
                    flash.error = (flash.error) ? "${flash.error}<br>${ex}" : ex.message
                }
                log.warn ex.message
            } finally {
                stream?.close()
            }
        }

        config
    }
}

package au.org.ala.admin

import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import grails.util.Metadata
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class SystemMessageService {

    def grailsApplication

    @CacheEvict(value = ["systemMessageCache"], allEntries = true)
    void setSystemMessage(SystemMessage message) {
        getSystemMessageFile()?.write(new JsonBuilder([message: message?.text ? message : [:]]).toPrettyString())
    }

    @Cacheable("systemMessageCache")
    SystemMessage getSystemMessage() {
        SystemMessage message = null

        File msgFile = getSystemMessageFile()
        if (msgFile?.text) {
            Map json = new JsonSlurper().parseText(msgFile.text)

            if (json?.message) {
                message = new SystemMessage(json?.message)
            }
        }

        message
    }

    private File getSystemMessageFile() {
        File systemMsgFile
        try {
            def path = grailsApplication.config.ala.admin.systemMessage.path ?: "/data/${Metadata.current.getApplicationName()}/config"
            File dataDir = new File(path)
            if (!dataDir.exists()) {
                dataDir.mkdirs()
            }

            def fileName = grailsApplication.config.ala.admin.systemMessage.fileName ?: "system-message.json"
            systemMsgFile = new File(dataDir, fileName)
            if (!systemMsgFile.exists()) {
                systemMsgFile.createNewFile()
            }
        } catch (IOException e) {
            log.warn("Could not getSystemMessageFile", e)
            systemMsgFile = null
        }

        return systemMsgFile
    }
}

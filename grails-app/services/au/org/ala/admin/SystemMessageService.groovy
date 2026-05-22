package au.org.ala.admin

import grails.util.Metadata
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable

class SystemMessageService {

    def grailsApplication

    @CacheEvict(value = "systemMessageCache", allEntries = true)
    void setSystemMessage(SystemMessage message) {
        getSystemMessageFile()?.write(new JsonBuilder([message: message?.text ? message : [:]]).toPrettyString())
    }

    @Cacheable("systemMessageCache")
    SystemMessage getSystemMessage() {
        File msgFile = getSystemMessageFile()

        if (msgFile?.text) {
            Map json = new JsonSlurper().parseText(msgFile.text)
            if (json?.message) {
                return new SystemMessage(json.message)
            }
        }

        return new SystemMessage([:])
    }

    private File getSystemMessageFile() {
        File systemMsgFile
        try {
            def path = grailsApplication.config.getProperty('ala.admin.systemMessage.path') ?: "/data/${Metadata.current.getApplicationName()}/config"
            File dataDir = new File(path)
            if (!dataDir.exists()) {
                dataDir.mkdirs()
            }

            def fileName = grailsApplication.config.getProperty('ala.admin.systemMessage.fileName') ?: "system-message.json"
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

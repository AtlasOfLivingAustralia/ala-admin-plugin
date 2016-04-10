package au.org.ala.admin

import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class SystemMessageService {

    def grailsApplication

    @CacheEvict(value = ["systemMessageCache"], allEntries = true)
    void setSystemMessage(SystemMessage message) {
        getSystemMessageFile().write(new JsonBuilder([message: message?.text ? message : [:]]).toPrettyString())
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
        File dataDir = new File("/data/${grailsApplication.metadata['app.name']}/config")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }

        File systemMsgFile = new File(dataDir, "system-message.json")
        if (!systemMsgFile.exists()) {
            systemMsgFile.createNewFile()
        }

        systemMsgFile
    }
}

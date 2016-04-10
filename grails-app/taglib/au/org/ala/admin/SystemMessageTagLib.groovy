package au.org.ala.admin

class SystemMessageTagLib {
    static namespace = "ala"

    SystemMessageService systemMessageService

    def systemMessage = { attrs ->
        SystemMessage message = systemMessageService.getSystemMessage()

        if (message?.text) {
            out << "<div class='system-message alert alert-${message.severity}'>${message.text}${attrs.showTimestamp?.toBoolean() ? ' (' + message.timestamp + ')' : ''}</div>"
        }
    }
}

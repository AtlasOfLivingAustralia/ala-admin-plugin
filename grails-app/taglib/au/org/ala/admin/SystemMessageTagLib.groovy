package au.org.ala.admin

class SystemMessageTagLib {
    static namespace = "ala"

    SystemMessageService systemMessageService

    def systemMessage = { attrs ->

        showNonProductionEnvironmentWarning(out, attrs)

        showSystemMessage(out, attrs)
    }

    private void showSystemMessage(out, attrs) {
        SystemMessage message = systemMessageService.getSystemMessage()

        if (message?.text) {
            out << "<div class='padding-top-1 system-message alert alert-${message.severity}'>${message.text}${attrs.showTimestamp?.toBoolean() ? ' (' + message.timestamp + ')' : ''}</div>"
        }
    }

    private void showNonProductionEnvironmentWarning(out, attrs) {
        if (isNonProductionEnvironment() && !attrs.hideEnvWarning?.toBoolean()) {
            out << """<div class="padding-top-1 alert alert-warning env-message">
                          <div class="env-message-text">This is a ${grailsApplication.config.deployment_env.toUpperCase()} site.</div>
                      </div>
                   """.stripIndent()
        }
    }

    private boolean isNonProductionEnvironment() {
        grailsApplication.config.deployment_env && grailsApplication.config.deployment_env?.toLowerCase() != "prod" && grailsApplication.config.deployment_env?.toLowerCase() != "production"
    }
}

package au.org.ala.admin

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AlaAdminControllerSpec extends Specification implements ControllerUnitTest<AlaAdminController> {

    void "viewConfig hides sensitive values by default"() {
        given:
        config['api.key'] = 'secret-api-key-1234'
        config['app.name'] = 'ala-admin'

        when:
        controller.viewConfig()

        then:
        view == '/view-config'
        model.config['api.key'] == '***************1234'
        model.config['app.name'] == 'ala-admin'
    }

    void "viewConfig reveals sensitive values when ala.admin.hideSensitiveValues is false"() {
        given:
        config['ala.admin.hideSensitiveValues'] = false
        config['api.key'] = 'secret-api-key-1234'
        config['app.name'] = 'ala-admin'

        when:
        controller.viewConfig()

        then:
        view == '/view-config'
        model.config['api.key'] == 'secret-api-key-1234'
        model.config['app.name'] == 'ala-admin'
    }

    void "viewConfig reveals sensitive values when ala.admin.viewConfig.hideSensitiveValues is false"() {
        given:
        config['ala.admin.hideSensitiveValues'] = true
        config['ala.admin.viewConfig.hideSensitiveValues'] = false
        config['api.key'] = 'secret-api-key-1234'
        config['app.name'] = 'ala-admin'

        when:
        controller.viewConfig()

        then:
        view == '/view-config'
        model.config['api.key'] == 'secret-api-key-1234'
        model.config['app.name'] == 'ala-admin'
    }
}

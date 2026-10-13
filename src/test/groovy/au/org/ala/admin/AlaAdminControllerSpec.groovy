package au.org.ala.admin

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AlaAdminControllerSpec extends Specification implements ControllerUnitTest<AlaAdminController> {

    def cleanup() {
        config.ala.admin.hideSensitiveValues = true
        config.ala.admin.sensitiveKeyPatterns = null
        config.remove('api.key')
        config.remove('app.name')
        config.remove('my.custom.field')
    }

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
        config.ala.admin.hideSensitiveValues = false
        config['api.key'] = 'secret-api-key-1234'
        config['app.name'] = 'ala-admin'

        when:
        controller.viewConfig()

        then:
        view == '/view-config'
        model.config['api.key'] == 'secret-api-key-1234'
        model.config['app.name'] == 'ala-admin'
    }

    void "viewConfig uses custom sensitiveKeyPatterns list"() {
        given:
        config.ala.admin.sensitiveKeyPatterns = ['custom', 'special']
        config['api.key'] = 'secret-api-key-1234'
        config['my.custom.field'] = 'confidentialvalue1234'

        when:
        controller.viewConfig()

        then:
        view == '/view-config'
        model.config['api.key'] == 'secret-api-key-1234'
        model.config['my.custom.field'] == '*****************1234'
    }

    void "viewConfig uses custom sensitiveKeyPatterns comma-separated string"() {
        given:
        config.ala.admin.sensitiveKeyPatterns = '  custom  ,  special  '
        config['api.key'] = 'secret-api-key-1234'
        config['my.custom.field'] = 'confidentialvalue1234'

        when:
        controller.viewConfig()

        then:
        view == '/view-config'
        model.config['api.key'] == 'secret-api-key-1234'
        model.config['my.custom.field'] == '*****************1234'
    }
}

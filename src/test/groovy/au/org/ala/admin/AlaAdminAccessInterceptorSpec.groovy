package au.org.ala.admin

import grails.testing.web.interceptor.InterceptorUnitTest
import org.apache.http.HttpStatus
import org.grails.web.util.GrailsApplicationAttributes
import spock.lang.Specification
import spock.lang.Unroll

import java.security.Principal

class AlaAdminAccessInterceptorSpec extends Specification implements InterceptorUnitTest<AlaAdminAccessInterceptor>{

    void "ALA Administrators are allowed to do everything"() {
        setup:
        AlaAdminController controller = [someAction: {}] as AlaAdminController
        // need to do this because grailsApplication.controllerClasses is empty in the filter when run from the unit test
        // unless we manually add the dummy controller class used in this test
        grailsApplication.addArtefact("Controller", AlaAdminController)

        when:
        request.userPrincipal = new User([authority: "ROLE_ADMIN"])
        request.addUserRole('ROLE_ADMIN')

        request.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'alaAdmin')
        request.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'someAction')
        withInterceptors(controller: "alaAdmin", action: "someAction") {
            controller.someAction()
        }

        then:
        response.status == HttpStatus.SC_OK
    }

    void "Non-Administrators are not allowed to do anything"() {
        setup:
        AlaAdminController controller = [someAction: {}] as AlaAdminController
        // need to do this because grailsApplication.controllerClasses is empty in the filter when run from the unit test
        // unless we manually add the dummy controller class used in this test
        grailsApplication.addArtefact("Controller", AlaAdminController)

        when:
        request.userPrincipal = new User([authority: "SOMETHING_ELSE"])

        request.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'alaAdmin')
        request.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'someAction')
        withInterceptors(controller: "alaAdmin", action: "someAction") {
            controller.someAction()
        }

        then:
        response.status == HttpStatus.SC_FORBIDDEN
    }
}

class User implements Principal {

    Map attributes

    User(Map attributes) {
        this.attributes = attributes
    }

    @Override
    String getName() {
        return "Fred"
    }

    Map getAttributes() {
        return attributes
    }
}
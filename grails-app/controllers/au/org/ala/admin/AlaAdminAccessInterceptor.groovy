package au.org.ala.admin

import org.apache.http.HttpStatus

class AlaAdminAccessInterceptor {
    static final String ALA_ADMIN_ROLE = "ROLE_ADMIN"

    AlaAdminAccessInterceptor() {
        match(controller: 'alaAdmin')
    }

    boolean before() {
        String actionFullName = "${controllerName.capitalize()}Controller.${actionName}"
        boolean isALAAdmin = request.isUserInRole(ALA_ADMIN_ROLE)

        log.debug "Is user ${request.userPrincipal?.name} an ALA Admin user? ${isALAAdmin}"

        if (!isALAAdmin) {
            log.error "User ${request.userPrincipal?.name} is not authorised to access action ${actionFullName}"
            response.status = HttpStatus.SC_FORBIDDEN
            response.sendError(HttpStatus.SC_FORBIDDEN)
        }

        return isALAAdmin
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
package au.org.ala.admin

import org.apache.http.HttpStatus

class AlaAdminAccessInterceptor {
    static final String ALA_ADMIN_ROLE = "ROLE_ADMIN"

    def filters = {
        all(controller: 'alaAdmin', action: '*') {
            before = {
                String actionFullName = "${controllerName.capitalize()}Controller.${actionName}"
                boolean isALAAdmin = request.isUserInRole(ALA_ADMIN_ROLE)

                AlaAdminAccessInterceptor.log.debug "Is user ${request.userPrincipal?.name} an ALA Admin user? ${isALAAdmin}"

                if (!isALAAdmin) {
                    AlaAdminAccessInterceptor.log.error "User ${request.userPrincipal?.name} is not authorised to access action ${actionFullName}"
                    response.status = HttpStatus.SC_FORBIDDEN
                    response.sendError(HttpStatus.SC_FORBIDDEN)
                }

                isALAAdmin
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
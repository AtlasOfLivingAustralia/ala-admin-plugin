package au.org.ala.admin

import org.apache.http.HttpStatus

class AlaAdminAccessFilters {
    static final String ALA_ADMIN_ROLE = "ROLE_ADMIN"

    def filters = {
        all(controller: 'alaAdmin', action: '*') {
            before = {
                String actionFullName = "${controllerName.capitalize()}Controller.${actionName}"
                List<String> usersRoles = request.userPrincipal ? request.userPrincipal?.attributes?.authority?.split(",") : []
                boolean isALAAdmin = usersRoles?.contains(ALA_ADMIN_ROLE)

                log.debug "Is user ${request.userPrincipal?.name} an ALA Admin user? ${isALAAdmin}"

                if (!isALAAdmin) {
                    log.error "User ${request.userPrincipal?.name} is not authorised to access action ${actionFullName}"
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
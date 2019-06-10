package au.org.ala.admin

class BootStrap {
    def configService

    def init = { servletContext ->
        configService.init()
    }

    def destroy = {
    }
}

package au.org.ala.admin

class AlaAdminUrlMappings {

    static mappings = {
        "/alaAdmin/viewConfig"(controller: "alaAdmin", action: "viewConfig", method: "GET")
        "/alaAdmin/reloadConfig"(controller: "alaAdmin", action: "reloadConfig", method: "POST")
        "/alaAdmin/systemMessage"(controller: "alaAdmin", action: "systemMessage", method: "POST")
        "/alaAdmin/clearMessage"(controller: "alaAdmin", action: "clearMessage", method: "POST")
        "/alaAdmin/buildInfo"(controller: "buildInfo", action: "index")
        "/alaAdmin"(controller: "alaAdmin", action: "index")
    }
}

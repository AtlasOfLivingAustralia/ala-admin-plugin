package au.org.ala.admin

class AlaAdminUrlMappings {

    static mappings = {
        "/alaAdmin/viewConfig" controller: "alaAdmin", action: [GET: "viewConfig"]
        "/alaAdmin/reloadConfig" controller: "alaAdmin", action: [POST: "reloadConfig"]
        "/alaAdmin/systemMessage" controller: "alaAdmin", action: [POST: "systemMessage"]
        "/alaAdmin/clearMessage" controller: "alaAdmin", action: [POST: "clearMessage"]
        "/alaAdmin/buildInfo" controller: "buildInfo", action: "index"
        "/alaAdmin" controller: "alaAdmin", action: [GET: "index"]
    }
}

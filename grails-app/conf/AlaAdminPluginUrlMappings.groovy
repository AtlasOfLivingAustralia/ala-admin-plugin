class AlaAdminPluginUrlMappings {

	static mappings = {
        "/alaAdmin/reloadConfig" controller: "alaAdmin", action: [POST: "reloadConfig"]
        "/alaAdmin/systemMessage" controller: "alaAdmin", action: [POST: "systemMessage"]
        "/alaAdmin/clearMessage" controller: "alaAdmin", action: [POST: "clearMessage"]
        "/alaAdmin" controller: "alaAdmin", action: [GET: "index"]
	}
}

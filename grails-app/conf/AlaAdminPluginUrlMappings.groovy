class AlaAdminPluginUrlMappings {

	static mappings = {
        "/alaAdmin/reloadConfig" controller: "alaAdmin", action: [POST: "reloadConfig"]
        "/alaAdmin" controller: "alaAdmin", action: [GET: "index"]
	}
}

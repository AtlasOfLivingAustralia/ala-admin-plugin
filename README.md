# ala-admin-plugin
Grails plugin containing common administrative functionality like reloading Grails config files or displaying a system message.

# Status
[![Build Status](https://travis-ci.org/AtlasOfLivingAustralia/ala-admin-plugin.svg?branch=master)](https://travis-ci.org/AtlasOfLivingAustralia/ala-admin-plugin)


# Usage

```
runtime ":ala-admin-plugin:x.y.z"
```

## Admin page

You will need to add ```/alaAdmin/?.*``` to the CAS ```uriFilterPattern``` property in order to view the Admin page. 
You will get a HTTP 403 if you do not do this.

The admin page can only be accessed by users with the ALA Admin role.

## System message

The system message is stored in a json file in the application's config directory under /data (```/data/[app-name]/config/system-message.json```).

To display the system message, add the ```<ala:systemMessage/>``` tag to your page. 

This tag will render a div with ```class="system-message alert alert-[severity]"```, so you can style it accordingly if the bootstrap alert style is not suitable.

There is an optional attribute ```showTimestamp``` which, when true, will display the timestamp of when the system message was saved in brackets after the message.

# Dev environment set up

1. Clone the repo
1. Import the source into your IDE
1. Use Grails version 2.5.2

To test changes locally, set the plugin as a local plugin on a grails application:

1. In the host application's BuildConfig.groovy
  1. Comment out (if present) the existing dependency on ala-admin-plugin
  1. Add ```grails.plugin.location.ala-admin-plugin = "/path/to/local/ala-admin-plugin"```
  
  
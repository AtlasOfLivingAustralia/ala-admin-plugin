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

You will need to include the [ala-auth-plugin](https://github.com/AtlasOfLivingAustralia/ala-auth-plugin) to do this.

### Embedding in another page

This plugin provides the admin form as a template which you can embed in an application-specific administration page if so desired. Simple render the template with the plugin reference:

```
<g:render template="/ala-admin-form" plugin="ala-admin-plugin"/>
```

NOTE: This template does not include the flash messages (so you can put it anywhere in the page), so you'll need to include some code at the top of your page to display flash messages. E.g.

```
<g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
</g:if>
```

## System message

The system message is stored in a json file in the application's config directory under /data (```/data/[app-name]/config/system-message.json```).

To display the system message, add the ```<ala:systemMessage/>``` tag to your page. 

This tag will render a div with ```class="system-message alert alert-[severity]"```, so you can style it accordingly if the bootstrap alert style is not suitable.

There is an optional attribute ```showTimestamp``` which, when true, will display the timestamp of when the system message was saved in brackets after the message.

## Environment message

If you include in your grails config a property called ```deployment_env``` with the name of the environment you are deploying to (e.g. prod, test, dev), then the ala-admin-plugin will display a message (using the ```<ala:systemMessage/>``` tag) on all non-production environments. A 'production' environment is one where the ```deployment_env``` property is blank, 'prod' or 'production'.

You can disable the environment warning by adding the ```hideEnvWarning=true``` attribute to the ```ala:systemMessage``` tag.

The environment message for a test environment would be "This is a TEST environment". This helps to avoid confusion for users who are involved in testing: they can clearly see which environment they are working in.

## Styling

Most features of this plugin rely on Bootstrap 3 being available. Some styles (e.g. the environment message) require the ```ala_admin``` resource bundle: ```<r:require modules="ala_admin"/>```.

## Build info
### Grails 2.x
The ala-admin plugin bundles the Grails Build-info plugin (https://grails.org/plugin/build-info), which provides a page containing all application properties and dependencies. 
If your host application is using the default URL mappings (```/$controller/$action?/$id?```), then the build info page will be available at ```.../buildInfo```. 
If you are not using the default URL mapping, then the alaAdmin plugin makes this page available under the secured path ```.../alaAdmin/buildInfo```. 

### Grails 3.x
The Build-info plugin does not work with Grails 3, so the functionality has been included in this plugin *in-situ*.

"Runtime Application Status" and "Installed plugins" will be displayed automatically but if the Git build info is required, then you need to add some extra config to your app. Add the following lines to you app's **`build.gradle`** (after the `buildscript` block):

```gradle
  
  plugins {
      id "com.gorylenko.gradle-git-properties" version "2.1.0"
  }

```

# Dev environment set up

Use a multi-project build as described [in the Grails plugin documentation](http://docs.grails.org/latest/guide/plugins.html#creatingAndInstallingPlugins)
  
  

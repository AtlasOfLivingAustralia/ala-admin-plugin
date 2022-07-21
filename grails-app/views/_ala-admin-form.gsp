<div class="panel-heading">
    <h3>Display System Message</h3>
</div>
<div class="panel-body">
    <g:form controller="alaAdmin" action="systemMessage">
        <g:set bean="systemMessageService" var="sms" />
        <p>This will display a system message on all pages in the application (assuming they render the message).</p>

        <div class="form-group">
            <label for="message" class="control-label">Message</label>
            <g:textField class="form-control" name="message" value="${sms.systemMessage?.text}"/>
        </div>
        <div class="form-group">
            <label for="severity" class="control-label">Severity</label> <span class="small">(used to style the message using Bootstrap's alert classes)</span>
            <g:select class="form-control" name="severity" from="${["info", "warning", "danger"]}" value="${sms.systemMessage?.severity}" />
        </div>

        <g:actionSubmit value="Set message" class="btn btn-primary" action="systemMessage"/>
        <g:actionSubmit value="Clear message" class="btn btn-default" action="clearMessage"/>
    </g:form>
</div>

<hr/>

<div class="panel-heading">
    <h3>Grails Config</h3>
</div>
<div class="panel-body">
    <g:form controller="alaAdmin" action="reloadConfig">
        <p>This lets you view the current Grails config object, and to reload the external configuration properties file ${grailsApplication.config.getProperty('default_config')}</p>
        <g:actionSubmit value="Reload external config" class="btn btn-primary" action="reloadConfig"/>
        <a href="${request.contextPath}/alaAdmin/viewConfig" class="btn btn-default">View current config</a>
    </g:form>
</div>
<hr/>

<div class="panel-heading">
    <h3>Build information</h3>
</div>
<div class="panel-body">
    <p>This will display a page listing all properties and dependencies of the host application</p>
    <g:link controller="buildInfo" action="index" class="btn btn-primary">View build info</g:link>
</div>
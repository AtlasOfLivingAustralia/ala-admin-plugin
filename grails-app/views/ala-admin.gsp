<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>ALA Admin</title>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body class="white-bg">
<div>
    <ala:systemMessage showTimestamp="true"/>

    <h1>ALA Administration</h1>
    <g:if test="${flash.message}">
        <div class="alert alert-info">${flash.message}</div>
    </g:if>

    <div class="panel-heading">
        <h3>Display System Message</h3>
    </div>
    <div class="panel-body">
        <g:form controller="alaAdmin" action="systemMessage">
            <p>This will display a system message on all pages in the application (assuming they render the message).</p>

            <div class="form-group">
                <label for="message" class="control-label">Message</label>
                <g:textField class="form-control" name="message" value="${params.text}"/>
            </div>
            <div class="form-group">
                <label for="severity" class="control-label">Severity</label> <span class="small">(used to style the message using Bootstrap's alert classes)</span>
                <g:select class="form-control" name="severity" from="${["info", "warning", "danger"]}" value="${params.severity}" />
            </div>

            <g:actionSubmit value="Set message" class="btn btn-primary" action="systemMessage"/>
            <g:actionSubmit value="Clear message" class="btn btn-default" action="clearMessage"/>
        </g:form>
    </div>

    <hr/>

    <div class="panel-heading">
        <h3>Reload Grails External Config File</h3>
    </div>
    <div class="panel-body">
        <g:form controller="alaAdmin" action="reloadConfig">
            <p>This will reload the external configuration properties file ${grailsApplication.config.default_config}</p>
            <g:actionSubmit value="Reload config" class="btn btn-primary" action="reloadConfig"/>
        </g:form>
    </div>
    <hr/>

    <div class="panel-heading">
        <h3>Build information</h3>
    </div>
    <div class="panel-body">
        <g:form controller="buildInfo" action="index" method="get">
            <p>This will display a page listing all properties and dependencies of the host application</p>
            <g:actionSubmit value="View build info" class="btn btn-primary" action="index"/>
        </g:form>
    </div>

</div>



</body>
</html>
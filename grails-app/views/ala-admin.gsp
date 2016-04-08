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

<body>
<div class="white-bg">
    <h1>ALA Administration</h1>
    <g:if test="${flash.message}">
        <div class="alert alert-info">${flash.message}</div>
    </g:if>

    <div class="panel-heading">
        <h3>Reload Grails External Config File</h3>
    </div>
    <div class="panel-body">
        <g:form controller="alaAdmin" action="reloadConfig">
            <p>This will reload the external configuration properties file ${grailsApplication.config.default_config}</p>
            <g:actionSubmit value="Reload config" class="btn btn-primary" action="reloadConfig"/>
        </g:form>
    </div>
</div>



</body>
</html>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="${grailsApplication.config.getProperty('skin.layout')}"/>
    <title>ALA Admin</title>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <asset:stylesheet src="ala-admin-asset.css" />
</head>

<body class="white-bg">
<div class="px-5">
    <ala:systemMessage showTimestamp="true"/>

    <div class="mb-4" style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">ALA Administration</h2>
        <a class="font-xxsmall" href="${request.contextPath ?: "/"}">Back to application</a>
    </div>
    <g:if test="${flash.message}">
        <div class="alert alert-info">${raw(flash.message)}</div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="alert alert-warning">${raw(flash.error)}</div>
    </g:if>

    <g:render template="/ala-admin-form"/>
</div>
</body>
</html>
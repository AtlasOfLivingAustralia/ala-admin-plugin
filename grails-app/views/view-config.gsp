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

    <plugin:isAvailable name="resources">
        <r:require modules="ala_admin"/>
    </plugin:isAvailable>
    <plugin:isNotAvailable name="resources">
        <asset:stylesheet src="ala-admin-asset.css" />
    </plugin:isNotAvailable>

</head>

<body class="white-bg">
<div>
    <ala:systemMessage showTimestamp="true"/>

    <ol class="breadcrumb hidden-print">
        <li><a class="font-xxsmall" href="${request.contextPath ?: "/"}">Back to application</a></li>
        <li><a class="font-xxsmall" href="${request.contextPath ?: "/"}alaAdmin">ALA Admin</a></li>
        <li class="font-xxsmall active">Grails config</li>
    </ol>

    <h1>ALA Administration</h1>

    <div class="panel-heading">
        <h2>Grails config</h2>
    </div>

    <div class="panel-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th scope="row">Property</th>
                    <th scope="row">Value</th>
                </tr>
                </thead>
                <tbody>

                <g:each in="${config.entrySet().sort { it.key }.flatten()}" var="prop">
                    <tr>
                        <td>${prop.key}</td>
                        <td>${prop.value}</td>
                </g:each>
                </tbody>
            </table>

        </div>
    </div>

</div>

</body>
</html>
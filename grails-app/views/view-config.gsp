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
<div>
    <ala:systemMessage showTimestamp="true"/>

    <nav aria-label="breadcrumb" class="d-print-none">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a class="font-xxsmall" href="${request.contextPath ?: "/"}">Back to application</a>
            </li>
            <li class="breadcrumb-item">
                <a class="font-xxsmall" href="${request.contextPath ?: "/"}alaAdmin">ALA Admin</a>
            </li>
            <li class="breadcrumb-item active font-xxsmall" aria-current="page">
                Grails config
            </li>
        </ol>
    </nav>

    <h1>ALA Administration</h1>

    <div class="card">
        <div class="card-header">
            <h2 class="mb-0">Grails config</h2>
        </div>

        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th scope="col">Property</th>
                        <th scope="col">Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${config.entrySet().sort { it.key }.flatten()}" var="prop">
                        <tr>
                            <td>${prop.key}</td>
                            <td>${prop.value}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

</body>
</html>
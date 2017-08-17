<h1><g:message code="plugin.title"/> </h1>
    <g:if test="${buildInfoProperties}">
        <table>
            <g:each in="${buildInfoProperties}" var="key,value">
                <tr>
                    <td><g:message code="${key}"/></td><td>${value}</td>
                </tr>
            </g:each>
        </table>
    </g:if>
    <g:else>
        <g:render template="gitBuildHowTo" plugin="buildInfo"/>
    </g:else>


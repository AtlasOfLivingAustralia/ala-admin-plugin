/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.org.ala.admin

import grails.converters.JSON
import grails.converters.XML
import grails.util.Environment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.GitProperties

class BuildInfoController {
    def pluginManager

    @Autowired(required = false)
    GitProperties gitProperties

    static final List buildInfoProperties = [
            'info.app.build.date',
            'scm.version',
            'environment.BUILD_NUMBER',
            'environment.BUILD_ID',
            'environment.BUILD_TAG',
            'environment.GIT_BRANCH',
            'environment.GIT_COMMIT'
    ]

    def index = {
        def buildInfoConfig = grailsApplication.config?.buildInfo
        def customProperties = buildInfoProperties

        if (buildInfoConfig?.properties?.exclude){
            customProperties -= buildInfoConfig.properties.exclude
        }
        if (buildInfoConfig?.properties?.add){
            customProperties += buildInfoConfig.properties.add
        }

        def buildInfo = [:]
        customProperties.findAll { grailsApplication?.metadata[it] }.each {
            buildInfo."${it}" = grailsApplication?.metadata[it]
        }

        if (gitProperties) {
            buildInfo."git.commit.date" = gitProperties.commitTime ? g.formatDate(date: gitProperties.commitTime, format:"yyyy-MM-dd'T'HH:mm:ssZ") : ""
            buildInfo."git.commit.id" = gitProperties.commitId
            buildInfo."git.commit.shortId" = gitProperties.shortCommitId
            buildInfo."git.branch" = gitProperties.branch

        }

        def installedPlugins = [:]
        pluginManager?.allPlugins?.sort({it.name.toUpperCase()})?.each {
            installedPlugins."${it.name}" = it.version
        }

        def runtimeEnvironment = [
            environment: Environment.current.name,
            'app.version': grailsApplication?.metadata['info.app.version'],
            'app.grails.version': grailsApplication?.metadata['info.app.grailsVersion'],
            'java.version': System.getProperty('java.version')
        ]

        Map model = [
                buildInfoProperties: buildInfo,
                installedPlugins: installedPlugins,
                runtimeEnvironment: runtimeEnvironment
        ]

        log.debug "model = ${model as JSON}"

        withFormat {
            html { render(view: 'index', model: model) }
            json { render model as JSON }
            xml { render model as XML }
        }
    }
}

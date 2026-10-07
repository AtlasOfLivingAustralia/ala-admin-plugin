package au.org.ala.admin

import spock.lang.Specification
import spock.lang.Unroll

class ConfigUtilitySpec extends Specification {

    void "flatten should flatten nested map structures with dot notation"() {
        given:
        Map input = [
            app: [
                name: "ala-bie-hub",
                version: "1.0",
                server: [
                    port: 8080
                ]
            ],
            simple: "value"
        ]

        when:
        Map<String, Object> result = ConfigUtility.flatten(input)

        then:
        result.size() == 4
        result['app.name'] == "ala-bie-hub"
        result['app.version'] == "1.0"
        result['app.server.port'] == 8080
        result['simple'] == "value"
    }

    void "flatten should preserve all keys and not collapse into a single 'key' entry"() {
        given:
        Map input = [
            a: "first",
            b: "second",
            c: "third",
            d: [
                e: "fourth",
                f: "fifth"
            ]
        ]

        when:
        Map<String, Object> result = ConfigUtility.flatten(input)

        then:
        result.size() == 5
        result.keySet().containsAll(['a', 'b', 'c', 'd.e', 'd.f'])
        !result.containsKey('key')
    }

    void "flatten should handle empty or null maps"() {
        expect:
        ConfigUtility.flatten(null) == [:]
        ConfigUtility.flatten([:]) == [:]
    }

    void "sanitise should mask sensitive keys preserving last 4 characters when length > 4"() {
        given:
        Map config = [
            'api.secret.token': 'abcdef1234',
            'auth.password': 'secretpassword99',
            'security.api.key': 'my-api-key-5678'
        ]

        when:
        Map<String, Object> result = ConfigUtility.sanitise(config)

        then:
        result['api.secret.token'] == '******1234'
        result['auth.password'] == '************rd99'
        result['security.api.key'] == '***********5678'
    }

    void "sanitise should mask short sensitive values of 4 or fewer characters to ****"() {
        given:
        Map config = [
            'db.password': 'pass',
            'secret.pin': '123'
        ]

        when:
        Map<String, Object> result = ConfigUtility.sanitise(config)

        then:
        result['db.password'] == '****'
        result['secret.pin'] == '****'
    }

    void "sanitise should not mask null or empty values"() {
        given:
        Map config = [
            'db.password': null,
            'api.token': ''
        ]

        when:
        Map<String, Object> result = ConfigUtility.sanitise(config)

        then:
        result['db.password'] == null
        result['api.token'] == ''
    }

    void "sanitise should not mask boolean values even if key contains sensitive words"() {
        given:
        Map config = [
            'security.auth.enabled': false,
            'cas.auth.enabled': true,
            'auth.disable': false
        ]

        when:
        Map<String, Object> result = ConfigUtility.sanitise(config)

        then:
        result['security.auth.enabled'] == false
        result['cas.auth.enabled'] == true
        result['auth.disable'] == false
    }

    void "sanitise should not mask non-sensitive properties"() {
        given:
        Map config = [
            'app.name': 'ala-bie-hub',
            'server.port': 8080,
            'grails.serverURL': 'https://bie.ala.org.au'
        ]

        when:
        Map<String, Object> result = ConfigUtility.sanitise(config)

        then:
        result['app.name'] == 'ala-bie-hub'
        result['server.port'] == 8080
        result['grails.serverURL'] == 'https://bie.ala.org.au'
    }

    @Unroll
    void "isSensitiveKey matches pattern '#pattern' in '#key'"() {
        expect:
        ConfigUtility.sanitise([(key): 'verylongsecretvalue'])[(key)] == '***************alue'

        where:
        pattern      | key
        'password'   | 'user.password'
        'secret'     | 'client.secret'
        'key'        | 'google.maps.api.key'
        'token'      | 'jwt.token'
        'credential' | 'aws.credential.data'
        'private'    | 'ssh.private.key'
        'auth'       | 'oauth.auth.token'
        'bearer'     | 'http.bearer.header'
        'signature'  | 'webhook.signature'
        'pwd'        | 'admin.pwd'
    }
}

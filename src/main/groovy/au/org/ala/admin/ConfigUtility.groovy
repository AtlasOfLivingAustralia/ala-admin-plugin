/*
 * Copyright (C) 2026 Atlas of Living Australia
 * All Rights Reserved.
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.admin

/**
 * Utility class to flatten and sanitise sensitive configuration values.
 *
 * @author nickdos https://github.com/nickdos
 * @since 2026-06-20
 */
class ConfigUtility {
    /**
     * List of sensitive key patterns to identify sensitive configuration values.
     * Keys containing any of these patterns will be masked in the output.
     */
    private static final List<String> SENSITIVE_KEY_PATTERNS = [
        'password', 'secret', 'key', 'token', 'credential',
        'private', 'auth', 'bearer', 'signature', 'pwd'
    ]

    /**
     * Flattens a nested Map (such as a ConfigObject) into a single-level Map
     * with dot-separated key paths.
     */
     static Map<String, Object> flatten(Map<?, ?> sourceMap, String separator = '.') {
        Map<String, Object> result = [:]
        if (!sourceMap) return result

        sourceMap.each { Object key, Object value ->
            String prefix = key.toString()
            if (value instanceof Map) {
                Map<String, Object> nested = flatten((Map<?, ?>) value, separator)
                nested.each { String nestedKey, Object nestedValue ->
                    result["${prefix}${separator}${nestedKey}".toString()] = nestedValue
                }
            } else {
                result[prefix] = value
            }
        }

        return sanitise(result)
    }

    /**
     * Sanitises sensitive values in a flattened configuration Map.
     * Sensitive keys are masked to prevent exposure of sensitive information.
     */
    static Map<String, Object> sanitise(Map<String, Object> flattenedConfig) {
        if (!flattenedConfig) return [:]

        flattenedConfig.collectEntries { String key, Object value ->
            [key: isSensitiveKey(key) ? maskValue(value) : value]
        }
    }

    /**
     * Checks if a key is considered sensitive based on predefined patterns.
     */
    private static boolean isSensitiveKey(String key) {
        if (!key) return false
        String lowerKey = key.toLowerCase()
        SENSITIVE_KEY_PATTERNS.any { pattern -> lowerKey.contains(pattern) }
    }

    /**
     * Masks a sensitive value, preserving the last 4 characters if possible.
     */
    private static Object maskValue(Object value) {
        if (value == null) return null

        String strValue = value.toString()
        int length = strValue.length()

        if (length <= 4) {
            return '****'
        }

        '*' * (length - 4) + strValue.substring(length - 4)
    }
}

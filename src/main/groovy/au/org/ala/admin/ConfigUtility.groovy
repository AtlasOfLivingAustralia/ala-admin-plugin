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
     * with dot-separated key paths, and sanitises sensitive values.
     */
    static Map<String, Object> flatten(Map<?, ?> sourceMap, String separator = '.') {
        Map<String, Object> flattened = [:]
        flattenMap(flattened, sourceMap, "", separator)
        return sanitise(flattened)
    }

    private static void flattenMap(Map<String, Object> result, Map<?, ?> sourceMap, String prefix, String separator) {
        if (!sourceMap) return

        sourceMap.each { Object key, Object value ->
            String fullKey = prefix ? "${prefix}${separator}${key}" : key.toString()
            if (value instanceof Map) {
                flattenMap(result, (Map<?, ?>) value, fullKey, separator)
            } else {
                result[fullKey] = value
            }
        }
    }

    /**
     * Sanitises sensitive values in a flattened configuration Map.
     * Sensitive keys are masked to prevent exposure of sensitive information.
     */
    static Map<String, Object> sanitise(Map<String, Object> flattenedConfig) {
        if (!flattenedConfig) return [:]

        flattenedConfig.collectEntries { String key, Object value ->
            [(key): isSensitive(key, value) ? maskValue(value) : value]
        }
    }

    /**
     * Determines whether a key/value pair should be masked.
     * Booleans (e.g. auth.enabled) and non-sensitive keys are not masked.
     */
    private static boolean isSensitive(String key, Object value) {
        if (value instanceof Boolean) {
            return false
        }
        return isSensitiveKey(key)
    }

    /**
     * Checks if a key is considered sensitive based on predefined patterns.
     */
    private static boolean isSensitiveKey(String key) {
        if (!key) return false
        String lowerKey = key.toLowerCase()
        if (lowerKey.endsWith('.enabled') || lowerKey.endsWith('.disabled') ||
            lowerKey.endsWith('.enable') || lowerKey.endsWith('.disable')) {
            return false
        }
        SENSITIVE_KEY_PATTERNS.any { pattern -> lowerKey.contains(pattern) }
    }

    /**
     * Masks a sensitive value, preserving the last 4 characters if possible.
     */
    private static Object maskValue(Object value) {
        if (value == null) return null

        String strValue = value.toString()
        if (strValue.isEmpty()) {
            return ""
        }

        int length = strValue.length()
        if (length <= 4) {
            return '****'
        }

        '*' * (length - 4) + strValue.substring(length - 4)
    }
}

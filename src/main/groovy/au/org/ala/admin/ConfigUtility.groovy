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
     **/
    public static final List<String> SENSITIVE_KEY_PATTERNS = [
        'password', 'secret', 'key', 'token', 'credential',
        'private', 'bearer', 'signature', 'pwd'
    ].asImmutable()

    /**
     * Flattens a nested Map (such as a ConfigObject) into a single-level Map
     * with dot-separated key paths, and optionally sanitises sensitive values.
     *
     * @param sourceMap The source Map to flatten.
     * @param separator The separator to use between keys (default is '.').
     * @param maskSensitiveValues Whether to mask sensitive values (default is true).
     * @param sensitiveKeyPatterns Optional patterns to identify sensitive keys.
     * @return A flattened and optionally sanitised Map.
     */
    static Map<String, Object> flatten(Map<?, ?> sourceMap, String separator = '.', boolean maskSensitiveValues = true, Object sensitiveKeyPatterns = null) {
        Map<String, Object> flattened = [:]
        flattenMap(flattened, sourceMap, "", separator)
        return maskSensitiveValues ? sanitise(flattened, sensitiveKeyPatterns) : flattened
    }

    /**
     * Recursively flattens a nested Map into a single-level Map with dot-separated key paths.
     *
     * @param result The Map to store the flattened key-value pairs.
     * @param sourceMap The source Map to flatten.
     * @param prefix The current key prefix for nested keys.
     * @param separator The separator to use between keys (default is '.').
     */
    private static void flattenMap(Map<String, Object> result, Map<?, ?> sourceMap, String prefix, String separator) {
        if (!sourceMap) return

        sourceMap.each { entry ->
            Object key = entry.key
            Object value = entry.value
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
     *
     * @param flattenedConfig The flattened configuration Map to sanitise.
     * @param sensitiveKeyPatterns Optional patterns to identify sensitive keys.
     * @return A new Map with sensitive values masked.
     */
    static Map<String, Object> sanitise(Map<String, Object> flattenedConfig, Object sensitiveKeyPatterns = null) {
        if (!flattenedConfig) return [:]

        flattenedConfig.collectEntries { String key, Object value ->
            [(key): isSensitive(key, value, sensitiveKeyPatterns) ? maskValue(value) : value]
        }
    }

    /**
     * Determines whether a key/value pair should be masked.
     * Booleans (e.g. auth.enabled) and non-sensitive keys are not masked.
     *
     * @param key The configuration key to check.
     * @param value The configuration value associated with the key.
     * @param sensitiveKeyPatterns The patterns to use for identifying sensitive keys.
     * @return true if the key/value pair should be masked, false otherwise.
     */
    private static boolean isSensitive(String key, Object value, Object sensitiveKeyPatterns = null) {
        if (value instanceof Boolean) {
            return false
        }
        return isSensitiveKey(key, sensitiveKeyPatterns)
    }

    /**
     * Checks if a key is considered sensitive based on predefined patterns.
     *
     * @param key The configuration key to check.
     * @param sensitiveKeyPatterns The patterns to use for identifying sensitive keys.
     * @return true if the key is considered sensitive, false otherwise.
     */
    private static boolean isSensitiveKey(String key, Object sensitiveKeyPatterns = null) {
        if (!key) return false
        String lowerKey = key.toLowerCase()
        if (lowerKey.endsWith('.enabled') || lowerKey.endsWith('.disabled') ||
            lowerKey.endsWith('.enable') || lowerKey.endsWith('.disable')) {
            return false
        }
        List<String> patterns = resolvePatterns(sensitiveKeyPatterns)
        patterns.any { pattern -> pattern && lowerKey.contains(pattern.toLowerCase()) }
    }

    /**
     * Resolves the sensitive key patterns from the provided input.
     *
     * @param sensitiveKeyPatterns The patterns to use for identifying sensitive keys.
     * @return A list of sensitive key patterns.
     */
    private static List<String> resolvePatterns(Object sensitiveKeyPatterns) {
        if (sensitiveKeyPatterns instanceof Collection) {
            return sensitiveKeyPatterns.collect { it?.toString()?.trim() }.findAll { it }
        } else if (sensitiveKeyPatterns instanceof String && !sensitiveKeyPatterns.trim().isEmpty()) {
            return sensitiveKeyPatterns.split(',').collect { it.trim() }.findAll { it }
        }
        return SENSITIVE_KEY_PATTERNS
    }

    /**
     * Masks a sensitive value, preserving the last 4 characters if possible.
     *
     * @param value The value to mask.
     * @return The masked value.
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

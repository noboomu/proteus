package io.sinistral.proteus.openapi.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for parsing and processing paths
 */
public class PathUtils {
    private static final Pattern PATH_PARAM_PATTERN = Pattern.compile("\\{([^}]+)\\}");

    /**
     * Parse a path and extract path parameters
     *
     * @param path the path to parse
     * @param regexMap map to store regex patterns for path parameters
     * @return the parsed path
     */
    public static String parsePath(String path, Map<String, String> regexMap) {
        if (path == null || path.isEmpty()) {
            return path;
        }

        // Remove trailing slashes
        if (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }

        // Extract path parameters with regex patterns like {id:regex}
        Matcher matcher = PATH_PARAM_PATTERN.matcher(path);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String param = matcher.group(1);
            String paramName;
            String regex = null;

            // Check if parameter has regex pattern
            int colonIndex = param.indexOf(':');
            if (colonIndex > 0) {
                paramName = param.substring(0, colonIndex).trim();
                regex = param.substring(colonIndex + 1).trim();
                if (regexMap != null) {
                    regexMap.put(paramName, regex);
                }
                // Replace with standard OpenAPI format
                matcher.appendReplacement(result, "{" + paramName + "}");
            } else {
                paramName = param.trim();
                matcher.appendReplacement(result, "{" + paramName + "}");
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Combine path segments
     */
    public static String combinePath(String parent, String child) {
        if (parent == null || parent.isEmpty()) {
            return child;
        }
        if (child == null || child.isEmpty()) {
            return parent;
        }

        // Normalize paths
        if (!parent.startsWith("/")) {
            parent = "/" + parent;
        }
        if (parent.endsWith("/")) {
            parent = parent.substring(0, parent.length() - 1);
        }
        if (!child.startsWith("/")) {
            child = "/" + child;
        }

        return parent + child;
    }
}

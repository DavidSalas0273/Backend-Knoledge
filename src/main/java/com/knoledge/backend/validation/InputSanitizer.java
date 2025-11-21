package com.knoledge.backend.validation;

import java.util.regex.Pattern;

public final class InputSanitizer {

    private static final Pattern UNSAFE_SYMBOLS = Pattern.compile("[<>\"'{}\\[\\]\\\\/|]");
    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s{2,}");

    private InputSanitizer() {
    }

    public static String sanitizeName(String value) {
        String cleaned = sanitizeBasic(value);
        return cleaned != null ? MULTIPLE_SPACES.matcher(cleaned).replaceAll(" ") : null;
    }

    public static String sanitizeText(String value) {
        return sanitizeBasic(value);
    }

    public static String sanitizeEmail(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    public static String sanitizePassword(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private static String sanitizeBasic(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.stripLeading();
        return UNSAFE_SYMBOLS.matcher(trimmed).replaceAll("").stripTrailing();
    }
}

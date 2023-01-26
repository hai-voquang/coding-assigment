package ca.ns.assignment.utils;

import org.apache.commons.lang3.StringUtils;

public final class InputUtils {

//    private static final String WHITELIST_REGEX = "[^a-zA-Z0-9\\s]";
    private static final String WHITELIST_REGEX = "<[^>]*>";

    private InputUtils() {
        //no implementation
    }
    public static String sanitizeText(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        return text.replaceAll(WHITELIST_REGEX, "");
    }
}

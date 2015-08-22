package com.learn.turtorial1.library.dmobi.Utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class Utils {
    /**
     * Trim specified charcater from front of string
     *
     * @param text
     *          Text
     * @param character
     *          Character to remove
     * @return Trimmed text
     */
    public static String trimFront(String text, char character) {
        String normalizedText;
        int index;

        if (StringUtils.isEmpty(text)) {
            return text;
        }

        normalizedText = text.trim();
        index = 0;

        while (normalizedText.charAt(index) == character) {
            index++;
        }
        return normalizedText.substring(index).trim();
    }

    /**
     * Trim specified charcater from end of string
     *
     * @param text
     *          Text
     * @param character
     *          Character to remove
     * @return Trimmed text
     */
    public static String trimEnd(String text, char character) {
        String normalizedText;
        int index;

        if (StringUtils.isEmpty(text)) {
            return text;
        }

        normalizedText = text.trim();
        index = normalizedText.length() - 1;

        while (normalizedText.charAt(index) == character) {
            if (--index < 0) {
                return "";
            }
        }
        return normalizedText.substring(0, index + 1).trim();
    }

    /**
     * Trim specified charcater from both ends of a String
     *
     * @param text
     *          Text
     * @param character
     *          Character to remove
     * @return Trimmed text
     */
    public static String trimAll(String text, char character) {
        String normalizedText = trimFront(text, character);

        return trimEnd(normalizedText, character);
    }
}

package com.utsman.hiyahiyahiya.utils.url_utils;

import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UrlUtil {
    public static String extractUrl(String input) {
        List<String> result = new ArrayList<>();
        String[] words = input.split("\\s+");
        Pattern pattern = Patterns.WEB_URL;
        for (String word : words) {
            if (pattern.matcher(word).find()) {
                result.add(word);
            }
        }

        return result.toString()
                .replace("[", "")
                .replace("]", "");
    }

    public static Boolean isUrl(String input) {
        Pattern pattern = Patterns.WEB_URL;
        if (input.contains("http://") || input.contains("https://")) {
            return pattern.matcher(input).find();
        } else  {
            return false;
        }
    }
}

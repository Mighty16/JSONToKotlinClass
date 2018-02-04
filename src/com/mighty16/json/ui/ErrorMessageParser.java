package com.mighty16.json.ui;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorMessageParser {

    private Pattern jsonCharacterErrorPattern = Pattern.compile("(?<=\\[character )(.*)(?= line )");
    private Pattern jsonLineErrorPattern = Pattern.compile("(?<=line )(.*)(?=])");

    public ErrorLocation findErrorLocation(String errorMessage) {
        try {
            Matcher characterMatcher = jsonCharacterErrorPattern.matcher(errorMessage);
            if (characterMatcher.find()) {
                String characterNumber = errorMessage.substring(characterMatcher.start(), characterMatcher.end());
                String lineNumber = null;
                Matcher lineMatcher = jsonLineErrorPattern.matcher(errorMessage);
                if (lineMatcher.find()) {
                    lineNumber = errorMessage.substring(lineMatcher.start(), lineMatcher.end());
                }
                if (lineNumber == null) return null;
                return new ErrorLocation(Integer.valueOf(lineNumber), Integer.valueOf(characterNumber));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class ErrorLocation {
        public int line;
        public int character;

        public ErrorLocation(int line, int character) {
            this.line = line;
            this.character = character;
        }
    }
}

package com.mighty16.json.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONColorizer {

    private static final String[] WORDS = {"\\{", "\\}"};

    private JTextPane editorPane;

    private StyledDocument document;

    private StyleContext styleContext = StyleContext.getDefaultStyleContext();
    private AttributeSet greenAttributeSet;
    private AttributeSet normalAttributeSet;
    private AttributeSet jsonKeyAttributeSet;

    private Highlighter highlighter;
    private Highlighter.HighlightPainter painter;

    Pattern pattern = getBracketsPattern();
    Pattern jsonKeyPattern = Pattern.compile("\"([^\"]*)\":");

    private Object last;

    public JSONColorizer(JTextPane editorPane) {
        this.editorPane = editorPane;
        document = editorPane.getStyledDocument();
        greenAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
        normalAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
        jsonKeyAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.CYAN);

        highlighter = editorPane.getHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
    }

    public void colorize() {
        document.setCharacterAttributes(0, editorPane.getText().length(), normalAttributeSet, true);

        // Look for tokens and highlight them
        Matcher matcher = pattern.matcher(editorPane.getText());
        while (matcher.find()) {
            // Change the color of recognized tokens
            document.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), greenAttributeSet, false);
        }

        Matcher keysMatcher = jsonKeyPattern.matcher(editorPane.getText());
        while (keysMatcher.find()) {
            // Change the color of recognized tokens
            document.setCharacterAttributes(keysMatcher.start(),
                    keysMatcher.end() - keysMatcher.start(), jsonKeyAttributeSet, false);
        }
    }

    public void clearErrorHighLight(){
        if (last != null) {
            highlighter.removeHighlight(last);
            last = null;
        }
    }

    public void highlightError(int line, int character) {
        Element root = document.getDefaultRootElement();
        int startOfLineOffset = root.getElement(line - 1).getStartOffset() + (character - 1);
        try {
            if (last != null) {
                highlighter.removeHighlight(last);
                last = null;
            }
            last = highlighter.addHighlight(startOfLineOffset - 1, startOfLineOffset, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    private Pattern buildPattern() {
        StringBuilder sb = new StringBuilder();
        for (String token : WORDS) {
            sb.append("\\b"); // Start of word boundary
            sb.append(token);
            sb.append("\\b|"); // End of word boundary and an or for the next word
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
        }
        return Pattern.compile(sb.toString());
    }

    private Pattern getBracketsPattern() {
        return Pattern.compile("\\{|\\}");
    }


}

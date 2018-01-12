package com.mighty16.json.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONColorizer {

    private JTextPane editorPane;

    private StyledDocument document;

    private StyleContext styleContext = StyleContext.getDefaultStyleContext();
    private AttributeSet greenAttributeSet;
    private AttributeSet normalAttributeSet;
    private AttributeSet jsonKeyAttributeSet;

    private Highlighter highlighter;
    private Highlighter.HighlightPainter painter;

    //Pattern bracketsPattern = Pattern.compile("\\{|\\}|\\[|\\]");
    Pattern bracketsPattern = Pattern.compile("(?<!\")(?!\")(\\{|\\}|\\[|\\])");
    Pattern jsonKeyPattern = Pattern.compile("\"([^\"]*)\":");



    private Object last;

    public JSONColorizer(JTextPane editorPane) {
        this.editorPane = editorPane;
        document = editorPane.getStyledDocument();

        Color currentColor = document.getForeground(editorPane.getInputAttributes());
        Color green = new Color(0,188,18);

        greenAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
                StyleConstants.Foreground, Color.RED);
        normalAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
                StyleConstants.Foreground, currentColor);
        jsonKeyAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
                StyleConstants.Foreground, green);

        highlighter = editorPane.getHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    }

    public void colorize() {
        document.setCharacterAttributes(0, editorPane.getText().length(), normalAttributeSet, true);
        Matcher matcher = bracketsPattern.matcher(editorPane.getText());
        while (matcher.find()) {
            document.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), greenAttributeSet, false);
        }

        Matcher keysMatcher = jsonKeyPattern.matcher(editorPane.getText());
        while (keysMatcher.find()) {
            document.setCharacterAttributes(keysMatcher.start(),
                    keysMatcher.end() - keysMatcher.start(), jsonKeyAttributeSet, false);
        }
    }

    public void clearErrorHighLight() {
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
}

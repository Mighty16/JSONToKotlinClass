package com.mighty16.json.ui;


public class TextResources {

    public String getActionTitle() {
        return "JSON Kotlin Models";
    }

    public String getJSONDialogTitle() {
        return "Generate class from JSON";
    }

    public String getReplaceDialogTitle() {
        return "Replace File";
    }

    public String getReplaceDialogMessage(String fileName) {
        return "Replace \"" + fileName + "\"?";
    }

    public String getJSONErrorTitle() {
        return "JSON Parsing Error";
    }

    public String getErrorMessage(String error) {
        return "ERROR: " + error;
    }

    public String getJSONErrorMessage(String error) {
        return "JSON ERROR: " + error;
    }

    public String getEmptyJSONTitle() {
        return "Error";
    }

    public String getEmptyJSONMessage() {
        return "JSON is empty!";
    }

    public String getEmptyClassNameTitle() {
        return "Error";
    }

    public String getEmptyClassMessage() {
        return "Class name is empty!";
    }

    public String getGeneratedFilesMessage(int filesCount) {
        return filesCount + " data " + ((filesCount == 1) ? "class" : "classes") + " generated from JSON";
    }

    public String getCutCommand() {
        return "Cut";
    }

    public String getPasteCommand() {
        return "Paste";
    }

    public String getCopyCommand() {
        return "Copy";
    }

    public String[] getFieldsTableColumns() {
        return new String[]{"Enabled", "Field name", "var/val", "Optional", "Default value", "Original value"};
    }

    public String getFieldsDialogTitle() {
        return "Class fields settings";
    }

    public String getEmptyFileNameTitle() {
        return "Error";
    }

    public String getEmptyFileNameMessage() {
        return "File name is empty!";
    }

}

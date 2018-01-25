package com.mighty16.json.ui;

import com.intellij.openapi.ui.ComboBox;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.models.FieldModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.HashMap;
import java.util.List;

public class FieldsTableDelegate {

    private JTable fieldsTable;
    private String columns[];
    private List<FieldModel> fieldsData;
    private LanguageResolver languageResolver;
    private HashMap<String, String> classNames;


    public FieldsTableDelegate(JTable fieldsTable, HashMap<String, String> classNames, LanguageResolver resolver) {
        this.fieldsTable = fieldsTable;
        this.languageResolver = resolver;
        this.classNames = classNames;
        columns = new String[]{"Enabled", "Field name", "var/val", "Type", "Default value", "Original value"};
    }

    public void setClass(ClassModel classModel) {
        fieldsData = classModel.fields;
        fieldsTable.setModel(new FieldsTableModel(fieldsData));

        TableColumn column = fieldsTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(30);

        TableColumn modifierColumn = fieldsTable.getColumnModel().getColumn(2);
        ComboBox<String> modifierCombobox = new ComboBox<>();
        modifierCombobox.addItem("var");
        modifierCombobox.addItem("val");

        modifierColumn.setCellEditor(new DefaultCellEditor(modifierCombobox));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < columns.length; i++) {
            fieldsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public List<FieldModel> getFieldsData() {
        return fieldsData;
    }


    class FieldsTableModel extends AbstractTableModel {

        public List<FieldModel> items;

        public FieldsTableModel(List<FieldModel> items) {
            this.items = items;
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Boolean.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                case 4:
                    return String.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            FieldModel fieldData = items.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return fieldData.enabled;
                case 1:
                    return fieldData.name;
                case 2:
                    return languageResolver.getModifier(fieldData.mutable);
                case 3:
                    String className = classNames.get(fieldData.type);
                    if (className != null) {
                        return className;
                    } else {
                        return fieldData.type;
                    }
                case 4:
                    return fieldData.defaultValue;
                case 5:
                    return fieldData.originalValue;
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            FieldModel fieldData = items.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    fieldData.enabled = (Boolean) aValue;
                    break;
                case 1:
                    fieldData.name = (String) aValue;
                    break;
                case 2:
                    fieldData.mutable = languageResolver.isModifierMutable((String) aValue);
                    break;
                case 3:
                    fieldData.type = (String) aValue;
                    break;
                case 4:
                    fieldData.defaultValue = (String) aValue;
                    break;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 5) {
                return false;
            }
//            if (col == 3) {
//                return languageResolver.canChangeType(fieldsData.get(row).type);
//            }
            return true;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }
    }
}

package com.mighty16.json.ui;

import com.intellij.openapi.ui.ComboBox;
import com.mighty16.json.TypesResolver;
import com.mighty16.json.models.FieldModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.util.List;

public class FieldsTableDelegate {

    private JTable fieldsTable;
    private String columns[];
    private List<FieldModel> fieldsData;
    private TypesResolver typesResolver;


    public FieldsTableDelegate(JTable fieldsTable, TypesResolver resolver) {
        this.fieldsTable = fieldsTable;
        this.typesResolver = resolver;
        columns = new String[]{"Enabled", "Field name", "Type", "Original value"};
    }

    public void setFieldsData(List<FieldModel> fields) {
        fieldsData = fields;
        fieldsTable.setModel(new FieldsTableModel(fieldsData));

        TableColumn column = fieldsTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(30);

        TableColumn typeColumn = fieldsTable.getColumnModel().getColumn(2);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.addItem(FieldModel.TYPE_STRING);
        comboBox.addItem(FieldModel.TYPE_DOUBLE);
        comboBox.addItem(FieldModel.TYPE_LONG);
        comboBox.addItem(FieldModel.TYPE_INT);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
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
                    return fieldData.type;
                case 3:
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
                    fieldData.type = (String) aValue;
                    break;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 3) {
                return false;
            } else if (col == 2) {
                return typesResolver.canChangeType(fieldsData.get(row).type);
            }
            return true;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }
    }
}

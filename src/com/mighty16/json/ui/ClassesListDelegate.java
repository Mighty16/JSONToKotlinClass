package com.mighty16.json.ui;

import com.mighty16.json.core.models.ClassModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;

public class ClassesListDelegate {


    public ClassesListDelegate(JTable list, List<ClassModel> classDataList, HashMap<String, String> classNames,
                               OnClassSelectedListener listener) {
        if (classDataList.size() == 1) {
            list.setVisible(false);
        } else {
            list.setVisible(true);
        }

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setModel(new ClassesListModel(classDataList, classNames));

        ListSelectionModel selectionModel = list.getSelectionModel();

        selectionModel.addListSelectionListener(e -> {
            int[] selectedRow = list.getSelectedRows();
            if (selectedRow == null) return;
            if (selectedRow.length == 0) return;
            int selectedIndex = selectedRow[0];
            if (listener != null) {
                listener.onClassSelected(classDataList.get(selectedIndex), selectedIndex);
            }
        });
    }

    public interface OnClassSelectedListener {
        void onClassSelected(ClassModel classData, int index);
    }

    class ClassesListModel extends AbstractTableModel {

        private HashMap<String, String> classNames;
        private List<ClassModel> classData;

        public ClassesListModel(List<ClassModel> classDataList, HashMap<String, String> classNames) {
            this.classData = classDataList;
            this.classNames = classNames;
        }

        @Override
        public int getRowCount() {
            return classData.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return classNames.get(classData.get(rowIndex).name);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            String newValue = (String) aValue;
            classNames.put(classData.get(rowIndex).name, newValue);
        }

        @Override
        public String getColumnName(int column) {
            return "";
        }
    }

}

package com.mighty16.json.ui;

import com.intellij.ui.JBColor;
import com.mighty16.json.models.ClassModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;

public class ClassesListDelegate {

    private OnClassSelectedListener listener;

    public ClassesListDelegate(JTable list, List<ClassModel> classDataList, HashMap<String, String> classNames, OnClassSelectedListener listener) {

        this.listener = listener;

        if (classDataList.size() == 1) {
            list.setVisible(false);
        } else {
            list.setVisible(true);
        }

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setModel(new ClassesListModel(classDataList,classNames));

        ListSelectionModel selectionModel = list.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = e.getFirstIndex();

                if (listener != null) {
                   listener.onClassSelected(classDataList.get(selectedIndex));
               }

            }
        });


        //list.setBorder(BorderFactory.createLineBorder(JBColor.BLACK, 1));

//        list.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (listener != null) {
//                    listener.onClassSelected(classData.get(list.getSelectedIndex()));
//                }
//            }
//        });

    }


    public interface OnClassSelectedListener {
        void onClassSelected(ClassModel classData);
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
            classNames.put(classData.get(rowIndex).name, (String) aValue);
        }

        @Override
        public String getColumnName(int column) {
            return "";
        }
    }

}

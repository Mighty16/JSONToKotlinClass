package com.mighty16.json.ui;

import com.intellij.ui.JBColor;
import com.mighty16.json.models.ClassModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class ClassesListDelegate {

    private List<ClassModel> classData;
    private OnClassSelectedListener listener;

    public ClassesListDelegate(JList list, OnClassSelectedListener listener, List<ClassModel> classDataList) {
        this.classData = classDataList;
        this.listener = listener;
        list.setLayoutOrientation(JList.VERTICAL);
        if (classDataList.size() == 1) {
            list.setVisible(false);
        } else {
            list.setVisible(true);
        }
        list.setListData(classDataList.toArray());
        list.setSelectedIndex(0);

        list.setBorder(BorderFactory.createLineBorder(JBColor.BLACK, 1));

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listener != null) {
                    listener.onClassSelected(classData.get(list.getSelectedIndex()));
                }
            }
        });
    }


    public interface OnClassSelectedListener {
        void onClassSelected(ClassModel classData);
    }

}

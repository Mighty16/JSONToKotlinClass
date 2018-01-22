package com.mighty16.json.ui;

import com.intellij.openapi.ui.Messages;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.ui.ClassesListDelegate;
import com.mighty16.json.ui.FieldsTableDelegate;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

public class ModelTableDialog extends JDialog implements ClassesListDelegate.OnClassSelectedListener {

    private static final int ANNOTATIONS_NONE = 0;
    private static final int ANNOTATIONS_GSON = 1;
    private static final int ANNOTATIONS_FAST_JSON = 2;
    private static final int ANNOTATIONS_MOSHI = 3;
    private static final int ANNOTATIONS_JACKSON = 4;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable fieldsTable;
    private JRadioButton annotationsNoneButton;
    private JRadioButton annotationsGsonButton;
    private JCheckBox singleFileCheckbox;
    private JTextField singleFileNameEdit;
    private JRadioButton annotationsFastJson;
    private JRadioButton annotationsMoshi;
    private JRadioButton annotationsJackson;
    private JTable table1;

    private FieldsTableDelegate fieldsTableDelegate;
    private ClassesListDelegate classesListDelegate;

    private List<ClassModel> data;

    private ModelTableCallbacks callbacks;

    public ModelTableDialog(List<ClassModel> data, LanguageResolver resolver, ModelTableCallbacks callbacks) {
        init();
        this.data = data;
        this.callbacks = callbacks;

        HashMap<String,String> classNames = new HashMap<>();

        for (ClassModel classModel:data){
            classNames.put(classModel.name,classModel.name);
        }

        classesListDelegate = new ClassesListDelegate(table1, data,classNames,this);
        fieldsTableDelegate = new FieldsTableDelegate(fieldsTable, resolver);
        fieldsTableDelegate.setFieldsData(data.get(0).fields);
    }

    private void init() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Class fields settings");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        singleFileCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                singleFileNameEdit.setEnabled(singleFileCheckbox.isSelected());
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onOK() {
        if (callbacks != null) {
            int annotationsType = ANNOTATIONS_NONE;
            if (annotationsGsonButton.isSelected()) {
                annotationsType = ANNOTATIONS_GSON;
            } else if (annotationsFastJson.isSelected()) {
                annotationsType = ANNOTATIONS_FAST_JSON;
            } else if (annotationsMoshi.isSelected()) {
                annotationsType = ANNOTATIONS_MOSHI;
            } else if (annotationsJackson.isSelected()) {
                annotationsType = ANNOTATIONS_JACKSON;
            }

            String singleFileName = null;
            if (singleFileCheckbox.isSelected()) {
                singleFileName = singleFileNameEdit.getText();
                if (singleFileName.length() == 0) {
                    Messages.showErrorDialog("File name is empty!", "Error");
                    return;
                }
            }
            dispose();
            callbacks.onModelsReady(data, singleFileName, annotationsType);
        }
    }

    @Override
    public void onClassSelected(ClassModel classData) {
        fieldsTableDelegate.setFieldsData(classData.fields);
    }

    public interface ModelTableCallbacks {
        void onModelsReady(List<ClassModel> data, String singleFileName, int annotationsType);
    }

}

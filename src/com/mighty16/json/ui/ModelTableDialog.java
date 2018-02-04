package com.mighty16.json.ui;

import com.intellij.openapi.ui.Messages;
import com.mighty16.json.core.models.ClassModel;
import com.mighty16.json.core.models.FieldModel;
import com.mighty16.json.core.LanguageResolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
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
    private JLabel claasesListLabel;

    private FieldsTableDelegate fieldsTableDelegate;
    private ClassesListDelegate classesListDelegate;

    private List<ClassModel> data;

    private ModelTableCallbacks callbacks;

    private int currentSelectedClassIndex = 0;

    private HashMap<String, String> classNames;

    private TextResources textResources;

    public ModelTableDialog(List<ClassModel> data, LanguageResolver resolver,
                            TextResources textResources, ModelTableCallbacks callbacks) {
        this.data = data;
        this.callbacks = callbacks;
        this.textResources = textResources;
        init();

        classNames = new HashMap<>();

        for (ClassModel classModel : data) {
            classNames.put(classModel.name, classModel.name);
        }

        classesListDelegate = new ClassesListDelegate(table1, data, classNames, this);
        fieldsTableDelegate = new FieldsTableDelegate(fieldsTable, resolver, textResources);
        fieldsTableDelegate.setClass(data.get(0));
        claasesListLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void init() {
        setContentPane(contentPane);
        setModal(true);
        setTitle(textResources.getFieldsDialogTitle());
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> dispose());
        singleFileCheckbox.addItemListener(e -> singleFileNameEdit.setEnabled(singleFileCheckbox.isSelected()));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
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
                    Messages.showErrorDialog(textResources.getEmptyFileNameMessage(),
                            textResources.getEmptyFileNameTitle());
                    return;
                }
            }

            for (ClassModel classModel : data) {
                String className = classNames.get(classModel.name);
                if (className != null) {
                    classModel.name = className;
                }
                Iterator<FieldModel> iterator = classModel.fields.iterator();
                while (iterator.hasNext()) {
                    FieldModel field = iterator.next();
                    if (!field.enabled) {
                        iterator.remove();
                    } else {
                        String fieldClassName = classNames.get(field.type);
                        if (fieldClassName != null) {
                            field.type = fieldClassName;
                        }
                    }
                }
            }
            callbacks.onModelsReady(data, singleFileName, annotationsType);
            dispose();
        }
    }

    @Override
    public void onClassSelected(ClassModel classData, int index) {
        data.get(currentSelectedClassIndex).fields = fieldsTableDelegate.getFieldsData();
        currentSelectedClassIndex = index;
        fieldsTableDelegate.setClass(classData);
    }

    public interface ModelTableCallbacks {
        void onModelsReady(List<ClassModel> data, String singleFileName, int annotationsType);
    }
}

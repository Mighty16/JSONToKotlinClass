package com.mighty16.json;

import com.mighty16.json.models.ClassModel;
import com.mighty16.json.ui.ClassesListDelegate;
import com.mighty16.json.ui.FieldsTableDelegate;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ModelTableDialog extends JDialog implements ClassesListDelegate.OnClassSelectedListener {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable fieldsTable;
    private JList list1;
    private JRadioButton annotationsNoneButton;
    private JRadioButton annotationsGsonButton;

    private FieldsTableDelegate fieldsTableDelegate;
    private ClassesListDelegate classesListDelegate;

    private List<ClassModel> data;

    private ModelTableCallbacks callbacks;

    public ModelTableDialog(List<ClassModel> data, TypesResolver resolver, ModelTableCallbacks callbacks) {
        init();
        this.data = data;
        this.callbacks = callbacks;
        classesListDelegate = new ClassesListDelegate(list1, this, data);
        fieldsTableDelegate = new FieldsTableDelegate(fieldsTable,resolver);
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
        dispose();
        if (callbacks!=null){
            int annotationsType = 0;
            if (annotationsGsonButton.isSelected()){
                annotationsType = 1;
            }
            callbacks.onModelsReady(data,annotationsType);
        }
    }


    @Override
    public void onClassSelected(ClassModel classData) {
        fieldsTableDelegate.setFieldsData(classData.fields);
    }

    public interface ModelTableCallbacks {
        void onModelsReady(List<ClassModel> data, int annotationsType);
    }

}

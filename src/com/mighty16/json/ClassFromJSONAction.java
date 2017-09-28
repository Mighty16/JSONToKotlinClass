package com.mighty16.json;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.annotations.GsonAnnotations;
import com.mighty16.json.kotlin.KotlinFilesGenerator;
import com.mighty16.json.kotlin.KotlinTypesResolver;
import com.mighty16.json.models.ClassModel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ClassFromJSONAction extends AnAction implements JSONEditDialog.JSONEditCallbacks, ModelTableDialog.ModelTableCallbacks {

    private PsiDirectory directory;
    private Point lastDialogLocation;
    private TypesResolver typesResolver;

    public ClassFromJSONAction() {
        super("JSON Kotlin Models");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {

        typesResolver = new KotlinTypesResolver();

        Project project = event.getProject();
        if (project == null) return;
        DataContext dataContext = event.getDataContext();
        final Module module = DataKeys.MODULE.getData(dataContext);
        if (module == null) return;
        final Navigatable navigatable = DataKeys.NAVIGATABLE.getData(dataContext);

        if (navigatable != null) {
            System.out.println("Navigatable: " + navigatable);
            if (navigatable instanceof PsiDirectory) {
                directory = (PsiDirectory) navigatable;
                //System.out.println("Directory: " + directory.getName());
                //packages = new ArrayList<>();
                //findPackages(directory);
                //Collections.sort(packages);
            }
        }

        if (directory == null) {
            ModuleRootManager root = ModuleRootManager.getInstance(module);
            for (VirtualFile file : root.getSourceRoots()) {
                directory = PsiManager.getInstance(project).findDirectory(file);
                System.out.println("Directory: " + directory.getName());
            }
        }

        JSONEditDialog dialog = new JSONEditDialog(this);
        dialog.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                lastDialogLocation = dialog.getLocation();
            }
        });
        dialog.setSize(640, 480);
        dialog.setVisible(true);
    }

    @Override
    public void onJsonParsed(List<ClassModel> classDataList) {
        ModelTableDialog tableDialog = new ModelTableDialog(classDataList,typesResolver, this);
        if (lastDialogLocation != null) {
            tableDialog.setLocation(lastDialogLocation);
        }
        tableDialog.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                lastDialogLocation = tableDialog.getLocation();
            }
        });

        tableDialog.pack();
        tableDialog.setVisible(true);
    }

    @Override
    public void onModelsReady(List<ClassModel> data, int annotationsType) {
        AnnotationGenerator annotations = null;
        switch (annotationsType) {
            case 1:
                annotations = new GsonAnnotations();
                break;
        }
        SourceFilesGenerator generator = new KotlinFilesGenerator(typesResolver, annotations);
        generator.generateFiles(directory, data);
    }
}

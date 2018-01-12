package com.mighty16.json;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.mighty16.json.annotations.*;
import com.mighty16.json.generator.SingleFileGenerator;
import com.mighty16.json.generator.SourceFilesGenerator;
import com.mighty16.json.generator.MultipleFilesGenerator;
import com.mighty16.json.resolver.KotlinResolver;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.ui.JSONEditDialog;
import com.mighty16.json.ui.ModelTableDialog;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ClassFromJSONAction extends AnAction implements JSONEditDialog.JSONEditCallbacks, ModelTableDialog.ModelTableCallbacks {

    private PsiDirectory directory;
    private Point lastDialogLocation;
    private LanguageResolver languageResolver;

    private DataContext dataContext;

    public ClassFromJSONAction() {
        super("JSON Kotlin Models");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {

        languageResolver = new KotlinResolver();

        Project project = event.getProject();
        if (project == null) return;
        dataContext = event.getDataContext();
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
        ModelTableDialog tableDialog = new ModelTableDialog(classDataList, languageResolver, this);
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
    public void onModelsReady(List<ClassModel> data, String singleFileName, int annotationsType) {
        AnnotationGenerator annotations = null;
        switch (annotationsType) {
            case 1:
                annotations = new GsonAnnotations();
                break;
            case 2:
                annotations = new FastJsonAnnotation();
                break;
            case 3:
                annotations = new MoshiAnnotations();
                break;
            case 4:
                annotations = new JacksonAnnotations();
                break;
        }
        SourceFilesGenerator generator;
        if (singleFileName == null) {
            generator = new MultipleFilesGenerator(languageResolver, annotations);
        } else {
            generator = new SingleFileGenerator(languageResolver, annotations, singleFileName);
        }

        generator.setListener(new SourceFilesGenerator.Listener() {
            @Override
            public void onFilesGenerated(int filesCount) {

                String message = filesCount + " data "+ ((filesCount==1)?"class":"classes")+ " generated from JSON";

                final NotificationGroup GROUP_DISPLAY_ID_INFO =
                        new NotificationGroup("JSON to data class",
                                NotificationDisplayType.BALLOON, true);

                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Notification notification = GROUP_DISPLAY_ID_INFO.createNotification(message, NotificationType.INFORMATION);
                        Notifications.Bus.notify(notification, directory.getProject());
                    }
                });
            }
        });

        generator.generateFiles(directory, data);
    }
}

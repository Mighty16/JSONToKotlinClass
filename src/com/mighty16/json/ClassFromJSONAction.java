package com.mighty16.json;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.util.ui.UIUtil;
import com.mighty16.json.annotations.*;
import com.mighty16.json.core.AnnotationGenerator;
import com.mighty16.json.core.FileSaver;
import com.mighty16.json.generator.SingleFileGenerator;
import com.mighty16.json.core.SourceFilesGenerator;
import com.mighty16.json.generator.MultipleFilesGenerator;
import com.mighty16.json.resolver.KotlinFileType;
import com.mighty16.json.resolver.KotlinResolver;
import com.mighty16.json.core.models.ClassModel;
import com.mighty16.json.core.LanguageResolver;
import com.mighty16.json.ui.JSONEditDialog;
import com.mighty16.json.ui.ModelTableDialog;
import com.mighty16.json.ui.NotificationsHelper;
import com.mighty16.json.ui.TextResources;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ClassFromJSONAction extends AnAction implements JSONEditDialog.JSONEditCallbacks, ModelTableDialog.ModelTableCallbacks {

    private PsiDirectory directory;
    private Point lastDialogLocation;
    private LanguageResolver languageResolver;
    private TextResources textResources;

    public ClassFromJSONAction() {
        super();
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        languageResolver = new KotlinResolver();
        textResources = new TextResources();

        Project project = event.getProject();
        if (project == null) return;
        DataContext dataContext = event.getDataContext();
        final Module module = DataKeys.MODULE.getData(dataContext);
        if (module == null) return;
        final Navigatable navigatable = DataKeys.NAVIGATABLE.getData(dataContext);

        if (navigatable != null) {
            if (navigatable instanceof PsiDirectory) {
                directory = (PsiDirectory) navigatable;
            }
        }

        if (directory == null) {
            ModuleRootManager root = ModuleRootManager.getInstance(module);
            for (VirtualFile file : root.getSourceRoots()) {
                directory = PsiManager.getInstance(project).findDirectory(file);
            }
        }

        JSONEditDialog dialog = new JSONEditDialog(this, textResources);
        dialog.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                lastDialogLocation = dialog.getLocation();
            }
        });
        dialog.setSize(640, 480);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    public void onJsonParsed(List<ClassModel> classDataList) {
        ModelTableDialog tableDialog = new ModelTableDialog(classDataList, languageResolver, textResources, this);
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

        Project project = directory.getProject();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiDirectoryFactory directoryFactory = PsiDirectoryFactory.getInstance(directory.getProject());
        String packageName = directoryFactory.getQualifiedName(directory, true);

        FileSaver fileSaver = new IDEFileSaver(factory, directory, KotlinFileType.INSTANCE);

        fileSaver.setListener(fileName -> {
            int ok = Messages.showOkCancelDialog(
                    textResources.getReplaceDialogMessage(fileName),
                    textResources.getReplaceDialogTitle(),
                    UIUtil.getQuestionIcon());
            return ok == 0;
        });

        SourceFilesGenerator generator;
        if (singleFileName == null) {
            generator = new MultipleFilesGenerator(fileSaver, languageResolver, annotations);
        } else {
            generator = new SingleFileGenerator(singleFileName, languageResolver, annotations, fileSaver);
        }

        generator.setListener(filesCount ->
                NotificationsHelper.showNotification(directory.getProject(),
                        textResources.getGeneratedFilesMessage(filesCount))
        );

        generator.generateFiles(packageName, data);
    }
}

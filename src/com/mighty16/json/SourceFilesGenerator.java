package com.mighty16.json;


import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.models.ClassModel;
import java.util.List;
import java.util.StringTokenizer;

public abstract class SourceFilesGenerator {

    protected TypesResolver resolver;
    protected AnnotationGenerator annotations;

    public SourceFilesGenerator(TypesResolver resolver, AnnotationGenerator annotations) {
        this.resolver = resolver;
        this.annotations = annotations;
    }

    public abstract void generateFiles(PsiDirectory directory, List<ClassModel> classDataList);

    public abstract String generateFileContentForClass(ClassModel classData);

    public abstract String getFileName(String className);


    public PsiDirectory createDirectory(PsiDirectory parent, String name) throws IncorrectOperationException {
        PsiDirectory result = null;
        for (PsiDirectory dir : parent.getSubdirectories()) {
            if (dir.getName().equalsIgnoreCase(name)) {
                result = dir;
                break;
            }
        }
        if (null == result) {
            result = parent.createSubdirectory(name);
        }
        return result;
    }

    public PsiDirectory createPackage(PsiDirectory sourceDir, String qualifiedPackage) throws IncorrectOperationException {
        PsiDirectory parent = sourceDir;
        StringTokenizer token = new StringTokenizer(qualifiedPackage, ".");
        while (token.hasMoreTokens()) {
            String dirName = token.nextToken();
            parent = createDirectory(parent, dirName);
        }
        return parent;
    }
}

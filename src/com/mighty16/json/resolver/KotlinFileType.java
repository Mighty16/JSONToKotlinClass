package com.mighty16.json.resolver;


import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class KotlinFileType implements FileType {

    public static final KotlinFileType INSTANCE = new KotlinFileType();

    @NotNull
    @Override
    public String getName() {
        return "Kotlin file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Kotlin source file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return ".kt";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile virtualFile, @NotNull byte[] bytes) {
        return null;
    }

}

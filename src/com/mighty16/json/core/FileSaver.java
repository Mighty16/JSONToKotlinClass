package com.mighty16.json.core;


public abstract class FileSaver {

    protected Listener listener;

    public abstract void saveFile(String fileName, String fileContent);

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        boolean shouldOverwriteFile(String fileName);
    }

}

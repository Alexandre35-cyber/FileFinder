package org.maison.filefinder.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RealOpener {

    private String explorer;
    private String editorPath;
    private String[] extensions;

    public RealOpener(String explorer, String editorPath, String[] ext){
        this.explorer = explorer;
        this.editorPath = editorPath;
        this.extensions = ext;
    }

    public void open(String fileToOpen)  throws Exception{
        String ext = fileToOpen.substring(fileToOpen.lastIndexOf('.')+1);
        boolean forbiddenExt = false;

        if (!isExtensionsNull()) {
            for (int i = 0; i < this.extensions.length; i++) {
                if (this.extensions[i].equals(ext)) {
                    forbiddenExt = true;
                    break;
                }
            }
        }

        if (forbiddenExt){
            System.out.println("File:" + fileToOpen + "Explorer Path:" + this.explorer);
            File file = new File(fileToOpen);
            String directory = file.getParent();
            if (directory!=null) {
                Runtime.getRuntime().exec(new String[]{this.explorer, directory});
            }
        } else {
            if (this.editorPath != null) {
                System.out.println("File:" + fileToOpen + "Editor Path:" + this.editorPath);
                Runtime.getRuntime().exec(new String[]{this.editorPath, fileToOpen});
            } else {
                System.err.println("Pas d'editeur configuré.");
            }
        }
    }
    public boolean isExtensionsNull(){
        return this.extensions == null;
    }

}

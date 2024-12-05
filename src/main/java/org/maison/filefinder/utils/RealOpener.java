package org.maison.filefinder.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealOpener {

    private String explorer;
    private String editorPath;
    private String[] extensions;
    private static Logger LOGGER = LoggerFactory.getLogger(RealOpener.class.getName());
    
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
        	LOGGER.debug("File:" + fileToOpen + "Explorer Path:" + this.explorer);
            File file = new File(fileToOpen);
            String directory = file.getParent();
            if (directory!=null) {
                Runtime.getRuntime().exec(new String[]{this.explorer, directory});
            }
        } else {
            if (this.editorPath != null) {
            	LOGGER.debug("File:" + fileToOpen + "Editor Path:" + this.editorPath);
                Runtime.getRuntime().exec(new String[]{this.editorPath, fileToOpen});
            } else {
            	LOGGER.debug("Pas d'editeur configuré.");
            }
        }
    }
    public boolean isExtensionsNull(){
        return this.extensions == null;
    }

}

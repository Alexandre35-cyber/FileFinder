package org.maison.filefinder.model;

import java.io.File;
import java.util.List;

import org.maison.filefinder.model.criteria.FileCriteriaVisitor;
import org.maison.filefinder.model.criteria.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileFilterWithException implements FileSearchService.FileExtensionFilter {
	private static Logger LOGGER = LoggerFactory.getLogger(FileFilterWithException.class.getName());
    private FileCriteriaVisitor vCriteria;
    boolean acceptAllFiles;
    private List<SearchCriteria> activeCriteria;
    private String ext;
    boolean shown = false;

    public FileFilterWithException( FileCriteriaVisitor v,
                                    boolean acceptAllFiles,
                                    List<SearchCriteria> activeCriteria,
                                    String ext ){
        this.vCriteria = v;
        this.acceptAllFiles = acceptAllFiles;
        this.activeCriteria = activeCriteria;
        this.ext = ext;
  
    }

    @Override
    public boolean accept(File currentFile) {
        if ("*".equals(this.ext)) return true;
        boolean fileAccepted = !currentFile.isDirectory() & ( currentFile.getName().endsWith(ext)|| "*".equals(this.ext));
        /*if (!this.acceptAllFiles) {
            fileAccepted &= currentFile.getName().endsWith(ext);
        }*/
        if (fileAccepted) {
            vCriteria.setCurrentFile(currentFile);
            vCriteria.visitStarted();

            for (SearchCriteria criteria : activeCriteria) {
            	LOGGER.info("Calcul Application de " + criteria.getName());	
                try {
                    criteria.accept(vCriteria);
                } catch (Exception e) {
                    if (!this.shown) {
                        //JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur type de fichier", JOptionPane.ERROR_MESSAGE);
                    	LOGGER.info("BINAIRE currentFile " + currentFile.getAbsolutePath());
                        this.shown = true;
                    }
                }
            }
            vCriteria.visitEnded();
            return vCriteria.isFileAccepted();
        }
        return false;
    }
}

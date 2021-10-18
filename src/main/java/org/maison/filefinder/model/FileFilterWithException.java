package org.maison.filefinder.model;

import org.maison.filefinder.model.criteria.FileCriteriaVisitor;
import org.maison.filefinder.model.criteria.SearchCriteria;

import java.io.File;
import java.util.List;

public class FileFilterWithException implements FileSearchService.FileExtensionFilter {

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
        boolean fileAccepted = !currentFile.isDirectory() & currentFile.getName().endsWith(ext);
        /*if (!this.acceptAllFiles) {
            fileAccepted &= currentFile.getName().endsWith(ext);
        }*/
        if (fileAccepted) {
            vCriteria.setCurrentFile(currentFile);
            vCriteria.visitStarted();

            for (SearchCriteria criteria : activeCriteria) {
                System.out.println("Calcul Application de " + criteria.getName());
                try {
                    criteria.accept(vCriteria);
                } catch (Exception e) {
                    if (!this.shown) {
                        //JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur type de fichier", JOptionPane.ERROR_MESSAGE);
                        System.out.println("BINAIRE currentFile " + currentFile.getAbsolutePath());
                        this.shown = true;
                    }
                }
            }
            System.out.println(currentFile.getAbsolutePath() + " " + vCriteria.isFileAccepted());
            vCriteria.visitEnded();
            return vCriteria.isFileAccepted();
        }
        return false;
    }
}

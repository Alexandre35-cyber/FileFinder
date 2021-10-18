package org.maison.filefinder.model;

import org.maison.filefinder.model.criteria.DuplicateSearchCriteria;
import org.maison.filefinder.model.criteria.FileCriteriaVisitor;
import org.maison.filefinder.model.criteria.SearchCriteria;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe qui effectue les recherches de fichiers avec les critères ajoutés.
 */
public class FileSearchService implements SearchEngine {

    //Criteres de recherche
    private List<SearchCriteria> criterias = new ArrayList<>();
    // Ecouteur de recherche
    private List<SearchListener> listeners = new ArrayList<>();
    //Repertoire choisi
    private String directory;
    //Extension de fichiers à filtrer
    private String filter;
    //Interface de filtrage de fichiers
    private FileExtensionFilter extensionFilter;

    private boolean stopSearch = false;
    //Enum pour unité de calcul
    public enum UNIT {KOS, MOS, GOS}

    //Unité choisie
    private UNIT unit;

    //Interface de filtrage de fichiers
    interface FileExtensionFilter {
        boolean accept(File file);
    }

    /**
     * Ajoute les criteres dans une liste, ils ne sont pas encore affiches/modifies
     *
     * @param criterium
     */
    public void addCriteria(SearchCriteria criterium) {
        criterias.add(criterium);
    }

    public void checkCriteria() throws FileFinderException{

        for (SearchCriteria searchCriteria:criterias){
            if (searchCriteria.isActive()){
                return;
            }
        }
        throw new FileFinderException("Aucun critère de recherche n'a été saisi.");
    }

    /**
     * Ajoute un ecouteur de recherche
     *
     * @param listener
     */
    public void addResultsListener(SearchListener listener) {
        listeners.add(listener);
    }

    /**
     * Setter pour le repertoire de recherche
     *
     * @param directory
     */
    public void setSearchDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Positionne le filtre de recherche pour les extensions de fichiers
     *
     * @param filter
     */
    public void setFileExtensionFilter(String filter) {
        this.filter = filter;
        boolean acceptAllFiles = filter.equals("*.*");
        String ext = filter.substring(2);
        FileCriteriaVisitor v = new FileCriteriaVisitor();

        List<SearchCriteria> activeCriteria = new ArrayList<>();
        for (SearchCriteria criteria : criterias) {
            if (criteria.isActive()) {
                activeCriteria.add(criteria);
            }
        }
        this.extensionFilter = new FileFilterWithException(v, acceptAllFiles, activeCriteria, ext);
    }

    /**
     * Positionne le filtre de recherche pour les fichiers
     *
     * @param filterOnFilename
     */
    public void setFileFilter(String filterOnFilename) {
        this.filter = filterOnFilename;
        List<SearchCriteria> activeCriteria = new ArrayList<>();
        for (SearchCriteria criteria : criterias) {
            if (criteria.isActive()) {
                activeCriteria.add(criteria);
            }
        }
        this.extensionFilter = currentFile -> {
            if (!currentFile.isDirectory() && currentFile.getName().equals(filter)) {
                FileCriteriaVisitor v = new FileCriteriaVisitor();
                v.setCurrentFile(currentFile);
                boolean shown = false;
                for (SearchCriteria criteria : activeCriteria) {
                    try {
                    criteria.accept(v);
                    } catch (Exception e){
                        if (!shown) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur type de fichier", JOptionPane.ERROR_MESSAGE);
                            shown = true;
                        }
                    }
                }
                v.visitEnded();
                return v.isFileAccepted();
            }
            return false;
        };
    }

    /**
     * Lance la recherche a partir d une extension de fichiers
     *
     * @param directory
     * @param extension
     * @throws FileFinderException
     */
    public void searchWithExtension(String directory, String extension) throws FileFinderException {
        setSearchDirectory(directory);
        setFileExtensionFilter(extension);
        find();
    }

    /**
     * Lance la recherche a partir d un fichier
     * @param directory
     * @param filename
     * @throws FileFinderException
     */

    public void searchWithFile(String directory, String filename) throws FileFinderException {
        setSearchDirectory(directory);
        setFileFilter(filename);
        find();
    }

    public void reset(){
        notifySearchStarted();
    }

    /**
     * Retourne les criteres
     * @return List&lt;SearchCriteria&gt;
     */

    @Override
    public List<SearchCriteria> getCriterias() {
        return criterias;
    }

    /**
     * Lance la recherche
     * @throws FileFinderException
     */

    public void find() throws FileFinderException {

        if ("".equals(this.directory) && this.directory == null) {
            throw new FileFinderException("Le repertoire de recherche n'est pas valorisé.");
        }
        File root = new File(this.directory);
        if (!root.exists() || !root.canRead() || !root.isDirectory()) {
            throw new FileFinderException("Le repertoire de recherche n'existe pas ou n est pas lisible.");
        }

        if ("".equals(this.filter) || this.filter == null) {
            throw new FileFinderException("L'extension de fichier n'est pas valorisée.");
        }
        if (this.filter.contains("*")) {
            if (!this.filter.matches("\\*\\.(([A-Za-z0-9]+)|([\\.\\*]))")) {
                throw new FileFinderException("L'extension de fichier '" + this.filter + "' est incorrecte.");
            }
        }
        reset();
        explore();
    }

    /**
     * Lance l'exploration  du disque
     */
    private void explore() {
        File root = new File(this.directory);
        exploreSubDirectory(root);
        notifySearchEnded();
    }

    /**
     * Lance l'exploration d'un reprtoire recursivement
     * @param root
     */
    private  void exploreSubDirectory(File root) {
        if (!stopSearch){
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files== null || files.length == 0) return;
            for (File f : files) {
                exploreSubDirectory(f);
            }
        } else {
            //Lance les criteres de recherche
            if (this.extensionFilter.accept(root)) {
                notifyFileFound(root);
            }
        }
        }
    }

    public void stopSearch(){
        stopSearch = true;
    }

    public void notifySearchStarted() {
        for (SearchListener listener : listeners) {
            listener.reset();
        }
        for (SearchListener listener : listeners) {
            listener.searchStarted();
        }
    }

    public void notifySearchEnded() {
        for (SearchListener listener : listeners) {
            listener.searchEnded();
        }
    }

    public void notifyFileFound(File root) {
        //DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(root.)
        String dateCreation ="";
        try {
            FileTime creationTime = (FileTime) Files.getAttribute(Path.of(root.getAbsolutePath()), "creationTime");
            dateCreation = creationTime.toString();
        } catch (IOException ex) {
            // handle exception
        }

        for (SearchListener listener : listeners) {
            listener.addResult(root.getAbsolutePath(), root.length(), dateCreation);
        }
    }

    public static void main(String[] args) {
        FileSearchService finder = new FileSearchService();
        finder.setFileExtensionFilter("*.*");
        finder.setSearchDirectory("C:\\Tmp");
/*
        finder.addCriteria(new SizeSearchCriteria(0, 10, UNIT.KOS));

        LocalDate dateMax = LocalDate.now();
        LocalDate dateMin = dateMax.minusMonths(2);
        finder.addCriteria(new DateSearchCriteria(dateMin, dateMax));

        finder.addCriteria(new PatternSearchCriteria());
*/
        DuplicateSearchCriteria criteria = new DuplicateSearchCriteria();
        finder.addCriteria(criteria);
        criteria.directorySelected("C:\\Tmp");
        criteria.setActive(true);

        System.out.println(criteria.getDuplicates());

        finder.addResultsListener(new SearchListener() {
            @Override
            public void searchStarted() {
                System.out.println("FileFinder::START");
            }

            @Override
            public void searchEnded() {
                System.out.println("FileFinder::STOP");
            }

            @Override
            public void addResult(String results, long size, String date) {
                date = date.replaceAll("T"," ");
                System.out.println("°°°°°°°°°°°°°°°°°°°°°°°FileFinder::Resultat:" + results + " " + date.substring(0,date.indexOf('.')));
            }

            @Override
            public void reset() {
                System.out.println("FileFinder::RESET ");
            }
        });

        try {
            finder.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

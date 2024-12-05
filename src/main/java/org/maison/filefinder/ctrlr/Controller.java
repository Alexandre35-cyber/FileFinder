package org.maison.filefinder.ctrlr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.maison.filefinder.model.FileFinderException;
import org.maison.filefinder.model.FileOpener;
import org.maison.filefinder.model.FileSearchService;
import org.maison.filefinder.model.SearchListener;
import org.maison.filefinder.utils.RealOpener;
import org.maison.filefinder.view.MainWindow;


/**
 * Controleur de l application.
 * Responsable de lancer les recherches.
 */
public class Controller implements FileOpener {

    private FileSearchService finder;
    private MainWindow window;
    private String configFile;
    private RealOpener realOpener;

    /**
    * Pour les tests unitaires remplace le fichier de configuration
    * @param configFile String Chemin absolu vers le fichier de configuration
     */
    public void setConfigForTests(String configFile) {
    	this.configFile = configFile;
    }

    /**
     * Methode de lancement de la recherche à partir d'une extension de fichier
     * @param directory
     * @param extension
     * @throws FileFinderException
     */
    public void searchWithExtension(String directory, String extension) throws FileFinderException{
    	finder.searchWithExtension(directory, extension);
    }

    /**
     * Methode de lancement de la recherche à partir d'un nom de fichier
     * @param directory
     * @param filename
     * @throws FileFinderException
     */
    public void searchWithFile(String directory, String filename) throws FileFinderException{
    	finder.searchWithFile(directory, filename);
    }

    /**
     * Ajoute un observeur de recherche
     * @param sl {@link SearchListener}
     */
    public void addResultListener(SearchListener sl) {
    	finder.addResultsListener(sl);
    }

    /**
     * Constructeur
     * @throws Exception
     */
    public Controller() throws Exception{
        finder = new FileSearchService();

        SearchConfiguration.apply(finder);

        window = new MainWindow(finder, this);

        finder.addResultsListener(window);
    }

    /**
     * Verifie si le fichier de configuration est bien rempli
     * @throws Exception
     */
    public void checkEditor() throws Exception  {
    	InputStream stream;    	
    	if (this.configFile!=null) {
    		stream = new FileInputStream(new File(this.configFile));
    	}else {
        stream = getClass().getClassLoader().getResourceAsStream("config.properties");
    	}
        Properties p = new Properties();
        p.load(stream);
        String path = (String) p.get("editor.executable");
        if (path == null) {
            throw new Exception("No editor configured.");
        }
        File editor = new File(path);
        if (!editor.exists()) {
            throw new Exception("Editor '"+path+"' not found.");
        }
        String explorer = (String) p.get("explorer.path");
        if (explorer == null) {
            throw new Exception("No explorer configured.");
        }
        String extensions = (String) p.get("extensions.forbidden");
        if (extensions != null) {
            this.realOpener = new RealOpener(explorer, path, extensions.split(","));
        }
        else{
            throw new Exception("No extensions.forbidden field configured.");
        }
    }

    /**
     * Affiche la fenetre
     */
    public void start(){
        this.window.display();
    }


    /**
     * Demande l'ouverture d'un fichier
     * @param fileToOpen Fichier uvrir
     * @throws Exception
     */
    public void open(URL fileToOpen) throws Exception {
        this.realOpener.open(fileToOpen.getFile().substring(1));
    }

    /**
     * Méthode principale de l application
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
         Controller ctrl = new Controller();
            ctrl.start();
        try {
            ctrl.checkEditor();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

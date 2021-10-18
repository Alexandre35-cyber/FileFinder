package org.maison.filefinder.ctrlr;

import org.maison.filefinder.model.*;
import org.maison.filefinder.model.criteria.DateSearchCriteria;
import org.maison.filefinder.model.criteria.DuplicateSearchCriteria;
import org.maison.filefinder.model.criteria.PatternSearchCriteria;
import org.maison.filefinder.model.criteria.SizeSearchCriteria;
import org.maison.filefinder.utils.RealOpener;
import org.maison.filefinder.view.MainWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Controller implements FileOpener {

    private FileSearchService finder;
    private MainWindow window;
    private String editorPath = null;
    private String explorerPath = null;
    private String configFile;
    private RealOpener realOpener;


    public void setConfigForTests(String configFile) {
    	this.configFile = configFile;
    }
    
    public void searchWithExtension(String directory, String extension) throws FileFinderException{
    	finder.searchWithExtension(directory, extension);
    }
    
    public void searchWithFile(String directory, String filename) throws FileFinderException{
    	finder.searchWithFile(directory, filename);
    }
       
    
    public void addResultListener(SearchListener sl) {
    	finder.addResultsListener(sl);
    }
    
    public Controller() throws Exception{
        finder = new FileSearchService();
        finder.addCriteria(new SizeSearchCriteria(-1,-1, FileSearchService.UNIT.KOS));

        finder.addCriteria(new DateSearchCriteria(null, null));

        finder.addCriteria(new PatternSearchCriteria());

        DuplicateSearchCriteria duplicateSearchCriteria = new DuplicateSearchCriteria();

        finder.addCriteria(duplicateSearchCriteria);

        window = new MainWindow(finder, this);

        finder.addResultsListener(window);
        finder.addResultsListener(new SearchListener() {
            @Override
            public void searchStarted() {
                System.out.println("ControllerStart...");
            }

            @Override
            public void searchEnded() {
                System.out.println("ControllersearchEnded Stop.");
            }

            @Override
            public void addResult(String results, long size, String date) {
                System.out.println("Fichier:" + results + " Taille:" + size + " Date:" + date);
            }

            @Override
            public void reset() {
                System.out.println("ControllerReset.");
            }
        });
    }

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
        String[] ext = extensions.split(",");
        this.realOpener = new RealOpener(explorer, path , ext);
    }

    public void start(){
        this.window.display();
    }


    public void open(String fileToOpen) throws Exception {
        this.realOpener.open(fileToOpen);

        if (this.editorPath !=null){
        	System.out.println("File:"+ fileToOpen + "Editor Path:" + this.editorPath);
            Runtime.getRuntime().exec(new String[]{this.editorPath, fileToOpen});
        }else{
            System.err.println("Pas d'editeur configuré.");
        }
    }

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

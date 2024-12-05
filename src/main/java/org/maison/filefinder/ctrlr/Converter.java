package org.maison.filefinder.ctrlr;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


public class Converter {

    private String directory;
    
    public Converter(String directory){
        this.directory = directory;
    }
    
    public  void explore() {
        File root = new File(this.directory);
        exploreSubDirectory(root);
    }

    /**
     * Lance l'exploration d'un reprtoire recursivement
     * @param root
     */
    private void exploreSubDirectory(File root) {
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files == null) return;
            for (File f : files) {
                exploreSubDirectory(f);
            }
        } else {
            if (!root.getName().endsWith("jks")) {
                System.out.println("Changement de " + root.getAbsolutePath());
                String filename = root.getAbsolutePath();
                File file = new File(filename);
                try {
                    String content = FileUtils.readFileToString(file, "ISO8859_1");
                    FileUtils.write(file, content, "UTF-8");
                } catch (Exception e){
                    System.out.println("File:" + filename);
                    e.printStackTrace();
                }
            }
            }
    }


    public static void main(String[] args) throws IOException {
        Converter cvter = new Converter(args[0]);
        cvter.explore();
    }
}

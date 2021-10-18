package org.maison.filefinder.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class TestUtils {


    public static void creerFichierTexte(String nomFichier, String chaine) throws IOException {
        Writer fw = new FileWriter(nomFichier);
        fw.append(chaine);
        fw.flush();
        fw.close();
    }

}

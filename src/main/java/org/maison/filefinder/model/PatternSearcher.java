package org.maison.filefinder.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternSearcher {
	private static Logger LOGGER = LoggerFactory.getLogger(PatternSearcher.class.getName());
    private File currentFile;
    private boolean regExpMode;

    public PatternSearcher(File file) throws FileFinderException {
        this.currentFile = file;
        if (PatternSearcher.isBinary(this.currentFile)){
            throw new FileFinderException("Le fichier '"+this.currentFile+ "' est de type binaire. Pas de recherche textuelle dans les fichiers binaires.");
        }
    }

    public void setRegexpMode(boolean regExpMode){
        this.regExpMode = regExpMode;
    }


    public boolean findPattern(String pattern) {
        try {
            List<String> lines = FileUtils.readLines(this.currentFile, "UTF-8");
            if (this.regExpMode){
                for (String line: lines){
                    if (line.matches(pattern)) {
                        return true;
                    }
                }
                return false;
            } else{
                for (String line: lines){
                	LOGGER.info(">>>"+line);
                    if (line.length() == 1)
                            continue;
                    if (line.contains(pattern)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isBinary(File currentFile) {
        try {
            if (currentFile.getAbsolutePath().endsWith(".txt")) return false;
            FileInputStream in = new FileInputStream(currentFile);
            int size = in.available();
            if (size > 1024) size = 1024;
            byte[] data = new byte[size];
            in.read(data);
            in.close();

            int ascii = 0;
            int other = 0;

            for (int i = 0; i < data.length; i++) {
                byte b = data[i];
                if (b < 0x09) return true;

                if (b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D) ascii++;
                else if (b >= 0x20 && b <= 0x7E) ascii++;
                else other++;
            }

            if (other == 0) return false;

            // Plus de 95% de autres caracteres
            LOGGER.info("%bin " + 100 * other / (ascii + other));
            return 100 * other / (ascii + other) > 95;
        } catch (Exception e){
            return false;
        }
    }

}

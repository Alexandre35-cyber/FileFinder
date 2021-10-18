package org.maison.filefinder.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maison.filefinder.model.criteria.PatternSearchCriteria;

import java.io.File;

import static org.maison.filefinder.model.TestUtils.creerFichierTexte;

public class PatternSearchCriteriaTest {
    private FileSearchService service;

    @Before
    public void setUp() {
        new File("C:\\Tmp\\patterns\\").mkdirs();
        service = new FileSearchService();
    }

    @Test
    public void testPremier() throws Exception{
        creerFichierTexte("C:\\Tmp\\patterns\\fichier.txt", "UneChaineDeUneChaineDeCaracteresDeUne");
        creerFichierTexte("C:\\Tmp\\patterns\\autre.txt", "DuTexte");
        service.addCriteria(new PatternSearchCriteria(){
            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public String getPattern() {
                return "UneChaineDeCaracteres";
            }

            @Override
            public boolean isRegexpEnabled() {
                return false;
            }
        });
        TheTestListener l = new TheTestListener();

        service.addResultsListener(l);

        service.searchWithExtension("C:\\Tmp\\patterns\\", "*.txt");

        Assert.assertTrue(l.check("C:\\Tmp\\patterns\\fichier.txt"));
        Assert.assertFalse(l.check("C:\\Tmp\\patterns\\autre.txt"));
    }

}
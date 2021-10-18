package org.maison.filefinder.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maison.filefinder.model.criteria.DuplicateSearchCriteria;

import java.io.File;
import java.util.List;

import static org.maison.filefinder.model.TestUtils.creerFichierTexte;

public class DuplicateSearchCriteriaTest {

    private FileSearchService service;

    @Before
    public void setUp() {
        new File("C:\\Tmp\\dups\\").mkdirs();
        new File("C:\\Tmp\\dups\\sub").mkdirs();
        service = new FileSearchService();
    }

    @Test
    public void testPremier() throws Exception {
        UserSelectionMgr.get().setDirectorySelected("C:\\Tmp\\dups\\");
        creerFichierTexte("C:\\Tmp\\dups\\premier.txt", "UneChaine");
        creerFichierTexte("C:\\Tmp\\dups\\fichier.txt", "UneChaine");
        creerFichierTexte("C:\\Tmp\\dups\\sub\\fichier.txt", "UneAutreChaine");

        service.addCriteria(new DuplicateSearchCriteria(){
            @Override
            public boolean isActive() {
                return true;
            }
        });

        TheTestListener l = new TheTestListener();

        service.addResultsListener(l);
        service.searchWithExtension("C:\\Tmp\\dups", "*.txt");
        Assert.assertTrue(l.check("C:\\Tmp\\dups\\fichier.txt"));
        Assert.assertTrue(l.check("C:\\Tmp\\dups\\sub\\fichier.txt"));
        Assert.assertFalse(l.check("C:\\Tmp\\dups\\premier.txt"));

    }
}

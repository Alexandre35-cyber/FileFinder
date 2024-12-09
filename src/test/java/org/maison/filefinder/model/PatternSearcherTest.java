package org.maison.filefinder.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class PatternSearcherTest {

    @Before
    public void setUp() throws Exception {
        TestUtils.creerFichierTexte("C:\\Tmp\\test.txt", "blabla1234blibli\n");
        TestUtils.creerFichierTexte("C:\\Tmp\\test2.txt", "blabla\nblibli\ntoto\n");
    }

    @Test
    public void testPremier() throws Exception {
        PatternSearcher searcher = new PatternSearcher(new File("C:\\Tmp\\test.txt"));
        searcher.setRegexpMode(false);
        Assert.assertFalse(searcher.findPattern("azerty"));
        Assert.assertTrue(searcher.findPattern("1234"));
        Assert.assertTrue(searcher.findPattern("bla"));
        searcher.setRegexpMode(true);
        Assert.assertTrue(searcher.findPattern("^bla(.*)$"));
        Assert.assertTrue(searcher.findPattern("^bla([a-z]{3})([0-9]{4})(.*)$"));
        searcher.setRegexpMode(false);
        Assert.assertFalse(searcher.findPattern("^bla(.*)$"));
        Assert.assertFalse(searcher.findPattern("^bla([a-z]{3})([0-9]{4})(.*)$"));

    }

    @Test
    public void testSecond() throws Exception{
        PatternSearcher searcher = new PatternSearcher(new File("C:\\Tmp\\test2.txt"));
        searcher.setRegexpMode(false);
        Assert.assertFalse(searcher.findPattern("azerty"));
        Assert.assertTrue(searcher.findPattern("toto"));
        Assert.assertTrue(searcher.findPattern("bla"));
        searcher.setRegexpMode(true);
        Assert.assertTrue(searcher.findPattern("^(.*)toto(.*)$"));
    }

    @Test
    public void testIsBinary() throws Exception {
        String binaryFile = "C:\\\\Windows\\\\regedit.exe";
        Assert.assertTrue(PatternSearcher.isBinary(new File(binaryFile)));
        Assert.assertFalse(PatternSearcher.isBinary(new File("C:\\Tmp\\test.txt")));
    }

    @Test
    public void binaryFileTest(){
        String binaryFile = "C:\\Windows\\regedit.exe";
        try {
            PatternSearcher searcher = new PatternSearcher(new File(binaryFile));
           Assert.fail("Should fail");
        } catch (Exception e){
            Assert.assertEquals("Le fichier '"+binaryFile+"' est de type binaire. Pas de recherche textuelle dans les fichiers binaires.", e.getMessage());
        }
    }

}

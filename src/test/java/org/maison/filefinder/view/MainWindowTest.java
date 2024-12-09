package org.maison.filefinder.view;

import org.junit.Assert;
import org.junit.Test;
import org.maison.filefinder.model.FileFinderException;
import org.maison.filefinder.model.FileOpener;
import org.maison.filefinder.model.SearchEngine;
import org.maison.filefinder.model.criteria.SearchCriteria;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainWindowTest {


    @Test
    public void testPremier() throws Exception {
        String filepath = "file://C:/Windows/Cursors/aero_arrow.cur";
        MainWindow window = new MainWindow(new SearchEngine() {
            @Override
            public void reset() {

            }

            @Override
            public void searchWithExtension(String directory, String extension) throws FileFinderException {

            }

            @Override
            public void searchWithFile(String directory, String filename) throws FileFinderException {

            }

            @Override
            public List<SearchCriteria> getCriterias() {
                return Collections.emptyList();
            }

            @Override
            public void stopSearch() {

            }
        }, new FileOpener() {
            @Override
            public void open(URL fileToOpen) throws Exception {
                Assert.assertEquals(filepath,fileToOpen.toString());
            }
        });

        window.doClick(filepath);
    }

    @Test
    public void testSecond(){
        String nom = "D:\\Users\\amaison\\Documents\\Modèles Office personnalisés";
        Assert.assertTrue(nom.matches("^(.*)[ ](.*)$"));
    }

}

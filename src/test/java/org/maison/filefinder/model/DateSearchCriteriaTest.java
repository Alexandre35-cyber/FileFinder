package org.maison.filefinder.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maison.filefinder.model.criteria.DateSearchCriteria;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.maison.filefinder.model.TestUtils.creerFichierTexte;

public class DateSearchCriteriaTest {

    private FileSearchService service;

    @Before
    public void setUp() {
        new File("C:\\Tmp\\date\\").mkdirs();
        service = new FileSearchService();
    }

    @Test
    public void testPremier() throws Exception{
        creerFichierTexte("C:\\Tmp\\date\\fichier.txt", "UneChaineDeUneChaineDeCaracteresDeUne");

        File file = new File("C:\\Tmp\\date\\fichier.txt");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        // print the original Last Modified date
        System.out.println("Original Last Modified Date : " + dateFormat.format(file.lastModified()));

        // set this date
        String newLastModifiedString = "10/12/2021";

        // we have to convert the above date to milliseconds...
        Date newLastModifiedDate = dateFormat.parse(newLastModifiedString);
        file.setLastModified(newLastModifiedDate.getTime());

        service.addCriteria(new DateSearchCriteria(LocalDate.now(), LocalDate.MAX){
            @Override
            public boolean isActive() {
                return true;
            }


        });
        TheTestListener l = new TheTestListener();

        service.addResultsListener(l);

        service.searchWithExtension("C:\\Tmp\\date\\", "*.txt");

        Assert.assertFalse(l.check("C:\\Tmp\\date\\fichier.txt"));

         newLastModifiedDate =GregorianCalendar.getInstance().getTime();
        file.setLastModified(newLastModifiedDate.getTime());

        l.reset();

        service.searchWithExtension("C:\\Tmp\\date\\", "*.txt");

        Assert.assertTrue(l.check("C:\\Tmp\\date\\fichier.txt"));
    }

}

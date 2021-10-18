package org.maison.filefinder.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maison.filefinder.model.criteria.SizeSearchCriteria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SizeSearchCriteriaTest {
    private FileSearchService service;

    @Before
    public void setUp() {
        new File("C:\\Tmp\\size\\").mkdirs();
        service = new FileSearchService();
    }

    @After
    public void tearDown(){
        File files[] = new File("C:\\Tmp\\size\\").listFiles();
        for (int i=0; i<files.length; i++ ){
            files[i].delete();
        }
    }

    @Test
    public void testCreateFile() throws Exception {

        writeToPosition("C:\\Tmp\\size\\fichierDeux.txt",'a',2048);
        service.addCriteria(new SizeSearchCriteria(0,1, FileSearchService.UNIT.KOS){
            @Override
            public boolean isActive() {
                return true;
            }
        });


        TheTestListener l = new TheTestListener();
        service.addResultsListener(l);
        service.searchWithExtension("C:\\Tmp\\size\\", "*.txt");
        Assert.assertFalse(l.check("C:\\Tmp\\size\\fichierDeux.txt"));

        writeToPosition("C:\\Tmp\\size\\fichierUn.txt",'a',512);
        l.reset();
        service.searchWithExtension("C:\\Tmp\\size\\", "*.txt");
        Assert.assertTrue(l.check("C:\\Tmp\\size\\fichierUn.txt"));

    }

    private void writeToPosition(String filename, int data, long position)
            throws IOException {
        RandomAccessFile writer = new RandomAccessFile(filename, "rw");
        writer.seek(position);
        writer.writeInt(data);
        writer.close();
    }

}

package org.maison.filefinder.model;

import java.util.ArrayList;
import java.util.List;

public class TheTestListener implements SearchListener {
    private List<String> resultsList = new ArrayList<String>();

    @Override
    public void searchStarted() {

    }

    @Override
    public void searchEnded() {

    }

    @Override
    public void addResult(String results, long size, String dateCreation) {
        resultsList.add(results);
    }

    @Override
    public void reset() {
        resultsList.clear();
    }

    boolean check(String filename) {
        for (String result : resultsList) {
            if (result.equals(filename)) return true;
        }
        return false;
    }
}

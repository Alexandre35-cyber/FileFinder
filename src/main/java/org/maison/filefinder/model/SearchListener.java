package org.maison.filefinder.model;

public interface SearchListener {
    void searchStarted();
    void searchEnded();
    void addResult(String results, long size, String dateCreation);
    void reset();

}

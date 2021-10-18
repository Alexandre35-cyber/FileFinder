package org.maison.filefinder.model;

import org.maison.filefinder.model.criteria.SearchCriteria;

import java.util.List;

public interface SearchEngine {

     void reset();
     void searchWithExtension(String directory, String extension) throws FileFinderException;
     void searchWithFile(String directory, String filename) throws FileFinderException;
     List<SearchCriteria> getCriterias();
     void stopSearch();
}

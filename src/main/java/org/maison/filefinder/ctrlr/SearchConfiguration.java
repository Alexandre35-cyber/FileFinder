package org.maison.filefinder.ctrlr;

import org.maison.filefinder.model.FileSearchService;
import org.maison.filefinder.model.SearchListener;
import org.maison.filefinder.model.criteria.DateSearchCriteria;
import org.maison.filefinder.model.criteria.DuplicateSearchCriteria;
import org.maison.filefinder.model.criteria.PatternSearchCriteria;
import org.maison.filefinder.model.criteria.SizeSearchCriteria;

public class SearchConfiguration {

    public static void apply(FileSearchService service){

        service.addCriteria(new SizeSearchCriteria(-1,-1, FileSearchService.UNIT.KOS));

        service.addCriteria(new DateSearchCriteria(null, null));

        service.addCriteria(new PatternSearchCriteria());

        service.addCriteria(new DuplicateSearchCriteria());

        service.addResultsListener(new SearchListener() {
            @Override
            public void searchStarted() {
                System.out.println("ControllerStart...");
            }

            @Override
            public void searchEnded() {
                System.out.println("ControllersearchEnded Stop.");
            }

            @Override
            public void addResult(String results, long size, String date) {
                System.out.println("Fichier:" + results + " Taille:" + size + " Date:" + date);
            }

            @Override
            public void reset() {
                System.out.println("ControllerReset.");
            }
        });

    }
}

package org.maison.filefinder.ctrlr;

import org.maison.filefinder.model.FileSearchService;
import org.maison.filefinder.model.SearchListener;
import org.maison.filefinder.model.criteria.DateSearchCriteria;
import org.maison.filefinder.model.criteria.DuplicateSearchCriteria;
import org.maison.filefinder.model.criteria.PatternSearchCriteria;
import org.maison.filefinder.model.criteria.SizeSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchConfiguration {
	private static Logger LOGGER = LoggerFactory.getLogger(SearchConfiguration.class.getName());
	
    public static void apply(FileSearchService service){

        service.addCriteria(new SizeSearchCriteria(-1,-1, FileSearchService.UNIT.KOS));

        service.addCriteria(new DateSearchCriteria(null, null));

        service.addCriteria(new PatternSearchCriteria());

        service.addCriteria(new DuplicateSearchCriteria());

        service.addResultsListener(new SearchListener() {
            @Override
            public void searchStarted() {
            	LOGGER.debug("ControllerStart...");
            }

            @Override
            public void searchEnded() {
            	LOGGER.debug("ControllersearchEnded Stop.");
            }

            @Override
            public void addResult(String results, long size, String date) {
            	LOGGER.debug("Fichier:" + results + " Taille:" + size + " Date:" + date);
            }

            @Override
            public void reset() {
            	LOGGER.debug("ControllerReset.");
            }
        });

    }
}

package org.maison.filefinder.model.criteria;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

import org.maison.filefinder.model.FileFinderException;
import org.maison.filefinder.model.FileSearchService;
import org.maison.filefinder.model.PatternSearcher;
import org.maison.filefinder.model.UserSelectionMgr;
import org.maison.filefinder.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileCriteriaVisitor implements SearchCriteriaVisitor {
	private static Logger LOGGER = LoggerFactory.getLogger(FileCriteriaVisitor.class.getName());
    private File currentFile;
    private boolean accepted = true;

    private static int KO = 1024;
    private static int MO = KO * KO;
    private static int GO = 1024 * MO;

    public void setCurrentFile(File currentFile){
        this.currentFile = currentFile;
        this.accepted = true;
    }

    public boolean isFileAccepted(){
        return accepted;
    }

    @Override
    public void visitCriteria(SearchCriteria criteria) {
        //NOTHING TO DO
    }

    @Override
    public void visitSizeCriteria(SizeSearchCriteria criteria)  throws Exception{
        long minSizeInBytes = 0;
        long maxSizeInByes = 0;
        if (criteria.isActive()){
             if (criteria.getChosenUnit() == FileSearchService.UNIT.KOS){
                 minSizeInBytes = criteria.getSizeMin() * KO;
                 maxSizeInByes = criteria.getSizeMax() * KO;
             }  else {
                 if (criteria.getChosenUnit() == FileSearchService.UNIT.MOS) {
                     minSizeInBytes = criteria.getSizeMin() * MO;
                     maxSizeInByes = criteria.getSizeMax() * MO;
                 } else{
                     if (criteria.getChosenUnit() == FileSearchService.UNIT.GOS) {
                         minSizeInBytes = criteria.getSizeMin() * GO;
                         maxSizeInByes = criteria.getSizeMax() * GO;
                     }
                 }
             }
            accepted &= ((minSizeInBytes < currentFile.length()) && (maxSizeInByes > currentFile.length()));
        }
    }

    @Override
    public void visitDateCriteria(DateSearchCriteria criteria)  throws Exception {
        if (criteria.isActive()){
            LocalDate dateMinimum = criteria.getMinDate();
            LocalDate dateMaximum = criteria.getMaxDate();
            LocalDate dateFile = DateUtils.asLocalDate(new Date(currentFile.lastModified()));
            accepted &= (dateMinimum.isBefore(dateFile)  ||
                    dateMinimum.isEqual(dateFile)) && (dateMaximum.isAfter(dateFile) ||
                    dateMaximum.isEqual(dateFile));
        }
    }

    @Override
    public void visitPatternCriteria(PatternSearchCriteria criteria) throws Exception {
        if ("*.exe".equals(UserSelectionMgr.get().getSelectedExtension())){
          throw new FileFinderException("Impossible de chercher un motif dans des fichiers binaires.");
        }

        if (criteria.isActive()){
            try {
            	LOGGER.debug("visitPatternCriteria:" + currentFile);

              PatternSearcher pSearcher = new PatternSearcher(currentFile);
              /*if (pSearcher.isBinary()){
                  accepted = false;
              }else{*/
                  pSearcher.setRegexpMode(criteria.isRegexpEnabled());
                  accepted &= pSearcher.findPattern(criteria.getPattern());
              //}
            } catch (Exception e){
                accepted = false;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void visitDuplicateSearchCriteria(DuplicateSearchCriteria criteria)  throws Exception {
        if (criteria.isActive()){

            criteria.directorySelected(UserSelectionMgr.get().getSelectedDirectory());
            while (!criteria.searchEnded()){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    //
                }
            }
            LOGGER.debug("Fichier en cours " + currentFile);
          if (criteria.duplicatesFound()){
        	  LOGGER.debug("Duplicates found " + criteria.getDuplicates());
              accepted = criteria.getDuplicates().contains(currentFile.getName());
            }
        }
    }

    public void visitEnded(){

    }

    public void visitStarted(){

    }
};


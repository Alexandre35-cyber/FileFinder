package org.maison.filefinder.model.criteria;

import org.maison.filefinder.model.FileSearchService;

public class SizeSearchCriteria extends SearchCriteria {

    private long sizeMin = -1;
    private long sizeMax = -1;

    private FileSearchService.UNIT chosenUnit;

    public SizeSearchCriteria(long min, long max, FileSearchService.UNIT chosenUnit){
        this.sizeMin = min;
        this.sizeMax = max;
        this.chosenUnit = chosenUnit;
    }

    public long getSizeMin() {
        return sizeMin;
    }

    public void setSizeMin(long sizeMin) throws Exception {
        ;
        if (sizeMax != -1){
            if (sizeMin> sizeMax){
                throw new Exception("La taille minimum est superieure à la taille maximum");
            }
        }
        this.sizeMin = sizeMin;
    }

    public long getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(long sizeMax) throws Exception {
        if (sizeMin != -1){
            if (sizeMin> sizeMax){
                throw new Exception("La taille minimum est superieure à la taille maximum");
            }
        }
        this.sizeMax = sizeMax;
        LOGGER.debug("sizeMin " + sizeMin);
        LOGGER.debug("sizeMax " + sizeMax);
    }

    public void setUnit(String choice) throws Exception{
        switch (choice){
            case "K":chosenUnit = FileSearchService.UNIT.KOS;
                break;
            case "M":chosenUnit = FileSearchService.UNIT.MOS;
                break;
            case "G":chosenUnit = FileSearchService.UNIT.GOS;
                break;
            default: chosenUnit = null;
                break;
        }
        
        if (chosenUnit == null){
            throw new Exception("Les seules valeurs possibles sont:[K,M,G] kilo, mega, giga octet");
        }
        LOGGER.debug("chosenUnit " + chosenUnit);
    }

    public FileSearchService.UNIT getChosenUnit() {
        return chosenUnit;
    }

    @Override
    public String getName() {
        return "Critere de taille";
    }

    public void accept(SearchCriteriaVisitor v) throws Exception {
            v.visitSizeCriteria(this);
    }

}

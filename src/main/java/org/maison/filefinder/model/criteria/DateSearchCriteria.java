package org.maison.filefinder.model.criteria;

import java.time.LocalDate;


public class DateSearchCriteria extends SearchCriteria {

    private LocalDate minDate;
    private LocalDate maxDate;

    public DateSearchCriteria(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) throws Exception {
        this.minDate = minDate;
        if (maxDate != null){
            if (maxDate.isBefore(minDate)){
                throw new Exception("La date " + minDate +" n'est pas anterieure à la date max " + maxDate);
            }
        }
    }

    public void reset(){
        maxDate = null;
        minDate = null;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) throws Exception {
        this.maxDate = maxDate;
        if (minDate != null){
            if (minDate.isAfter(maxDate)){
                throw new Exception("La date " + minDate +" n'est pas anterieure à la date max " + maxDate);
            }
        }
    }

    public void accept(SearchCriteriaVisitor visitor) throws Exception {
        visitor.visitDateCriteria(this);
    }

    @Override
    public String getName() {
        return "Critere de Date";
    }
}

package org.maison.filefinder.model.criteria;

import java.time.LocalDate;

/**
 * Critere de recherche sur la date de creation d un fichier
 */
public class DateSearchCriteria extends SearchCriteria {

    private LocalDate minDate;
    private LocalDate maxDate;

    /**
     * Constructeur
     * @param minDate
     * @param maxDate
     */
    public DateSearchCriteria(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        LOGGER.info("minDate " + minDate);
        LOGGER.info("maxDate " + maxDate);
    }

    /**
     * Getter date min
     * @return @link {java.time.LocalDate}
     */
    public LocalDate getMinDate() {
        return minDate;
    }

    /**
     * Setter date min
     * @param minDate @link {java.time.LocalDate}
     */
    public void setMinDate(LocalDate minDate) throws Exception {
        this.minDate = minDate;
        if (maxDate != null){
            if (maxDate.isBefore(minDate)){
                throw new Exception("La date " + minDate +" n'est pas anterieure à la date max " + maxDate);
            }
        }
        LOGGER.info("minDate " + minDate);
        LOGGER.info("maxDate " + maxDate);
    }

    /**
     * Remise a zero des dates
     */
    public void reset(){
        maxDate = null;
        minDate = null;
    }

    /**
     * Retourne la date max
     * @return
     */
    public LocalDate getMaxDate() {
        return maxDate;
    }

    /**
     * Positionne la date max
     * @param maxDate
     * @throws Exception
     */
    public void setMaxDate(LocalDate maxDate) throws Exception {
        this.maxDate = maxDate;
        if (minDate != null){
            if (minDate.isAfter(maxDate)){
                throw new Exception("La date " + minDate +" n'est pas anterieure à la date max " + maxDate);
            }
        }
        LOGGER.info("minDate " + minDate);
        LOGGER.info("maxDate " + maxDate);
    }

    /**
     * La prise en compte du critere se fait dans un visiteur
     * @param visitor
     * @throws Exception
     */
    public void accept(SearchCriteriaVisitor visitor) throws Exception {
        visitor.visitDateCriteria(this);
    }

    @Override
    public String getName() {
        return "Critere de Date";
    }
}

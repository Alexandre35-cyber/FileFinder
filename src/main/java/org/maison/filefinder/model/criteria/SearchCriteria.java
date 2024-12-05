package org.maison.filefinder.model.criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SearchCriteria {
    protected  Logger LOGGER = LoggerFactory.getLogger(SearchCriteria.class.getName());
    private boolean active = false;

    public abstract String getName();

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active){
    	LOGGER.debug("Application du critère " + getName() + " " + active);
        this.active = active;
    }

    public void accept(SearchCriteriaVisitor v) throws Exception {
        v.visitCriteria(this);
    }
}

package org.maison.filefinder.model.criteria;

public abstract class SearchCriteria {

    private boolean active = false;

    public abstract String getName();

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active){
        System.out.println("Application du critère " + getName() + " " + active);
        this.active = active;
    }

    public void accept(SearchCriteriaVisitor v) throws Exception {
        v.visitCriteria(this);
    }
}

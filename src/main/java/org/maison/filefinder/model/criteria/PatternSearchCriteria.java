package org.maison.filefinder.model.criteria;

public class PatternSearchCriteria extends SearchCriteria {

    private String pattern;
    private boolean regexpMode = false;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        System.out.println("Setting pattern " + pattern);
    }

    public boolean isRegexpEnabled(){
        return this.regexpMode;
    }

    public void setRegularExpressionMode(boolean value){
        this.regexpMode = value;
    }

    public void accept(SearchCriteriaVisitor visitor) throws Exception{
        visitor.visitPatternCriteria(this);
    }

    @Override
    public String getName() {
        return "Critère de motif textuel";
    }
}

package org.maison.filefinder.model.criteria;

public interface SearchCriteriaVisitor {
    void visitCriteria(SearchCriteria criteria) throws Exception;

    void visitSizeCriteria(SizeSearchCriteria criteria) throws Exception;

    void visitDateCriteria(DateSearchCriteria criteria) throws Exception;

    void visitPatternCriteria(PatternSearchCriteria criteria) throws Exception;

    void visitDuplicateSearchCriteria(DuplicateSearchCriteria criteria) throws Exception;
}

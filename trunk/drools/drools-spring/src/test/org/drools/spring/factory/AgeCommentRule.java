package org.drools.spring.factory;

import org.drools.FactException;
import org.drools.spi.KnowledgeHelper;

class AgeCommentRule {

    private int minAge = Integer.MIN_VALUE;
    private int maxAge = Integer.MAX_VALUE;
    private String comment;
    private AuditService auditService;

    public void setAuditService(AuditService service) {
        this.auditService = service;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public boolean condition(Person person) {
        return minAge <= person.getAge() && person.getAge() <= maxAge;
    }

    public void consequence(KnowledgeHelper knowledgeHelper, Person person) throws FactException {
        person.setComment(comment);
        knowledgeHelper.retractObject(person);

        String auditComment = knowledgeHelper.getRuleName() + ":" + person.getName();
        auditService.auditMessage(person, auditComment);
    }
}

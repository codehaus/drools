package org.drools.spring.factory;

import org.drools.FactException;
import org.drools.spi.KnowledgeHelper;

class ChildCommentRule {

    public boolean condition(Person person) {
        return 0 <= person.getAge() && person.getAge() <= 17;
    }

    public void consequence(KnowledgeHelper knowledgeHelper, Person person) throws FactException {
        person.setComment("this is a child comment");
        knowledgeHelper.retractObject(person);
    }
}

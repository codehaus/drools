package org.drools.spring.factory;

import org.drools.FactException;
import org.drools.spi.KnowledgeHelper;

public class AdultCommentRule {

    public boolean condition(Person person) {
        return 17 <= person.getAge();
    }

    public void consequence(KnowledgeHelper knowledgeHelper, Person person) throws FactException {
        person.setComment("this is an adult comment");
        knowledgeHelper.retractObject(person);
    }
}

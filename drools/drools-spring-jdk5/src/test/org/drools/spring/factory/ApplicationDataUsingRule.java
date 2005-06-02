package org.drools.spring.factory;

import org.drools.spring.metadata.annotation.java.Condition;
import org.drools.spring.metadata.annotation.java.Consequence;
import org.drools.spring.metadata.annotation.java.Data;
import org.drools.spring.metadata.annotation.java.Fact;
import org.drools.spring.metadata.annotation.java.Rule;

@Rule("ApplicationDataUsingRule")
class ApplicationDataUsingRule {

    @Condition
    public boolean condition(@Fact Person person) {
        return true;
    }

    @Consequence
    public void consequence(@Fact Person person, @Data("audit") AuditService service) {
        service.auditMessage(person, "audit message");
    }
}

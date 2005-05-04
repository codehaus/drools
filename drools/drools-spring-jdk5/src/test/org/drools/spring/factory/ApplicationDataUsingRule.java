package org.drools.spring.factory;

import org.drools.FactException;
import org.drools.spring.metadata.annotation.java.*;

@Rule("ApplicationDataUsingRule")
class ApplicationDataUsingRule {

    @Condition
    public boolean condition(@Fact Person person) {
        return true;
    }

    @Consequence
    public void consequence(@Fact Person person, @Data("audit") AuditService service) throws FactException {
        service.auditMessage(person, "audit message");
    }
}

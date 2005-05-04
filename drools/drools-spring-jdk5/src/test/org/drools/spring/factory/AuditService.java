package org.drools.spring.factory;

import java.util.ArrayList;
import java.util.List;

class AuditService {

    private List auditLogs = new ArrayList();

    public void auditMessage(Person person, String message) {
        auditLogs.add("Set person " + person.getName() + " message to " + message);
    }

    public List getAuditLogs() {
        return auditLogs;
    }
}

package org.drools.spring.factory;

import java.util.ArrayList;
import java.util.List;

class AuditService {

    private List<String> auditLogs = new ArrayList<String>();

    public void auditMessage(Person person, String message) {
        auditLogs.add("Set person " + person.getName() + " message to " + message);
    }

    public List<String> getAuditLogs() {
        return auditLogs;
    }
}

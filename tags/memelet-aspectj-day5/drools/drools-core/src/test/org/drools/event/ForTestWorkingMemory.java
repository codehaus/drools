package org.drools.event;

import java.util.List;
import java.util.Map;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.spi.AgendaFilter;

public class ForTestWorkingMemory implements WorkingMemory {

    public Map getApplicationDataMap() {
        return null;
    }

    public void setApplicationData(String name, Object value) {
    }

    public Object getApplicationData(String name) {
        return null;
    }

    public RuleBase getRuleBase() {
        return null;
    }

    public void fireAllRules() {
    }

    public void fireAllRules(AgendaFilter agendaFilter) {
    }

    public Object getObject(FactHandle handle) {
        return null;
    }

    public FactHandle getFactHandle(Object object) {
        return null;
    }

    public List getObjects() {
        return null;
    }

    public List getObjects(Class objectClass) {
        return null;
    }

    public List getFactHandles() {
        return null;
    }

    public boolean containsObject(FactHandle handle) {
        return false;
    }

    public FactHandle assertObject_return;
    public FactHandle assertObject(Object object) {
        return assertObject_return;
    }

    public Object retractObject_return;
    public Object retractObject(FactHandle handle) {
        return retractObject_return;
    }

    public Object modifyObject_return;
    public Object modifyObject(FactHandle handle, Object object) {
        return modifyObject_return;
    }

    public void clearAgenda() {
    }

}

package org.drools.spring.examples.jiahvac.control;

import java.beans.PropertyEditorSupport;

import org.drools.spring.examples.jiahvac.model.Vent;

public class VentStatePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws java.lang.IllegalArgumentException {
        setValue(Vent.State.valueOf(text));
    }

}

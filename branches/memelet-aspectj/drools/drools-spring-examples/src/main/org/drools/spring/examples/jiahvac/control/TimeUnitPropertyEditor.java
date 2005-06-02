package org.drools.spring.examples.jiahvac.control;

import java.beans.PropertyEditorSupport;
import java.util.concurrent.TimeUnit;

public class TimeUnitPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws java.lang.IllegalArgumentException {
        setValue(TimeUnit.valueOf(text));
    }

}

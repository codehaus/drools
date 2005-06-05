package org.drools.spring.pojorule;

import org.drools.spi.Tuple;

public class ApplicationDataArgument implements Argument {

    private final String name;
    private final Class clazz;

    public ApplicationDataArgument(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public Object getValue(Tuple tuple) {
        Object value = tuple.getWorkingMemory().getApplicationData(name);
        if (!clazz.isAssignableFrom(value.getClass())) {
            throw new IllegalStateException("Application data class different than declaration"
                    + ": app-data-name = " + name + ", expected class = " + clazz
                    + ", actual class = " + value.getClass());
        }
        return value;
    }

    public String getDataName() {
        return name;
    }

    public Class getDataClass() {
        return clazz;
    }
}

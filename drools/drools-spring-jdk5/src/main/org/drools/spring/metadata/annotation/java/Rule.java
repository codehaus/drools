package org.drools.spring.metadata.annotation.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

@Inherited @Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule
{
    String value() default "";
    String name() default ""; // == value

    String documentation() default "";
    int salience() default Integer.MIN_VALUE;
    long duration() default Long.MIN_VALUE;

    enum Loop {
        ALLOW(true), DISALLOW(false), DEFAULT(null);

        private Boolean value;

        Loop(Boolean value) {
            this.value = value;
        }

        boolean getValue() {
            if (value == null) {
                throw new IllegalStateException("Cannot call get getValue on DEFAULT_LOOP");
            }
            return value;
        }
    }

    Loop loop() default Loop.DEFAULT;

}

package org.drools.semantics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

public interface Drools
{
    @Inherited @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME) 
    public @interface Rule 
    {}
    
    @Inherited @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME) 
    public @interface Parameter 
    { 
        String value(); // identifier
    }
    
    @Inherited @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME) 
    public @interface ApplicationData 
    { 
        String value(); // name
    }
    
    @Inherited @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME) 
    public @interface Condition 
    {}
     
    @Inherited @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME) 
    public @interface Consequence 
    {}
}

package org.drools.semantics.annotation;

import java.util.List;

//import java.lang.annotation.ElementType;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Inherited;
//import java.lang.annotation.Target;
//import java.lang.annotation.Retention;

import org.drools.FactException;

/**
 * This methods on this interface are identical to the public api of
 * org.drools.spi.KnowledgeHelper. This is to allow mocking when unit-testing
 * rules. If an interface were added to KnowledgeHelper this interface would no
 * longer be needed for this purpose.
 * 
 * (Note however, that once clover can handle it, the inner annotations will
 * replace the currently used outer annotations. Thus, this interface will
 * probably remain.)
 */
public interface Drools
{
    /*
     * @Inherited @Target({ElementType.TYPE})
     * @Retention(RetentionPolicy.RUNTIME) public @interface Rule {}
     * 
     * @Inherited @Target({ElementType.PARAMETER})
     * @Retention(RetentionPolicy.RUNTIME) public @interface Parameter { String
     * value(); // identifier }
     * 
     * @Inherited @Target({ElementType.PARAMETER})
     * @Retention(RetentionPolicy.RUNTIME) public @interface ApplicationData {
     * String value(); // name }
     * 
     * @Inherited @Target({ElementType.METHOD})
     * @Retention(RetentionPolicy.RUNTIME) public @interface Condition {}
     * 
     * @Inherited @Target({ElementType.METHOD})
     * @Retention(RetentionPolicy.RUNTIME) public @interface Consequence {}
     */

    void assertObject( Object object ) throws FactException;

    void assertObject( Object object, boolean dynamic ) throws FactException;

    void clearAgenda( );

    List getObjects( );

    List getObjects( Class objectClass );

    String getRuleName( );

    void modifyObject( Object object ) throws FactException;

    void modifyObject( Object oldObject, Object newObject ) throws FactException;

    void retractObject( Object object ) throws FactException;

}

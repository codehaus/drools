package org.drools.sementics.annotation.examples;

import org.drools.semantics.annotation.examples.helloworld.HelloWorldDrlExample;
import org.drools.semantics.annotation.examples.helloworld.HelloWorldDrlMixedExample;
import org.drools.semantics.annotation.examples.helloworld.HelloWorldNativeExample;
import org.drools.semantics.annotation.examples.helloworld.HelloWorldSpringInnerPojosExample;
import org.drools.semantics.annotation.examples.helloworld.HelloWorldSpringOuterPojosExample;

import junit.framework.TestCase;

/**
 * The purpose of this test is to run the examples - no assertions are performed.
 */
public class RunExamplesTest extends TestCase {

    public void testHelloWorldNativeExample() throws Exception {
        HelloWorldNativeExample.main(null);
    }

    public void testHelloWorldSpringInnerPojosExample() throws Exception {
        HelloWorldSpringInnerPojosExample.main(null);
    }

    public void testHelloWorldSpringOuterPojosExample() throws Exception {
        HelloWorldSpringOuterPojosExample.main(null);
    }

    /*
     * TODO If these tests are ordered prior to testHelloWorldSpringOuterPojosExample, then
     * we get the below exception. Could it be due to the way the annotation.xsd is 
     * defined (DROOLS-272)?
     * 
     * org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.drools.semantics.annotation.examples.helloworld.HelloRule' defined in class path resource [org/drools/semantics/annotation/examples/helloworld/helloworld.appctx.xml]: Error setting property values; nested exception is org.springframework.beans.PropertyAccessExceptionsException: PropertyAccessExceptionsException (1 errors); nested propertyAccessExceptions are: [org.springframework.beans.MethodInvocationException: Property 'messagePrinter' threw exception; nested exception is java.lang.IllegalAccessException: Class org.springframework.beans.BeanWrapperImpl can not access a member of class org.drools.semantics.annotation.examples.helloworld.HelloRule with modifiers "public"]
     * PropertyAccessExceptionsException (1 errors)
     * org.springframework.beans.MethodInvocationException: Property 'messagePrinter' threw exception; nested exception is java.lang.IllegalAccessException: Class org.springframework.beans.BeanWrapperImpl can not access a member of class org.drools.semantics.annotation.examples.helloworld.HelloRule with modifiers "public"
     * java.lang.IllegalAccessException: Class org.springframework.beans.BeanWrapperImpl can not access a member of class org.drools.semantics.annotation.examples.helloworld.HelloRule with modifiers "public"
     *      at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:65)
     *      at java.lang.reflect.Method.invoke(Method.java:578)
     *      at org.springframework.beans.BeanWrapperImpl.setPropertyValue(BeanWrapperImpl.java:779)
     */
    
    public void testHelloWorldDrlExample() throws Exception {
        HelloWorldDrlExample.main(null);
    }

    public void testHelloWorldDrlMixedExample() throws Exception {
        HelloWorldDrlMixedExample.main(null);
    }
}

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


    public void testHelloWorldDrlExample() throws Exception {
        HelloWorldDrlExample.main(null);
    }

    /*
     * TODO This example fails to run from within JUnit or as a run-target witin
     * Eclipse. But it does run from Maven :-{
     */
//    public void testHelloWorldDrlMixedExample() throws Exception {
//        HelloWorldDrlMixedExample.main(null);
//    }

}

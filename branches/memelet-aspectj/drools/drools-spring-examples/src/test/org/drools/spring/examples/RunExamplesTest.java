package org.drools.spring.examples;

import org.drools.spring.examples.helloworld.HelloWorldSpringInnerPojosExample;
import org.drools.spring.examples.helloworld.HelloWorldSpringOuterPojosExample;

import junit.framework.TestCase;

/**
 * The purpose of this test is to run the examples - no assertions are performed.
 */
public class RunExamplesTest extends TestCase {

    public void testHelloWorldSpringInnerPojosExample() throws Exception {
        HelloWorldSpringInnerPojosExample.main(null);
    }

    public void testHelloWorldSpringOuterPojosExample() throws Exception {
        HelloWorldSpringOuterPojosExample.main(null);
    }
}

package org.drools.spring.factory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.spring.SpringTestSetup;

public class RuleSetFactoryBeanApplicationDataTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(RuleSetFactoryBeanApplicationDataTest.class),
                contextHolder, "org/drools/spring/factory/appdata-test.appctx.xml");
    }

//    private RuleSetFactoryBean factory = new RuleSetFactoryBean();

    // TODO jira:DROOLS-322
    public void testApplicationData() throws Exception {
//        WorkingMemory workingMemory = (WorkingMemory) contextHolder.context.getBean("workingMemory");
//
//        workingMemory.setApplicationData("audit", AuditService.class);
    }

}

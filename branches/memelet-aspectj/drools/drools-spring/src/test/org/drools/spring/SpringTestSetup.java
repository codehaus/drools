package org.drools.spring;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.extensions.TestSetup;
import junit.framework.Test;

/**
 * Creates the single specified spring ApplicationContext for the entire TestCase.
 * The context is destroyed after all tests have run.
 */
public class SpringTestSetup extends TestSetup {

    public static class ContextHolder {
        public ConfigurableApplicationContext context;
    }

    private final ContextHolder contextHolder;
    private final String configFilename;

    public SpringTestSetup(Test test, ContextHolder contextHolder, String configFilename) {
        super(test);
        this.contextHolder = contextHolder;
        this.configFilename = configFilename;
    }

    protected void setUp() throws Exception {
        Logger.getLogger("org.springframework").setLevel(Level.WARNING);
        contextHolder.context = new ClassPathXmlApplicationContext(configFilename);
        assertNotNull(contextHolder.context);
    }

    protected void tearDown() throws Exception {
        contextHolder.context.close();
        contextHolder.context = null;
    }
}


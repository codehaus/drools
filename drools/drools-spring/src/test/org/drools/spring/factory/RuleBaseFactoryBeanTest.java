package org.drools.spring.factory;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.RuleBase;
import org.drools.conflict.CompositeConflictResolver;
import org.drools.rule.RuleSet;
import org.drools.spring.SpringTestSetup;

public class RuleBaseFactoryBeanTest extends TestCase {

    private static SpringTestSetup.ContextHolder contextHolder = new SpringTestSetup.ContextHolder();

    public static Test suite() {
        return new SpringTestSetup(
                new TestSuite(RuleBaseFactoryBeanTest.class),
                contextHolder, "org/drools/spring/factory/rulebase.appctx.xml");
    }

    private RuleBaseFactoryBean ruleBaseFactoryBean;
    private RuleBase ruleBase;
    private RuleBase autoDetectRuleBase;
    
    protected void setUp() throws Exception {
        super.setUp();
        ruleBaseFactoryBean = (RuleBaseFactoryBean) contextHolder.context.getBean("&ruleBase");
        ruleBase = (RuleBase) contextHolder.context.getBean("ruleBase");
        autoDetectRuleBase = (RuleBase) contextHolder.context.getBean("ruleBase.autoDetect");
    }

    public void testGetObjectType() throws Exception {
        assertSame(RuleBase.class, ruleBaseFactoryBean.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        assertTrue(ruleBaseFactoryBean.isSingleton());
        assertSame(ruleBaseFactoryBean.getObject(), ruleBaseFactoryBean.getObject());
    }

    public void testRuleSets() throws Exception {
        assertNotNull(ruleBase);
        List ruleSets = ruleBase.getRuleSets();
        assertEquals(2, ruleSets.size());
        Set ruleSetNames = new HashSet();
        ruleSetNames.add(((RuleSet)ruleSets.get(0)).getName());
        ruleSetNames.add(((RuleSet)ruleSets.get(1)).getName());
        assertTrue(ruleSetNames.contains("ruleSet.beanRules.NAME"));
        assertTrue(ruleSetNames.contains("ruleSet.pojoRules"));
    }
    
    public void testAutoDetectRuleSets() throws Exception {
        assertNotNull(autoDetectRuleBase);
        List ruleSets = autoDetectRuleBase.getRuleSets();
        assertEquals(2, ruleSets.size());
        Set ruleSetNames = new HashSet();
        ruleSetNames.add(((RuleSet)ruleSets.get(0)).getName());
        ruleSetNames.add(((RuleSet)ruleSets.get(1)).getName());
        assertTrue(ruleSetNames.contains("ruleSet.beanRules.NAME"));
        assertTrue(ruleSetNames.contains("ruleSet.pojoRules"));
    }
    
    public void testConflictResolver() throws Exception {
        assertTrue(ruleBase.getConflictResolver() instanceof CompositeConflictResolver);
    }

    public void testFactHandle() throws Exception {
        assertTrue(ruleBase.getFactHandleFactory() instanceof NullFactHandleFactory);
    }
}


package org.drools.spring.pojorule;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.KnowledgeHelperArgument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class KnowledgeHelperArgumentTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private Rule rule = new Rule("for-test");
    private MockControl controlTuple = mocks.createControl(Tuple.class);

    private KnowledgeHelperArgument arg = new KnowledgeHelperArgument(rule);

    public void testGetValue() throws Exception {
        mocks.replay();

        Object value = arg.getValue((Tuple)controlTuple.getMock());

        mocks.verify();
        assertTrue(value instanceof DefaultKnowledgeHelper);
        // TODO assert the constructor args using AOP.
    }
}

package org.drools.spring.metadata;

import org.drools.rule.Rule;
import org.drools.spring.metadata.DataArgumentMetadata;
import org.drools.spring.pojorule.ApplicationDataArgument;
import org.drools.spring.pojorule.Argument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

import junit.framework.TestCase;

public class DataArgumentMetadataTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl controlRule = mocks.createControl(Rule.class, new Class[]{String.class}, new Object[]{"for-test"});
    private Rule mockRule = (Rule) controlRule.getMock();

    public void testNewWithNullIdentifier() {
        try {
            new DataArgumentMetadata(null, String.class);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewWithBlankIdentifier() {
        try {
            new DataArgumentMetadata("  ", String.class);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewWithNullParameterClass() {
        try {
            new DataArgumentMetadata("id", null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testCreateArgument() throws Exception {
        DataArgumentMetadata metadata =
                new DataArgumentMetadata("id", String.class);

        mocks.replay();

        Argument argument = metadata.createArgument(mockRule);

        mocks.verify();

        assertTrue(argument instanceof ApplicationDataArgument);
        ApplicationDataArgument appDataArgument = (ApplicationDataArgument)argument;
        assertEquals("id", appDataArgument.getDataName());
        assertEquals(String.class, appDataArgument.getDataClass());
    }
}
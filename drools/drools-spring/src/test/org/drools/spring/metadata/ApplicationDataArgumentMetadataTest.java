package org.drools.spring.metadata;

import org.drools.rule.Rule;
import org.drools.spring.metadata.ApplicationDataArgumentMetadata;
import org.drools.spring.pojorule.ApplicationDataArgument;
import org.drools.spring.pojorule.Argument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

import junit.framework.TestCase;

public class ApplicationDataArgumentMetadataTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl controlRule = mocks.createControl(Rule.class, new Class[]{String.class}, new Object[]{"for-test"});
    private Rule mockRule = (Rule) controlRule.getMock();

    public void testNewWithNullIdentifier() {
        try {
            new ApplicationDataArgumentMetadata(null, String.class);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewWithBlankIdentifier() {
        try {
            new ApplicationDataArgumentMetadata("  ", String.class);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewWithNullParameterClass() {
        try {
            new ApplicationDataArgumentMetadata("id", null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testCreateArgument() throws Exception {
        ApplicationDataArgumentMetadata metadata =
                new ApplicationDataArgumentMetadata("id", String.class);

        mocks.replay();

        Argument argument = metadata.createArgument(mockRule);

        mocks.verify();

        assertTrue(argument instanceof ApplicationDataArgument);
        ApplicationDataArgument appDataArgument = (ApplicationDataArgument)argument;
        assertEquals("id", appDataArgument.getDataName());
        assertEquals(String.class, appDataArgument.getDataClass());
    }
}
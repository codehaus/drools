package org.drools.spring.metadata.annotation.java;

import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.annotation.java.Rule.Loop;

import junit.framework.TestCase;

public class AnnotationRuleMetadataSourceTest extends TestCase {

    private AnnotationRuleMetadataSource source = new AnnotationRuleMetadataSource();

    public void testNoAnnotation() throws Exception {
        class PojoRule {}

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        assertNull(metadata);
    }

    public void testNoValues() throws Exception {
        @Rule
        class PojoRule {}

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        assertNull(metadata);
    }

    public void testDefaultValues() throws Exception {
        @Rule("myRule")
        class PojoRule {}

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        assertEquals("myRule", metadata.getName());
        assertNull(metadata.getDocumentation());
        assertNull(metadata.getSalience());
        assertNull(metadata.getDuration());
        assertNull(metadata.getNoLoop());
    }

    public void testNameDefaulted() throws Exception {
        @Rule(documentation="myDocumentation")
        class PojoRule {}

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        assertEquals(PojoRule.class.getName(), metadata.getName());
        assertEquals("myDocumentation",  metadata.getDocumentation());
    }

    public void testSpecifiedValues() throws Exception {
        @Rule(name="myRule",
              documentation="myDocumentation",
              salience=10,
              duration=20,
              loop=Loop.ALLOW)
        class PojoRule {}

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        assertEquals("myRule", metadata.getName());
        assertEquals("myDocumentation", metadata.getDocumentation());
        assertEquals(10, (int)metadata.getSalience());
        assertEquals(20, (long)metadata.getDuration());
        assertTrue(metadata.getNoLoop());
    }
}

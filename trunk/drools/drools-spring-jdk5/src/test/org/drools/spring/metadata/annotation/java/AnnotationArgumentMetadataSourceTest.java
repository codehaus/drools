package org.drools.spring.metadata.annotation.java;

import java.lang.reflect.Method;

import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.DataArgumentMetadata;
import org.drools.spring.metadata.FactArgumentMetadata;
import org.drools.spring.metadata.annotation.java.AnnotationArgumentMetadataSource;
import org.drools.spring.metadata.annotation.java.Consequence;
import org.drools.spring.metadata.annotation.java.Data;
import org.drools.spring.metadata.annotation.java.Fact;

import junit.framework.TestCase;

public class AnnotationArgumentMetadataSourceTest extends TestCase {

    private static class PojoRule {
        @Consequence
        public void factParameterDefaultIdentifier(@Fact String fact) { }
        @Consequence
        public void factParameterExplicitIdentifier(@Fact("myFact") String fact) { }
        @Consequence
        public void factDataParameter(@Data("myData") Integer data) { }
    }

    private Method factParameterDefaultIdentifierMethod;
    private Method factParameterExplicitIdentifier;
    private Method factDataParameter;

    private AnnotationArgumentMetadataSource source = new AnnotationArgumentMetadataSource();

    @Override
    protected void setUp() throws Exception {
        factParameterDefaultIdentifierMethod = PojoRule.class.getMethod("factParameterDefaultIdentifier", new Class[]{String.class});
        factParameterExplicitIdentifier = PojoRule.class.getMethod("factParameterExplicitIdentifier", new Class[]{String.class});
        factDataParameter = PojoRule.class.getMethod("factDataParameter", new Class[]{Integer.class});
    }

    public void testGetArgumentMetadataFactDefaultIdentifier() throws Exception {
        ArgumentMetadata metadata = source.getArgumentMetadata(
                factParameterDefaultIdentifierMethod, String.class, 0);

        assertTrue(metadata instanceof FactArgumentMetadata);
        FactArgumentMetadata factMetadata = (FactArgumentMetadata) metadata;
        assertTrue(factMetadata.getIdentifier() != null &&  factMetadata.getIdentifier().length() > 0);
    }

    public void testGetArgumentMetadataFactExplicitDefaultIdentifier() throws Exception {
        ArgumentMetadata metadata = source.getArgumentMetadata(
                factParameterExplicitIdentifier, String.class, 0);

        assertTrue(metadata instanceof FactArgumentMetadata);
        FactArgumentMetadata factMetadata = (FactArgumentMetadata) metadata;
        assertEquals("myFact", factMetadata.getIdentifier());
        assertSame(String.class, factMetadata.getParameterClass());
    }

    public void testGetArgumentMetadataData() throws Exception {
        ArgumentMetadata metadata = source.getArgumentMetadata(
                factDataParameter, Integer.class, 0);

        assertTrue(metadata instanceof DataArgumentMetadata);
        DataArgumentMetadata dataMetadata = (DataArgumentMetadata) metadata;
        assertEquals("myData", dataMetadata.getIdentifier());
        assertSame(Integer.class, dataMetadata.getParameterClass());
    }
}

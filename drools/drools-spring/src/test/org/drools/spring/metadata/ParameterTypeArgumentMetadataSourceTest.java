package org.drools.spring.metadata;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;
import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.FactArgumentMetadata;
import org.drools.spring.metadata.KnowledgeHelperArgumentMetadata;
import org.drools.spring.metadata.ParameterInferedTypeArgumentMetadataSource;
import org.drools.spring.pojorule.Argument;

public class ParameterTypeArgumentMetadataSourceTest extends TestCase {

    private ParameterInferedTypeArgumentMetadataSource source = new ParameterInferedTypeArgumentMetadataSource();

    private static class ArgumentForTest implements Argument {
        public Object getValue(Tuple tuple) {
            return "value";
        }
    }
    private static class ArgumentMetadataForTest implements ArgumentMetadata {
        public Argument createArgument(Rule rule) {
            return new ArgumentForTest();
        }
    }
    private static class ParameterTypeArgumentMetadataFactoryForTest implements ParameterInferedTypeArgumentMetadataSource.ParameterTypeArgumentMetadataFactory {
        public ArgumentMetadata createMetadata(Class parameterType) {
            return new ArgumentMetadataForTest();
        }
    }
    private static ParameterTypeArgumentMetadataFactoryForTest parameterTypeArgumentMetadataFactoryForTest =
            new ParameterTypeArgumentMetadataFactoryForTest();

    public void testDefaultKnoweledgeHelper() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper) {}
        }

        ArgumentMetadata[] metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{KnowledgeHelper.class}));

        assertEquals(1, metadata.length);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata[0].getClass()));
    }

    public void testDefaultFallback() throws Exception {
        class Pojo {
            public void consequence(String fact) {}
        }

        ArgumentMetadata[] metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{String.class}));

        assertEquals(1, metadata.length);
        assertTrue(FactArgumentMetadata.class.isAssignableFrom(metadata[0].getClass()));
    }

    public void testSetFallback() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper, String fact) {}
        }

        source.setFallbackParameterTypeArgumentMetadataFactory(parameterTypeArgumentMetadataFactoryForTest);

        ArgumentMetadata[] metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{KnowledgeHelper.class, String.class}));

        assertEquals(2, metadata.length);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata[0].getClass()));
        assertTrue(ArgumentMetadataForTest.class.isAssignableFrom(metadata[1].getClass()));
    }

    public void testSetArgumentMetadataFactories() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper, String fact1, Integer fact2) {}
        }

        Map factories = new HashMap();
        factories.put(String.class, parameterTypeArgumentMetadataFactoryForTest);
        source.setArgumentMetadataFactories(factories);

        ArgumentMetadata[] metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{KnowledgeHelper.class, String.class, Integer.class}));

        assertEquals(3, metadata.length);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata[0].getClass()));
        assertTrue(ArgumentMetadataForTest.class.isAssignableFrom(metadata[1].getClass()));
        assertTrue(FactArgumentMetadata.class.isAssignableFrom(metadata[2].getClass()));
    }

    public void testAddArgumentMetadataFactory() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper, String fact1, Integer fact2) {}
        }

        source.addArgumentMetadataFactory(Integer.class, parameterTypeArgumentMetadataFactoryForTest);

        ArgumentMetadata[] metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{KnowledgeHelper.class, String.class, Integer.class}));

        assertEquals(3, metadata.length);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata[0].getClass()));
        assertTrue(FactArgumentMetadata.class.isAssignableFrom(metadata[1].getClass()));
        assertTrue(ArgumentMetadataForTest.class.isAssignableFrom(metadata[2].getClass()));
    }
}

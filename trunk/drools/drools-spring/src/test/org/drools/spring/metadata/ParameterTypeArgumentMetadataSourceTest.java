package org.drools.spring.metadata;

import java.lang.reflect.Method;
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
        public Class getParameterClass() {
            return String.class;
        }
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

        ArgumentMetadata metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{KnowledgeHelper.class}),
                KnowledgeHelper.class, 0);

        assertNotNull(metadata);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata.getClass()));
    }

    public void testDefaultFallback() throws Exception {
        class Pojo {
            public void consequence(String fact) {}
        }

        ArgumentMetadata metadata = source.getArgumentMetadata(
                Pojo.class.getDeclaredMethod("consequence", new Class[]{String.class}),
                String.class, 1);

        assertNotNull(metadata);
        assertTrue(FactArgumentMetadata.class.isAssignableFrom(metadata.getClass()));
    }

    public void testSetFallback() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper, String fact) {}
        }

        source.setFallbackParameterTypeArgumentMetadataFactory(parameterTypeArgumentMetadataFactoryForTest);

        Method pojoMethod = Pojo.class.getDeclaredMethod(
                "consequence", new Class[]{KnowledgeHelper.class, String.class});
        ArgumentMetadata metadata_0 = source.getArgumentMetadata(pojoMethod, KnowledgeHelper.class, 0);
        ArgumentMetadata metadata_1 = source.getArgumentMetadata(pojoMethod, String.class, 1);

        assertNotNull(metadata_0);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata_0.getClass()));
        assertNotNull(metadata_1);
        assertTrue(ArgumentMetadataForTest.class.isAssignableFrom(metadata_1.getClass()));
    }

    public void testSetArgumentMetadataFactories() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper, String fact1, Integer fact2) {}
        }

        Map factories = new HashMap();
        factories.put(String.class, parameterTypeArgumentMetadataFactoryForTest);
        source.setArgumentMetadataFactories(factories);

        Method pojoMethod = Pojo.class.getDeclaredMethod(
                "consequence", new Class[]{KnowledgeHelper.class, String.class, Integer.class});
        ArgumentMetadata metadata_0 = source.getArgumentMetadata(pojoMethod, KnowledgeHelper.class, 0);
        ArgumentMetadata metadata_1 = source.getArgumentMetadata(pojoMethod, String.class, 1);
        ArgumentMetadata metadata_2 = source.getArgumentMetadata(pojoMethod, Integer.class, 2);

        assertNotNull(metadata_0);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata_0.getClass()));
        assertNotNull(metadata_1);
        assertTrue(ArgumentMetadataForTest.class.isAssignableFrom(metadata_1.getClass()));
        assertNotNull(metadata_2);
        assertTrue(FactArgumentMetadata.class.isAssignableFrom(metadata_2.getClass()));
    }

    public void testAddArgumentMetadataFactory() throws Exception {
        class Pojo {
            public void consequence(KnowledgeHelper knowledgeHelper, String fact1, Integer fact2) {}
        }

        source.addArgumentMetadataFactory(Integer.class, parameterTypeArgumentMetadataFactoryForTest);

        Method pojoMethod = Pojo.class.getDeclaredMethod(
                "consequence", new Class[]{KnowledgeHelper.class, String.class, Integer.class});
        ArgumentMetadata metadata_0 = source.getArgumentMetadata(pojoMethod, KnowledgeHelper.class, 0);
        ArgumentMetadata metadata_1 = source.getArgumentMetadata(pojoMethod, String.class, 1);
        ArgumentMetadata metadata_2 = source.getArgumentMetadata(pojoMethod, Integer.class, 2);

        assertNotNull(metadata_0);
        assertTrue(KnowledgeHelperArgumentMetadata.class.isAssignableFrom(metadata_0.getClass()));
        assertNotNull(metadata_1);
        assertTrue(FactArgumentMetadata.class.isAssignableFrom(metadata_1.getClass()));
        assertNotNull(metadata_2);
        assertTrue(ArgumentMetadataForTest.class.isAssignableFrom(metadata_2.getClass()));
    }
}

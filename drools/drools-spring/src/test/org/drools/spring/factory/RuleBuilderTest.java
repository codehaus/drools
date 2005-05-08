package org.drools.spring.factory;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.drools.DroolsException;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;
import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.BeanObjectType;
import org.drools.spring.metadata.DataArgumentMetadata;
import org.drools.spring.metadata.FactArgumentMetadata;
import org.drools.spring.metadata.KnowledgeHelperArgumentMetadata;
import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.PojoCondition;
import org.drools.spring.pojorule.PojoConsequence;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class RuleBuilderTest extends TestCase {

    private static class PojoRule {
        public int getValue() { return 0; }
        private void performTask() { }
        public boolean conditionOne(String stringValue) { return false; }
        public boolean conditionTwo(long longValue, int intValue) { return false; }
        public void consequenceOne(String stringValue) { }
        public void consequenceTwo(Object object, long longValue, int intValue, KnowledgeHelper knowledgeHelper) { }
    }
    private PojoRule pojo = new PojoRule();

    private static Method getValueMethod;
    private static Method performTaskMethod;
    private static Method conditionOneMethod;
    private static Method conditionTwoMethod;
    private static Method consequenceOneMethod;
    private static Method consequenceTwoMethod;

    private static ArgumentMetadata[] conditionOneArgumentMetadata;
    private static ArgumentMetadata[] conditionTwoArgumentMetadata;
    private static ArgumentMetadata[] consequenceOneArgumentMetadata;
    private static ArgumentMetadata[] consequenceTwoArgumentMetadata;

    private static Set expectedDeclarationObjectTypes = new HashSet();
    private static Set expectedConditionMethodNames = new HashSet();
    private static Set expectedConsequenceMethodNames = new HashSet();

    private static MethodMetadata CONDITION_METADATA = new MethodMetadata(MethodMetadata.CONDITION);
    private static MethodMetadata CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.CONSEQUENCE);

    private static final Argument ARGUMENT = new Argument() {
        public Object getValue(Tuple tuple) {
            return null;
        }
    };

    private boolean isStaticSetUp;

    private EasymockContainer mocks = new EasymockContainer( );

    private Rule rule = new Rule("test-rule");

    private MockControl controlMethodMetadataSource = mocks.createNiceControl(MethodMetadataSource.class);
    private MethodMetadataSource mockMethodMetadataSource = (MethodMetadataSource) controlMethodMetadataSource.getMock();

    private MockControl controlArgumentMetadataSource = mocks.createControl(ArgumentMetadataSource.class);
    private ArgumentMetadataSource mockArgumentMetadataSource = (ArgumentMetadataSource) controlArgumentMetadataSource.getMock();

    private RuleBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        staticSetUp();
        builder = new RuleBuilder();
        builder.setMethodMetadataSource(mockMethodMetadataSource);
        builder.setArgumentMetadataSource(mockArgumentMetadataSource);
    }

    protected void staticSetUp() throws Exception {
        if (!isStaticSetUp) {
            try {
                // conditionOne
                conditionOneMethod = PojoRule.class.getDeclaredMethod("conditionOne", new Class[]{
                        String.class
                });
                conditionOneArgumentMetadata = new ArgumentMetadata[]{
                        new FactArgumentMetadata(null, String.class)
                };
                expectedDeclarationObjectTypes.add(new BeanObjectType(String.class));
                expectedConditionMethodNames.add(conditionOneMethod.getName());

                // conditionTwo
                conditionTwoMethod = PojoRule.class.getDeclaredMethod("conditionTwo", new Class[]{
                        long.class, int.class
                });
                conditionTwoArgumentMetadata = new ArgumentMetadata[]{
                        new FactArgumentMetadata(null, long.class),
                        new DataArgumentMetadata("value", int.class)
                };
                expectedDeclarationObjectTypes.add(new BeanObjectType(long.class));
                expectedConditionMethodNames.add(conditionTwoMethod.getName());

                // consequenceOne
                consequenceOneMethod = PojoRule.class.getDeclaredMethod("consequenceOne", new Class[]{
                        String.class
                });
                consequenceOneArgumentMetadata = new ArgumentMetadata[]{
                        new FactArgumentMetadata(null, String.class)
                };
                expectedConsequenceMethodNames.add(consequenceOneMethod.getName());

                // consequenceTwo
                consequenceTwoMethod = PojoRule.class.getDeclaredMethod("consequenceTwo", new Class[]{
                        Object.class,
                        long.class,
                        int.class,
                        KnowledgeHelper.class
                });
                consequenceTwoArgumentMetadata = new ArgumentMetadata[]{
                        new FactArgumentMetadata(null, Object.class),
                        new FactArgumentMetadata(null, long.class),
                        new DataArgumentMetadata("value", int.class),
                        new KnowledgeHelperArgumentMetadata()
                };
                expectedDeclarationObjectTypes.add(new BeanObjectType(Object.class));
                expectedDeclarationObjectTypes.add(new BeanObjectType(long.class));
                expectedConsequenceMethodNames.add(consequenceTwoMethod.getName());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            isStaticSetUp = true;
        }
    }

    //---- Rule

    private void methodMetadataSourceExpectAndReturn(Method method, MethodMetadata metadata) {
        controlMethodMetadataSource.expectAndReturn(
                mockMethodMetadataSource.getMethodMetadata(method),
                metadata);
    }

    private void argumentMetadataSourceExpectAndReturn(Method method, ArgumentMetadata[] metadata) {
        for (int i = 0; i < metadata.length; i++) {
            controlArgumentMetadataSource.expectAndReturn(
                    mockArgumentMetadataSource.getArgumentMetadata(method, metadata[i].getParameterClass(), i),
                    metadata[i]);
        }
    }

    private ArgumentMetadata[] argumentMetadataExpectAndReturnCreateArgument(int argCount) throws DroolsException {
        ArgumentMetadata[] metadata = new ArgumentMetadata[argCount];
        for (int i = 0; i < argCount; i++) {
            MockControl control = mocks.createControl(ArgumentMetadata.class);
            metadata[i] = (ArgumentMetadata) control.getMock();
            control.expectAndReturn(metadata[i].createArgument(rule), ARGUMENT);
        }
        return metadata;
    }

    public void testBuildRule() throws Exception {

        methodMetadataSourceExpectAndReturn(conditionOneMethod, CONDITION_METADATA);
        argumentMetadataSourceExpectAndReturn(conditionOneMethod, conditionOneArgumentMetadata);

        methodMetadataSourceExpectAndReturn(conditionTwoMethod, CONDITION_METADATA);
        argumentMetadataSourceExpectAndReturn(conditionTwoMethod, conditionTwoArgumentMetadata);

        methodMetadataSourceExpectAndReturn(consequenceOneMethod, CONSEQUENCE_METADATA);
        argumentMetadataSourceExpectAndReturn(consequenceOneMethod, consequenceOneArgumentMetadata);

        methodMetadataSourceExpectAndReturn(consequenceTwoMethod, CONSEQUENCE_METADATA);
        argumentMetadataSourceExpectAndReturn(consequenceTwoMethod, consequenceTwoArgumentMetadata);

        mocks.replay();

        Rule populatedRule = builder.buildRule(rule, pojo);

        mocks.verify();

        // We check only the declaration ObjectType since all FactArgument's are using default
        // identifiers based on the parameter class (ie, all identifiers for a given type
        // will be identical).
        List parameterDeclarations = rule.getParameterDeclarations();
        assertEquals(expectedDeclarationObjectTypes.size(), parameterDeclarations.size());
        for (Iterator iter = parameterDeclarations.iterator(); iter.hasNext();) {
            Declaration declaration = (Declaration) iter.next();
            if (!expectedDeclarationObjectTypes.contains(declaration.getObjectType())) {
                fail("Unexpected rule declaration object-type: " + declaration.getObjectType());
            }
        }

        List conditions = rule.getConditions();
        assertEquals(2, conditions.size());
        for (Iterator iter = conditions.iterator(); iter.hasNext();) {
            Condition condition = (Condition) iter.next();
            assertTrue(condition instanceof PojoCondition);
            PojoCondition pojoCondition = (PojoCondition) condition;
            if (!expectedConditionMethodNames.contains(pojoCondition.getMethodName())) {
                fail("Unexpected rule pojo-condition method: " + pojoCondition.getMethodName());
            }
        }

        Consequence consequence = rule.getConsequence();
        assertNotNull(consequence);
        assertTrue(consequence instanceof PojoConsequence);
        PojoConsequence pojoConsequence = (PojoConsequence) consequence;
        String[] pojoConsequenceMethodNames = pojoConsequence.getMethodNames();
        for (int i = 0; i < pojoConsequenceMethodNames.length; i++) {
            if (!expectedConsequenceMethodNames.contains(pojoConsequenceMethodNames[i])) {
                fail("Unexpected rule pojo-consequence method: " + pojoConsequenceMethodNames[i]);
            }
        }
    }
}

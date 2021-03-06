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



import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.KnowledgeHelper;
import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.BeanObjectType;
import org.drools.spring.metadata.DataArgumentMetadata;
import org.drools.spring.metadata.FactArgumentMetadata;
import org.drools.spring.metadata.KnowledgeHelperArgumentMetadata;
import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;
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
        public void consequenceTwo(Object object, long longValue, int intValue, KnowledgeHelper knowledgeHelper) {
            // Call this method just to get rid of unused-warnings
            performTask();
        }
    }
    private PojoRule pojo = new PojoRule();

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

    private static MethodMetadata METHOD_CONDITION_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONDITION);
    private static MethodMetadata METHOD_CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONSEQUENCE);

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

    public void testBuildRule() throws Exception {

        methodMetadataSourceExpectAndReturn(conditionOneMethod, METHOD_CONDITION_METADATA);
        argumentMetadataSourceExpectAndReturn(conditionOneMethod, conditionOneArgumentMetadata);

        methodMetadataSourceExpectAndReturn(conditionTwoMethod, METHOD_CONDITION_METADATA);
        argumentMetadataSourceExpectAndReturn(conditionTwoMethod, conditionTwoArgumentMetadata);

        methodMetadataSourceExpectAndReturn(consequenceOneMethod, METHOD_CONSEQUENCE_METADATA);
        argumentMetadataSourceExpectAndReturn(consequenceOneMethod, consequenceOneArgumentMetadata);

        methodMetadataSourceExpectAndReturn(consequenceTwoMethod, METHOD_CONSEQUENCE_METADATA);
        argumentMetadataSourceExpectAndReturn(consequenceTwoMethod, consequenceTwoArgumentMetadata);

        mocks.replay();

        builder.buildRule(rule, pojo);

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


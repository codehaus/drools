package org.drools.semantics.annotation.model;

import junit.framework.TestCase;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.base.ClassObjectType;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class TupleParameterValueFactoryTestCase extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private static final String BASE_DEFAULT_IDENTIFIER = TupleParameterValueFactory.BASE_DEFAULT_IDENTIFIER;

    private Mock<Rule> mockRule = mocks.createMock(Rule.class);
    private Mock<Declaration> mockDeclaration = mocks.createMock(Declaration.class);

    private static class TestObject extends Object {}

    private static class TestRule {
        public void identifierSpecified(@DroolsParameter("v") int value) {}
        public void identifierDefaultedPrimitive(@DroolsParameter int value) {}
        public void identifierDefaultedWrapper(@DroolsParameter Double value) {}
        public void identifierDefaultedObject(@DroolsParameter TestObject value) {}
    }

    private TupleParameterValueFactory factory = new TupleParameterValueFactory();

    protected void setUp() throws Exception {
        super.setUp();
    }

    private Annotation[] getParameterAnnotations(String methodName, Class parameterType) throws Exception {
        Method identifierDefaultedMethod = TestRule.class.getMethod(methodName, new Class[]{parameterType});
        return identifierDefaultedMethod.getParameterAnnotations()[0];
    }

    private String getExpectedIdentifier(Class parameterType) {
        return BASE_DEFAULT_IDENTIFIER + parameterType.getName();
    }

    //---- ---- ----

    public void testCreateIdentifierSpecifiedNotYetDeclared() throws Exception {
        ClassObjectType objectType = new ClassObjectType(int.class);

        mockRule.control.expectAndReturn(
                mockRule.object.getParameterDeclaration("v"),
                null);
        mockRule.control.expectAndReturn(
                mockRule.object.addParameterDeclaration("v", objectType),
                mockDeclaration.object);

        mocks.replay();

        ParameterValue parameterValue = factory.create(
                mockRule.object, int.class,
                getParameterAnnotations("identifierSpecified", int.class));

        mocks.verify();
    }

    public void testCreateIdentifierSpecifiedAlreadyDeclared() throws Exception {
        ClassObjectType objectType = new ClassObjectType(int.class);

        mockRule.control.expectAndReturn(
                mockRule.object.getParameterDeclaration("v"),
                mockDeclaration.object);

        mocks.replay();

        ParameterValue parameterValue = factory.create(
                mockRule.object, int.class,
                getParameterAnnotations("identifierSpecified", int.class));

        mocks.verify();
    }

    public void doTestDefaultIdentifierNotYetDeclared(
            String methodName, Class parameterType) throws Exception {

        ClassObjectType objectType = new ClassObjectType(parameterType);
        String expectedIdentifier = getExpectedIdentifier(parameterType);

        mockRule.control.expectAndReturn(
                mockRule.object.getParameterDeclaration(expectedIdentifier),
                null);
        mockRule.control.expectAndReturn(
                mockRule.object.addParameterDeclaration(expectedIdentifier, objectType),
                mockDeclaration.object);

        mocks.replay();

        ParameterValue parameterValue = factory.create(
                mockRule.object, parameterType,
                getParameterAnnotations(methodName, parameterType));

        mocks.verify();
    }

    public void doTestDefaultIdentifierAlreadyDeclared(
            String methodName, Class parameterType) throws Exception {

        String expectedIdentifier = getExpectedIdentifier(parameterType);

        mockRule.control.expectAndReturn(
                mockRule.object.getParameterDeclaration(expectedIdentifier),
                mockDeclaration.object);

        mocks.replay();

        ParameterValue parameterValue = factory.create(
                mockRule.object, parameterType,
                getParameterAnnotations(methodName, parameterType));

        mocks.verify();
    }

    public void testDefaultIdentifierPrimitiveNotYetDeclared() throws Exception {
        doTestDefaultIdentifierNotYetDeclared("identifierDefaultedPrimitive", int.class);
    }

    public void testDefaultIdentifierPrimitiveAlreadyDeclared() throws Exception {
        doTestDefaultIdentifierAlreadyDeclared("identifierDefaultedPrimitive", int.class);
    }

    public void testDefaultIdentifierWrapperNotYetDeclared() throws Exception {
        doTestDefaultIdentifierNotYetDeclared("identifierDefaultedWrapper", Double.class);
    }

    public void testDefaultIdentifierWrapperAlreadyDeclared() throws Exception {
        doTestDefaultIdentifierAlreadyDeclared("identifierDefaultedWrapper", Double.class);
    }

    public void testDefaultIdentifierObjectNotYetDeclared() throws Exception {
        doTestDefaultIdentifierNotYetDeclared("identifierDefaultedObject", TestObject.class);
    }

    public void testDefaultIdentifierObjectAlreadyDeclared() throws Exception {
        doTestDefaultIdentifierAlreadyDeclared("identifierDefaultedObject", TestObject.class);
    }
}

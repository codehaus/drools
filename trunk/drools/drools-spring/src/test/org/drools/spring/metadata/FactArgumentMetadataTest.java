package org.drools.spring.metadata;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spring.metadata.FactArgumentMetadata;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.FactArgument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

import junit.framework.TestCase;

public class FactArgumentMetadataTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl controlRule = mocks.createControl(Rule.class, new Class[]{String.class}, new Object[]{"for-test"});
    private Rule mockRule = (Rule) controlRule.getMock();

    Rule ruleDeclarationFactory = new Rule("for-test.declaration-factory");

    private Declaration createDeclaration(String identifier) throws Exception  {
        return ruleDeclarationFactory.addParameterDeclaration(identifier, null);
    }

    public void testNewWithNullParameterClass() {
        try {
            new FactArgumentMetadata("id", null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewWithNullIdentifier() {
        FactArgumentMetadata metadata = new FactArgumentMetadata(null, String.class);
        assertNull(metadata.getIdentifier());
        assertSame(String.class, metadata.getParameterClass());
    }

    public void testCreateArgumentExplicitIdentifierDeclarationNotYetDefined() throws Exception {
        final String explicitIdentifier = "explicit-identifier";
        FactArgumentMetadata metadata = new FactArgumentMetadata(explicitIdentifier, String.class);

        Declaration declaration = createDeclaration(explicitIdentifier);
        ClassObjectType objectType = new ClassObjectType(String.class);
        controlRule.expectAndReturn(mockRule.getParameterDeclaration(explicitIdentifier), null);
        controlRule.expectAndReturn(mockRule.addParameterDeclaration(explicitIdentifier, objectType), declaration);

        mocks.replay();

        Argument argument = metadata.createArgument(mockRule);

        mocks.verify();

        assertTrue(argument instanceof FactArgument);
        FactArgument factArgument = (FactArgument)argument;

        assertSame(declaration, factArgument.getDeclaration());
    }

    public void testCreateArgumentExplicitIdentifierDeclarationAlreadyDefined() throws Exception {
        final String explicitIdentifier = "explicit-identifier";
        FactArgumentMetadata metadata = new FactArgumentMetadata(explicitIdentifier, String.class);

        Declaration declaration = createDeclaration(explicitIdentifier);
        controlRule.expectAndReturn(mockRule.getParameterDeclaration(explicitIdentifier), declaration);

        mocks.replay();

        Argument argument = metadata.createArgument(mockRule);

        mocks.verify();

        assertTrue(argument instanceof FactArgument);
        FactArgument factArgument = (FactArgument)argument;

        assertSame(declaration, factArgument.getDeclaration());
    }

//    public void testCreateArgumentImplicitIdentifier() throws Exception {
//        final String implicitIdentifier = FactArgumentMetadata.getDefaultIdentifier(String.class);
//        FactArgumentMetadata factAttributes_1 = new FactArgumentMetadata(null, String.class);
//        FactArgumentMetadata factAttributes_2 = new FactArgumentMetadata(null, String.class);
//
//        Declaration declaration = createDeclaration(implicitIdentifier);
//        ClassObjectType objectType = new ClassObjectType(String.class);
//        controlRule.expectAndReturn(mockRule.getParameterDeclaration(implicitIdentifier), null);
//        controlRule.expectAndReturn(mockRule.addParameterDeclaration(implicitIdentifier, objectType), declaration);
//        controlRule.expectAndReturn(mockRule.getParameterDeclaration(implicitIdentifier), declaration);
//
//        mocks.replay();
//
//        Argument argument_1 = factAttributes_1.createArgument(mockRule);
//        Argument argument_2 = factAttributes_2.createArgument(mockRule);
//
//        mocks.verify();
//
//        assertTrue(argument_1 instanceof FactArgument);
//        FactArgument factArgument_1 = (FactArgument)argument_1;
//        assertSame(declaration, factArgument_1.getDeclaration());
//        assertTrue(argument_2 instanceof FactArgument);
//        FactArgument factArgument_2 = (FactArgument)argument_2;
//        assertSame(declaration, factArgument_2.getDeclaration());
//    }
}

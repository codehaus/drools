package org.drools.spring.metadata;

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



import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.FactArgument;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

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
        assertEquals(FactArgumentMetadata.getDefaultIdentifier(String.class), metadata.getIdentifier());
        assertSame(String.class, metadata.getParameterClass());
    }

    public void testCreateArgumentExplicitIdentifierDeclarationNotYetDefined() throws Exception {
        final String explicitIdentifier = "explicit-identifier";
        FactArgumentMetadata metadata = new FactArgumentMetadata(explicitIdentifier, String.class);

        Declaration declaration = createDeclaration(explicitIdentifier);
        BeanObjectType objectType = new BeanObjectType(String.class);
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


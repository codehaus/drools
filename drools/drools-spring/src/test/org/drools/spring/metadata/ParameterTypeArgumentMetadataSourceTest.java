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



import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;
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
            public Pojo() {
                consequence(null); // to eliminate non-used warning
            }
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
            public Pojo() {
                consequence(null); // to eliminate non-used warning
            }
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
            public Pojo() {
                consequence(null, null); // to eliminate non-used warning
            }
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
            public Pojo() {
                consequence(null, null, null); // to eliminate non-used warning
            }
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
            public Pojo() {
                consequence(null, null, null); // to eliminate non-used warning
            }
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


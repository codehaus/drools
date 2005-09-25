package org.drools.spring.metadata.annotation.java;

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


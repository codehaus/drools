package org.drools.semantics.groovy;

/*
 * $Id: GroovyBlockConsequence.java,v 1.4 2005-11-10 05:33:37 mproctor Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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

import java.io.Serializable;
import java.util.List;
import java.util.Map;


import org.drools.semantics.java.JavaBlockConsequence;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.smf.ConsequenceInvoker;
import org.drools.smf.Invoker;
import org.drools.smf.SemanticConsequence;
import org.drools.smf.SemanticInvokeable;
import org.drools.smf.SemanticRuleCompiler;
import org.drools.smf.SemanticComponent;
import org.drools.smf.SemanticRule;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

/**
 * Java block semantics <code>Consequence</code>.
 * 
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 */
public class GroovyBlockConsequence extends JavaBlockConsequence
    implements
    Serializable,
    Consequence,
    SemanticConsequence
{
    private final static String semanticType    = "groovy";

    protected final String      thrownException = null;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param block
     *            The statement block.
     * @param rule
     *            The rule.
     */
    public GroovyBlockConsequence(String name,
                                String block,
                                Rule rule) throws Exception
    {
        super( name, block, rule );
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    public String getSemanticType()
    {
        return semanticType;
    }

    public boolean isExceptionThrown()
    {
        return false;
    }

    public String getThrownException()
    {
        return this.thrownException;
    }
  
    public SemanticRuleCompiler getSemanticRuleCompiler()
    {
        return GroovySemanticRuleCompiler.getInstance();
    }
}

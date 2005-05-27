package org.drools.rule;

/*
 * $Id: ApplicationData.java,v 1.3.2.1 2005-05-10 12:11:25 mproctor Exp $
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

/**
 * <code>ApplicationData</code> allows data to be made accessible to
 * <code>Condition</code>s and <code>Consequence<code>s without the need to assert
 * the data. <code>ApplicationData</code> name and type must be declared to a <code>RuleBase</code>
 * before it can set in a <code>WorkingMemory</code> otherwise a RuntimeException will occur.
 * 
 * @author <a href="mailto:mproctor@codehaus.org"> mark proctor </a>
 *
 */
public class ApplicationData
    implements
    Serializable
{
    /** 
     * The identifier. This is the name that is used to reference the 
     * instance object inside the <code>Condition</code> or <code>Consequence</code>
     *  
     */
    private String  identifier;
    
    /** 
     * The declare type for the identifier. An instanceof check will be applied by the <class>WorkingMemory</class>
     * for this type against the one being set, if they differe a <code>RuntimeException</code> we be thrown.
     * */
    private Class   type;
    private RuleSet ruleSet;

    public ApplicationData(RuleSet ruleSet,
                           String identifier,
                           Class type)
    {
        this.identifier = identifier;
        this.type = type;
        this.ruleSet = ruleSet;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public Class getType()
    {
        return this.type;
    }
}

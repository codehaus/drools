package org.drools.spi;

/*
 * $Id: ConditionException.java,v 1.6.2.1 2005-05-08 00:57:36 mproctor Exp $
 * 
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 * 
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import org.drools.AssertionException;
import org.drools.rule.Rule;

/**
 * Indicates an error while applying a <code>FilterCondition<code>.
 *
 *  @see Condition
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class ConditionException extends AssertionException
{
    private Rule   rule;
    private String info;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    public ConditionException()
    {
        // intentionally left blank
    }

    public ConditionException(String message)
    {
        super( message );
    }

    /**
     * Construct with a root cause.
     * 
     * @param rootCause
     *        The root cause of this exception.
     */
    public ConditionException(Throwable rootCause)
    {
        super( rootCause );
    }

    public ConditionException(Rule rule)
    {
        this.rule = rule;
    }

    public ConditionException(String message,
                              Rule rule,
                              String info)
    {
        super( message );
        this.rule = rule;
        this.info = info;
    }

    /**
     * Construct with a root cause.
     * 
     * @param rootCause
     *        The root cause of this exception.
     */
    public ConditionException(Throwable rootCause,
                              Rule rule,
                              String info)
    {
        super( rootCause );
        this.rule = rule;
        this.info = info;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    /**
     * Set arbitrary extra information about the condition.
     * 
     * <p>
     * The info property may be used to communicate the actual expression text
     * or other information in the case that Condition does not have expression
     * text.
     * </p>
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return this.info;
    }
}

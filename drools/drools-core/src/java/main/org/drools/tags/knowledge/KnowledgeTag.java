package org.drools.tags.knowledge;

/*
 $Id: KnowledgeTag.java,v 1.1 2002-08-20 18:33:17 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import org.drools.WorkingMemory;
import org.drools.RuleBase;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

/** Work in a knowledge session.
 *
 *  @see WorkingMemory
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: KnowledgeTag.java,v 1.1 2002-08-20 18:33:17 bob Exp $
 */
public class KnowledgeTag extends TagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The variable. */
    private String var;

    /** Transactional flag. */
    private boolean transactional;

    /** The working memory. */
    private WorkingMemory workingMemory;

    /** The rule base. */
    private RuleBase ruleBase;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public KnowledgeTag()
    {
        this.transactional = false;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the transactionality flag.
     *
     *  @param transactional Transactionality flag.
     */
    public void setTransactional(boolean transactional)
    {
        this.transactional = transactional;
    }

    /** Retrieve the transactionality flag.
     *
     *  @return The transactionality flag.
     */
    public boolean getTransactional()
    {
        return this.transactional;
    }

    /** Set the variable in which to store the <code>WorkingMemory</code>.
     *
     *  @param var The variable name.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /** Retrieve the variable in which to store the <code>WorkingMemory</code>.
     *
     *  @return The variable name.
     */
    public String getVar()
    {
        return this.var;
    }

    /** Set the <code>WorkingMemory</code>.
     *
     *  @param memory The working memory.
     */
    public void setMemory(WorkingMemory memory)
    {
        this.workingMemory = memory;
    }

    /** Retrieve the <code>WorkingMemory</code>.
     *
     *  @return The working memory.
     */
    public WorkingMemory getMemory()
    {
        return this.workingMemory;
    }

    /** Set the <code>RuleBase</code>.
     *
     *  @param ruleBase The rule-base.
     */
    public void setRuleBase(RuleBase ruleBase)
    {
        this.ruleBase = ruleBase;
    }

    /** Retrieve the <code>RuleBase</code>.
     *
     *  @return The rule-base.
     */
    public RuleBase getRuleBase()
    {
        return this.ruleBase;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.apache.commons.jelly.Tag
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    
    /** Perform this tag.
     *
     *  @param output The output sink.
     *
     *  @throws Exception If an error occurs while attempting
     *          to perform this tag.
     */
    public void doTag(XMLOutput output) throws Exception
    {
        if (  this.workingMemory == null 
              &&
              this.ruleBase == null ) 
        {
            throw new JellyException( "Either 'memory' or 'rulebase' attribute must be specified" );
        }

        if ( this.workingMemory == null )
        {
            if ( this.transactional )
            {
                this.workingMemory = this.ruleBase.createTransactionalWorkingMemory();
            }
            else
            {
                this.workingMemory = this.ruleBase.createWorkingMemory();
            }
        }

        invokeBody( output );
    }
}

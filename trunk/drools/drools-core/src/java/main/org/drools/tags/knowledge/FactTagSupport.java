package org.drools.tags.knowledge;

/*
 $Id: FactTagSupport.java,v 1.4 2003-03-25 19:47:32 tdiesler Exp $

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

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.drools.WorkingMemory;

/** Support for fact tags.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: FactTagSupport.java,v 1.4 2003-03-25 19:47:32 tdiesler Exp $
 */
public abstract class FactTagSupport extends TagSupport
{
    // ------------------------------------------------------------
    //     Instance mebers
    // ------------------------------------------------------------

    /** The fact object. */
    private Object fact;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected FactTagSupport()
    {
        super( true );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the fact object.
     *
     *  @param fact The fact object.
     */
    public void setFact(Object fact)
    {
        this.fact = fact;
    }

    /** Retrieve the fact object.
     *
     *  @return The fact object.
     */
    public Object getFact()
    {
        return this.fact;
    }

    /** Validate required attributes.
     *
     *  @throws MissingAttributeException If a required attribute is missing.
     */
    protected void validateAttributes() throws MissingAttributeException
    {
        if ( this.fact == null )
        {
            throw new MissingAttributeException( "fact" );
        }
    }

    /** Retrieve the current <code>WorkingMemory</code>.
     *
     *  @return The working memory.
     *
     *  @throws JellyTagException If a working memory cannot be retrieved.
     */
    protected WorkingMemory getWorkingMemory() throws JellyTagException
    {
        WorkingMemory memory = (WorkingMemory) getContext().getVariable( "org.drools.working-memory" );

        if ( memory == null )
        {
            KnowledgeTag tag = (KnowledgeTag) findAncestorWithClass( KnowledgeTag.class );
            
            if ( tag == null )
            {
                throw new JellyTagException( "No working memory available" );
            }
            
            return tag.getMemory();
        }

        return memory;
    }
}

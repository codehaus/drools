package org.drools.smf;

/*
 $Id: SemanticComponentTagSupport.java,v 1.1 2002-08-02 19:43:11 bob Exp $

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

import org.apache.commons.jelly.MissingAttributeException;

/** Support for semantic components.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public abstract class SemanticComponentTagSupport extends SmfTagSupport 
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The component name. */
    private String name;

    /** The component class name. */
    private String classname;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public SemanticComponentTagSupport()
    {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the component name.
     *
     *  @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Retrieve the component name.
     *
     *  @return The component name.
     */
    public String getName()
    {
        return this.name;
    }

    /** Set the component class name.
     *
     *  @param classname The component class name.
     */
    public void setClassname(String classname)
    {
        this.classname = classname;
    }

    /** Retrieve the component class name.
     *
     *  @return The component class name.
     */
    public String getClassname()
    {
        return this.classname;
    }

    /** Check if the attributes have been set.
     *
     *  @throws MissingAttributeException If the attributes have not been set.
     */
    public void checkAttributes() throws MissingAttributeException
    {
        if ( this.classname == null )
        {
            throw new MissingAttributeException( "classname" );
        }

        if ( this.name == null )
        {
            throw new MissingAttributeException( "name" );
        }
    }

    /** Retrieve the current <code>SemanticModule</code>.
     *
     *  @return The current <code>SemanticModule</code> or
     *          <code>null</code> if none found.
     */
    public SimpleSemanticModule getCurrentSemanticModule()
    {
        SemanticModuleTag tag = (SemanticModuleTag) findAncestorWithClass( SemanticModuleTag.class );

        if ( tag == null )
        {
            return null;
        }

        return tag.getSemanticModule();
    }
}


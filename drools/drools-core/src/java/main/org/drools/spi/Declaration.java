package org.drools.spi;

/*
 $Id: Declaration.java,v 1.4 2002-07-27 01:50:17 bob Exp $

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

/** A typed, named variable for {@link Condition} evaluation.
 *
 *  @see ObjectType
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class Declaration
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The type of the variable. */
    private ObjectType objectType;

    /** The identifier for the variable. */
    private String     identifier;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param objectType The type of this variable declaration.
     *  @param identifier The name of the variable.
     */
    public Declaration(ObjectType objectType,
                       String identifier)
    {
        this.objectType = objectType;
        this.identifier = identifier;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>ObjectType</code> of this <code>Declaration</code>.
     *
     *  @return The <code>ObjectType</code> of this <code>Declaration</code>.
     */
    public ObjectType getObjectType()
    {
        return this.objectType;
    }

    /** Retrieve the variable's identifier.
     *
     *  @return The variable's identifier.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Determine if another <code>Declaration</code> is
     *  <b>semantically</b> equivelent to this one.
     *
     *  @param thatObj The object to compare to.
     *
     *  @return <code>true</code> if <code>thatObj</code> is
     *          semantically equal to this object.
     */
    public boolean equals(Object thatObj)
    {
        Declaration that = (Declaration) thatObj;

        return ( this.objectType.equals( that.objectType )
                 &&
                 this.identifier.equals( that.identifier ) );
    }

    /** Produce debug string.
     *
     *  @return debug string.
     */
    public String toString()
    {
        return "[Declaration: identifier='" + getIdentifier() + "'; objectType=" + getObjectType() + "]";
    }
}

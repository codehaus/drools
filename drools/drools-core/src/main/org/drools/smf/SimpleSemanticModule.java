package org.drools.smf;

/*
 $Id: SimpleSemanticModule.java,v 1.3 2002-08-13 04:12:26 bob Exp $

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

import org.drools.spi.ObjectType;
import org.drools.spi.FactExtractor;
import org.drools.spi.Action;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/** Simple implementation of a Semantic Module.
 *
 *  @see org.drools.spi
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class SimpleSemanticModule implements SemanticModule
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** URI of this module. */
    private String uri;

    /** Object type implementations. */
    private Map objectTypes;

    /** Fact extractor implementations. */
    private Map factExtractors;

    /** Action implementations. */
    private Map actions;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct with a URI.
     *
     *  @param uri The URI the identifies this semantic module.
     */
    public SimpleSemanticModule(String uri)
    {
        this.uri = uri;

        this.objectTypes    = new HashMap();
        this.factExtractors = new HashMap();
        this.actions        = new HashMap();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the URI that identifies this semantic module.
     *
     *  @return The URI.
     */
    public String getUri()
    {
        return this.uri;
    }

    /** Set the URI.
     *
     *  @param uri The URI.
     */
    void setUri(String uri)
    {
        this.uri = uri;
    }

    /** Add a semantic object type.
     *
     *  @param name The object type name.
     *  @param objectType The object type implementation.
     *
     *  @throws InvalidObjectTypeException If a class that is not a
     *          object type is added.
     */
    public void addObjectType(String name,
                              Class objectType) throws InvalidObjectTypeException
    {
        if ( ! ObjectType.class.isAssignableFrom( objectType ) )
        {
            throw new InvalidObjectTypeException( objectType );
        }

        this.objectTypes.put( name,
                              objectType );
    }

    /** Retrieve a semantic object type by name.
     *
     *  @param name the name.
     *
     *  @return The object type implementation or <code>null</code>
     *          if none is bound to the name.
     */   
    public Class getObjectType(String name)
    {
        return (Class) this.objectTypes.get( name );
    }

    /** Retrieve the set of all object type names.
     *
     *  @return The set of names.
     */
    public Set getObjectTypeNames()
    {
        return this.objectTypes.keySet();
    }

    /** Add a semantic object type.
     *
     *  @param name The object type name.
     *  @param factExtractor The object type implementation.
     *
     *  @throws InvalidFactExtractorException If a class that is not a
     *          object type is added.
     */
    public void addFactExtractor(String name,
                                 Class factExtractor) throws InvalidFactExtractorException
    {
        if ( ! FactExtractor.class.isAssignableFrom( factExtractor ) )
        {
            throw new InvalidFactExtractorException( factExtractor );
        }

        this.factExtractors.put( name,
                                 factExtractor );
    }

    /** Retrieve a semantic fact extractor by name.
     *
     *  @param name the name.
     *
     *  @return The fact extractor implementation or <code>null</code>
     *          if none is bound to the name.
     */
    public Class getFactExtractor(String name)
    {
        return (Class) this.factExtractors.get( name );
    }

    /** Retrieve the set of all object type names.
     *
     *  @return The set of names.
     */
    public Set getFactExtractorNames()
    {
        return this.factExtractors.keySet();
    }

    /** Add a semantic action.
     *
     *  @param name The action name.
     *  @param action The action implementation.
     *
     *  @throws InvalidActionException If a class that is not a
     *          action is added.
     */
    public void addAction(String name,
                          Class action) throws InvalidActionException
    {
        if ( ! Action.class.isAssignableFrom( action ) )
        {
            throw new InvalidActionException( action );
        }

        this.actions.put( name,
                          action );
    }

    /** Retrieve a semantic action by name.
     *
     *  @param name the name.
     *
     *  @return The action implementation or <code>null</code>
     *          if none is bound to the name.
     */
    public Class getAction(String name)
    {
        return (Class) this.actions.get( name );
    }

    /** Retrieve the set of all object type names.
     *
     *  @return The set of names.
     */
    public Set getActionNames()
    {
        return this.actions.keySet();
    }
}

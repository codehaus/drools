package org.drools.smf;

/*
 $Id: SimpleSemanticModule.java,v 1.8 2003-11-19 21:31:11 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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
import org.drools.spi.Condition;
import org.drools.spi.Extractor;
import org.drools.spi.Consequence;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/** Simple implementation of a Semantic Module.
 *
 *  @see org.drools.spi
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class SimpleSemanticModule
    implements SemanticModule
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** URI of this module. */
    private String uri;

    /** Object type implementations. */
    private Map objectTypes;

    /** Condition implementations. */
    private Map conditions;

    /** Extractor implementations. */
    private Map extractors;

    /** Consequence implementations. */
    private Map consequences;

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

        this.objectTypes  = new HashMap();
        this.conditions   = new HashMap();
        this.extractors   = new HashMap();
        this.consequences = new HashMap();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** @see SemanticModule
     */
    public String getUri()
    {
        return this.uri;
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

    /** @see SemanticModule
     */
    public Class getObjectType(String name)
    {
        return (Class) this.objectTypes.get( name );
    }

    /** @see SemanticModule
     */
    public Set getObjectTypeNames()
    {
        return this.objectTypes.keySet();
    }

    /** Add a semantic condition.
     *
     *  @param name The condition name.
     *  @param condition The condition implementation.
     *
     *  @throws InvalidConditionException If a class that is not a
     *          condition is added.
     */
    public void addCondition(String name,
                             Class condition) throws InvalidConditionException
    {
        if ( ! Condition.class.isAssignableFrom( condition ) )
        {
            throw new InvalidConditionException( condition );
        }

        this.conditions.put( name,
                             condition );
    }

    /** @see SemanticModule
     */
    public Class getCondition(String name)
    {
        return (Class) this.conditions.get( name );
    }

    /** @see SemanticModule
     */
    public Set getConditionNames()
    {
        return this.conditions.keySet();
    }

    /** Add a semantic extractor.
     *
     *  @param name The extractor name.
     *  @param extractor The extractor implementation.
     *
     *  @throws InvalidExtractorException If a class that is not a
     *          extractor is added.
     */
    public void addExtractor(String name,
                             Class extractor) throws InvalidExtractorException
    {
        if ( ! Extractor.class.isAssignableFrom( extractor ) )
        {
            throw new InvalidExtractorException( extractor );
        }

        this.extractors.put( name,
                             extractor );
    }

    /** @see SemanticModule
     */
    public Class getExtractor(String name)
    {
        return (Class) this.extractors.get( name );
    }

    /** @see SemanticModule
     */
    public Set getExtractorNames()
    {
        return this.extractors.keySet();
    }

    /** Add a semantic consequence.
     *
     *  @param name The consequence name.
     *  @param consequence The consequence implementation.
     *
     *  @throws InvalidConsequenceException If a class that is not a
     *          consequence is added.
     */
    public void addConsequence(String name,
                          Class consequence) throws InvalidConsequenceException
    {
        if ( ! Consequence.class.isAssignableFrom( consequence ) )
        {
            throw new InvalidConsequenceException( consequence );
        }

        this.consequences.put( name,
                          consequence );
    }

    /** @see SemanticModule
     */
    public Class getConsequence(String name)
    {
        return (Class) this.consequences.get( name );
    }

    /** @see SemanticModule
     */
    public Set getConsequenceNames()
    {
        return this.consequences.keySet();
    }
}

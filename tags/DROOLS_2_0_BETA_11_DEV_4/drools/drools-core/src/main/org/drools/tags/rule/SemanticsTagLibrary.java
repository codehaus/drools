package org.drools.tags.rule;

/*
 $Id: SemanticsTagLibrary.java,v 1.7 2003-03-25 19:47:32 tdiesler Exp $

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
import org.apache.commons.jelly.impl.DynamicTagLibrary;
import org.drools.smf.SemanticModule;

import java.util.Iterator;
import java.util.Set;

/** Dyanmic tag library for loading <code>SemanticModule</code>s.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: SemanticsTagLibrary.java,v 1.7 2003-03-25 19:47:32 tdiesler Exp $
 */
class SemanticsTagLibrary extends DynamicTagLibrary
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The module. */
    private SemanticModule module;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param module The module to make available as a taglib.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to initialize the module as a tag library.
     */
    SemanticsTagLibrary(SemanticModule module) throws JellyTagException
    {
        super( module.getUri() );
        this.module = module;

        registerObjectTypes();
        registerConditions();
        registerExtractors();
        registerConsequences();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>SemanticModule</code>.
     *
     *  @return The semantic module.
     */
    protected SemanticModule getSemanticModule()
    {
        return this.module;
    }

    /** Register <code>ObjectType</code>s.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register this module's object-types.
     */
    protected void registerObjectTypes() throws JellyTagException
    {
        Set names = getSemanticModule().getObjectTypeNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getObjectType( eachName );

            registerObjectType( eachName,
                                eachClass );
        }
    }

    /** Register an <code>ObjectType</code>.
     *
     *  @param name The name.
     *  @param beanClass The class.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register the object-type.
     */
    protected void registerObjectType(final String name,
                                      final Class beanClass) throws JellyTagException
    {
        registerBeanTag( name,
                         new ComponentTagFactory( beanClass,
                                                  ObjectTypeComponentTag.class ) );
    }

    /** Register <code>Condition</code>s.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register this module's conditions.
     */
    protected void registerConditions() throws JellyTagException
    {
        Set names = getSemanticModule().getConditionNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getCondition( eachName );

            registerCondition( eachName,
                                eachClass );
        }
    }

    /** Register an <code>Condition</code>.
     *
     *  @param name The name.
     *  @param beanClass The class.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register the condition.
     */
    protected void registerCondition(final String name,
                                     final Class beanClass) throws JellyTagException
    {
        registerBeanTag( name,
                         new ComponentTagFactory( beanClass,
                                                  ConditionComponentTag.class ) );
    }

    /** Register <code>Extractors</code>s.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register this module's extractors.
     */
    protected void registerExtractors() throws JellyTagException
    {
        Set names = getSemanticModule().getExtractorNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getExtractor( eachName );

            registerExtractor( eachName,
                               eachClass );
        }
    }

    /** Register an <code>Extractor</code>.
     *
     *  @param name The name.
     *  @param beanClass The class.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register the extractor.
     */
    protected void registerExtractor(final String name,
                                     final Class beanClass) throws JellyTagException
    {
        registerBeanTag( name,
                         new ComponentTagFactory( beanClass,
                                                  ExtractorComponentTag.class ) );
    }

    /** Register <code>Consequence</code>s.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register this module's consequences.
     */
    protected void registerConsequences() throws JellyTagException
    {
        Set names = getSemanticModule().getConsequenceNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getConsequence( eachName );

            registerConsequence( eachName,
                                 eachClass );
        }
    }

    /** Register an <code>Consequence</code>.
     *
     *  @param name The name.
     *  @param beanClass The class.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to register the consequence.
     */
    protected void registerConsequence(final String name,
                                       final Class beanClass) throws JellyTagException
    {
        registerBeanTag( name,
                         new ComponentTagFactory( beanClass,
                                                  ConsequenceComponentTag.class ) );
    }
}

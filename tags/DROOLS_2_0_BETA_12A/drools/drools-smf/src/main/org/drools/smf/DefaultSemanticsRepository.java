package org.drools.smf;

/*
 $Id: DefaultSemanticsRepository.java,v 1.1.1.1 2003-12-30 21:19:59 bob Exp $

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

import java.net.URL;
import java.util.Enumeration;

/** Default <code>SemanticsRepository</code> which uses discovery to autoload
 *  semantic modules.
 *
 *  <p>
 *  Any <b>semantic module</b> that conforms to the SMF contract will be
 *  discovered and autoloaded upon first access of the <code>DefaultSemanticsRepository</code>.
 *  </p>
 *
 *  <p>
 *  To be discovered, the module should be in a jar with a module descriptor
 *  located at <code>/META-INF/drools-semantics.properties</code>.
 *  </p>
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: DefaultSemanticsRepository.java,v 1.1.1.1 2003-12-30 21:19:59 bob Exp $
 */
public final class DefaultSemanticsRepository
    implements SemanticsRepository
{
    // ----------------------------------------------------------------------
    //     Class members
    // ----------------------------------------------------------------------

    /** Singleton instance, lazily initialized. */
    private static SemanticsRepository INSTANCE = null;

    // ----------------------------------------------------------------------
    //     Class methods
    // ----------------------------------------------------------------------

    /** Retrieve the singleton instance.
     *
     *  @return The singleton instance.
     *
     *  @throws Exception If an error occurs while performing discovery
     *          and loading of the semantic modules.
     */
    public static synchronized SemanticsRepository getInstance()
        throws Exception
    {
        if ( INSTANCE == null )
        {
            INSTANCE = new DefaultSemanticsRepository();
        }

        return INSTANCE;
    }

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Module repository. */
    private SimpleSemanticsRepository repository;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @throws Exception If an error occurs while performing discovery
     *          and loading of the semantic modules.
     */
    private DefaultSemanticsRepository()
        throws Exception
    {
        this.repository = new SimpleSemanticsRepository();
        init();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Initialize and perform discovery.
     *
     *  @throws Exception If an error occurs while performing discovery
     *          and loading of the semantic modules.
     */
    protected void init()
        throws Exception
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Enumeration moduleDescriptors = cl.getResources( "META-INF/drools-semantics.properties" );
        
        SemanticsReader semanticsReader = new SemanticsReader();
        
        while ( moduleDescriptors.hasMoreElements() )
        {
            URL moduleDescriptor = (URL) moduleDescriptors.nextElement();

            SemanticModule module = semanticsReader.read( moduleDescriptor );
            
            this.repository.registerSemanticModule( module );
        }
    }

    /** @see SemanticsRepository
     */
    public SemanticModule lookupSemanticModule(String uri)
        throws NoSuchSemanticModuleException
    {
        return this.repository.lookupSemanticModule( uri );
    }

    /** @see SemanticsRepository
     */
    public SemanticModule[] getSemanticModules()
    {
        return this.repository.getSemanticModules();
    }
}

package org.drools.jsr94.rules;

/*
 * $Id: Jsr94FactHandleFactory.java,v 1.13 2004-11-27 00:59:54 dbarnett Exp $
 *
 * Copyright 2003-2004 (C) The Werken Company. All Rights Reserved.
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

import org.drools.FactHandle;
import org.drools.reteoo.FactHandleFactory;

/**
 * A factory for creating <code>Handle</code>s.
 */
public final class Jsr94FactHandleFactory implements FactHandleFactory
{
    /** The Singleton instance of the <code>Jsr94FactHandleFactory</code>. */
    private static Jsr94FactHandleFactory INSTANCE;

    /** Counter for generating unique <code>Handle</code> ids. */
    private long idCounter;

    /** Counter for generating successive recency values. */
    private long recencyCounter;

    /** Private constructor; use <code>getInstance</code> instead. */
    private Jsr94FactHandleFactory( )
    {
        // Hide the constructor.
    }

    /**
     * Gets the Singleton instance of a <code>Jsr94FactHandleFactory</code>.
     *
     * @return The Singleton instance of the repository.
     */
    public static synchronized Jsr94FactHandleFactory getInstance( )
    {
        if ( Jsr94FactHandleFactory.INSTANCE != null )
        {
            return Jsr94FactHandleFactory.INSTANCE;
        }
        return Jsr94FactHandleFactory.INSTANCE = new Jsr94FactHandleFactory( );
    }

    /**
     * Returns a new <code>Handle</code>.
     *
     * @return a new <code>Handle</code>.
     */
    public synchronized FactHandle newFactHandle( )
    {
        return new Jsr94FactHandle( ++this.idCounter,  ++this.recencyCounter );
    }

    /**
     * Returns a new <code>Handle</code>.
     *
     * @param id A unique <code>Handle</code> id.
     *
     * @return a new <code>Handle</code> with the given id.
     */
    public synchronized FactHandle newFactHandle( long id )
    {
        return new Jsr94FactHandle( id, ++this.recencyCounter );
    }
}

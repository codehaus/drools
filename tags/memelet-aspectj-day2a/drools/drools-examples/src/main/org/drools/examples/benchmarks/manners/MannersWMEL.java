package org.drools.examples.benchmarks.manners;

/*
 * $Id: MannersWMEL.java,v 1.1 2004-12-15 03:31:26 dbarnett Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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

import java.util.HashSet;
import java.util.Set;

import org.drools.event.DefaultWorkingMemoryEventListener;
import org.drools.event.ObjectAssertedEvent;
import org.drools.examples.benchmarks.manners.model.Chosen;
import org.drools.examples.benchmarks.manners.model.Path;

/**
 * A <code>WorkingMemoryEventListener</code> used to simulate "not"
 * functionality not currently supported by Drools.
 */
public class MannersWMEL extends DefaultWorkingMemoryEventListener
{
    /**
     * The <code>Set</code> of <code>Path</code> objects
     * asserted into the <code>WorkingMemory</code>.
     */
    private static Set pathSet = new HashSet( 512 );
    
    /**
     * The <code>Set</code> of <code>Chosen</code> objects
     * asserted into the <code>WorkingMemory</code>.
     */
    private static Set chosenSet = new HashSet( 512 );
    
    /**
     * Keeps track of asserted <code>Path</code>
     * and <code>Chosen</code> objects.
     */
    public void objectAsserted( ObjectAssertedEvent event )
    {
        Object object = event.getObject( );
        if ( object instanceof Path )
        {
            Path path = ( Path ) object;
            pathSet.add( path.getId( ) + ':' + path.getName( ) );
        }
        else if ( object instanceof Chosen )
        {
            Chosen chosen = ( Chosen ) object;
            chosenSet.add( chosen.getId( ) + ':' + chosen.getName( ) + ':' + chosen.getHobby( ) );
        }
    }
    
    /**
     * Returns <code>true</code> if a <code>Path</code>
     * with the given id and name has already been asserted.
     */
    public static boolean pathExists( int id, String name )
    {
        return pathSet.contains( id + ':' + name );
    }
    
    /**
     * Returns <code>true</code> if a <code>Chosen</code>
     * with the given id, name, and hobby has already been asserted.
     */
    public static boolean chosenExists( int id, String name, String hobby )
    {
        return chosenSet.contains( id + ':' + name + ':' + hobby );
    }
}

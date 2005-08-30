package org.drools.examples.benchmarks.waltz;

/*
 * $Id: WaltzWMEL.java,v 1.1 2004-12-15 15:13:41 dbarnett Exp $
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
import org.drools.event.ObjectModifiedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.examples.benchmarks.waltz.model.Edge;
import org.drools.examples.benchmarks.waltz.model.Junction;
import org.drools.examples.benchmarks.waltz.model.Line;

/**
 * A <code>WorkingMemoryEventListener</code> used to simulate "not"
 * functionality not currently supported by Drools.
 */
public class WaltzWMEL extends DefaultWorkingMemoryEventListener
{
    private static int lineCount;
    private static int joinedEdgeCount;
    private static int plottedEdgeCount;
    private static Set edgeSet = new HashSet( 512 );
    private static int maxJunctionBasePoint = -1;
    private static int minJunctionBasePoint = Integer.MAX_VALUE;

    public void objectAsserted( ObjectAssertedEvent event )
    {
        Object object = event.getObject( );
        if ( object instanceof Line )
        {
            ++lineCount;
        }
        else if ( object instanceof Edge )
        {
            Edge edge = ( Edge ) object;
            if ( edge.isJoined( ) )
            {
                ++joinedEdgeCount;
            }
            if ( edge.isPlotted( ) )
            {
                ++plottedEdgeCount;
            }
            edgeSet.add( edge.getP1( ) + ":" + edge.getP2( ) );
        }
        else if ( object instanceof Junction )
        {
            Junction junction = ( Junction ) object;
            int basePoint = junction.getBasePoint( );
            if ( basePoint > maxJunctionBasePoint )
            {
                maxJunctionBasePoint = basePoint;
            }
            if ( basePoint < minJunctionBasePoint )
            {
                minJunctionBasePoint = basePoint;
            }
        }
    }

    public void objectModified( ObjectModifiedEvent event )
    {
        Object oldObject = event.getOldObject( );
        if ( oldObject instanceof Line )
        {
            --lineCount;
        }
        else if ( oldObject instanceof Edge )
        {
            Edge edge = ( Edge ) oldObject;
            if ( edge.isJoined( ) )
            {
                --joinedEdgeCount;
            }
            if ( edge.isPlotted( ) )
            {
                --plottedEdgeCount;
            }
        }

        Object newObject = event.getObject( );
        if ( newObject instanceof Line )
        {
            ++lineCount;
        }
        else if ( newObject instanceof Edge )
        {
            Edge edge = ( Edge ) newObject;
            if ( edge.isJoined( ) )
            {
                ++joinedEdgeCount;
            }
            if ( edge.isPlotted( ) )
            {
                ++plottedEdgeCount;
            }
        }
    }

    public void objectRetracted( ObjectRetractedEvent event )
    {
        Object object = event.getOldObject();
        if ( object instanceof Line )
        {
            --lineCount;
        }
        else if ( object instanceof Edge )
        {
            Edge edge = ( Edge ) object;
            if ( edge.isJoined( ) )
            {
                --joinedEdgeCount;
            }
            if ( edge.isPlotted( ) )
            {
                --plottedEdgeCount;
            }
        }
    }

    public static boolean lineExists( )
    {
        return lineCount != 0;
    }

    public static boolean edgeExists( int p1, int p2a, int p2b )
    {
        if ( edgeSet.contains( p1 + ":" + p2a ) )
        {
            return true;
        }

        return ( edgeSet.contains( p1 + ":" + p2b ) );
    }

    public static boolean joinedEdgeExists( )
    {
        return joinedEdgeCount != 0;
    }

    public static boolean plottedEdgeExists( )
    {
        return plottedEdgeCount != 0;
    }

    public static boolean greaterJunctionExists( int basePoint )
    {
        return ( basePoint != maxJunctionBasePoint );
    }

    public static boolean smallerJunctionExists( int basePoint )
    {
        return ( basePoint != minJunctionBasePoint );
    }
}

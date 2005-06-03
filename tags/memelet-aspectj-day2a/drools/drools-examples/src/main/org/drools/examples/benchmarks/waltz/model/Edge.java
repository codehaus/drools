package org.drools.examples.benchmarks.waltz.model;

/*
 * $Id: Edge.java,v 1.1 2004-12-15 15:13:42 dbarnett Exp $
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

/**
 * Edges are like lines except that they can be joined,
 * permanently labelled and plotted.
 *
 * (literalize edge p1 p2 joined label plotted)
 */
public class Edge
{
    private int p1;
    private int p2;
    private boolean joined;
    private String label;
    private boolean plotted;

    public Edge( int p1, int p2, boolean joined )
    {
        this.p1 = p1;
        this.p2 = p2;
        this.joined = joined;
        this.label = null;
        this.plotted = false;
    }

    public int getP1( )
    {
        return this.p1;
    }

    public int getP2( )
    {
        return this.p2;
    }

    public boolean isJoined( )
    {
        return this.joined;
    }

    public String getLabel( )
    {
        return this.label;
    }

    public boolean isPlotted( )
    {
        return this.plotted;
    }

    public void setJoined( boolean joined )
    {
        this.joined = joined;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }

    public void setPlotted( boolean plotted )
    {
        this.plotted = plotted;
    }

    public String toString( )
    {
        return "Edge[p1=" + this.p1 + ", p2=" + this.p2 + ", label=" + this.label
            + ", joined=" + this.joined + ", plotted=" + this.plotted + "]";
    }
}

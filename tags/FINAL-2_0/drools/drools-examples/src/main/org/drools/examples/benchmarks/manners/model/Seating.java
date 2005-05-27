package org.drools.examples.benchmarks.manners.model;

/*
 * $Id: Seating.java,v 1.1 2004-12-15 03:31:26 dbarnett Exp $
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
 * (deftemplate seating
 *   (slot id) (slot pid)
 *   (slot seat1) (slot name1)
 *   (slot name2) (slot seat2)
 *   (slot path_done))
 */
public class Seating
{
    private int id;
    private int pid;
    private int seat1;
    private int seat2;
    private String name1;
    private String name2;
    private boolean pathDone;

    public Seating( int id, int pid, int seat1, String name1, int seat2, String name2, boolean pathDone )
    {
        this.id = id;
        this.pid = pid;
        this.seat1 = seat1;
        this.name1 = name1;
        this.seat2 = seat2;
        this.name2 = name2;
        this.pathDone = pathDone;
    }

    public int getId( )
    {
        return this.id;
    }
    
    public int getPid( )
    {
        return this.pid;
    }
    
    public int getSeat1( )
    {
        return this.seat1;
    }

    public int getSeat2( )
    {
        return this.seat2;
    }

    public String getName1( )
    {
        return this.name1;
    }

    public String getName2( )
    {
        return this.name2;
    }

    public void setName2( String name2 )
    {
        this.name2 = name2;
    }
    
    public boolean isPathDone( )
    {
        return this.pathDone;
    }

    public void setPathDone( boolean pathDone )
    {
        this.pathDone = pathDone;
    }
    
    public String toString( )
    {
        return "Seating["
            + "id=" + this.id + ",pid=" + this.pid + ","
            + "seat1=" + this.seat1 + ",name1=" + this.name1 + ","
            + "seat2=" + this.seat2 + ",name2=" + this.name2 + ","
            + "pathDone=" + this.pathDone
            + "]";
    }
}

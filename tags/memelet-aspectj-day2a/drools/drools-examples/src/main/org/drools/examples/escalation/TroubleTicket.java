package org.drools.examples.escalation;

/*
 * $Id: TroubleTicket.java,v 1.3 2005-05-08 04:22:34 dbarnett Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

public class TroubleTicket
{
    public static final int NEW      = 1;

    public static final int NOTIFIED = 2;

    public static final int NULL     = 3;

    private String          submitter;

    private int             status;

    public TroubleTicket(String submitter)
    {
        this.submitter = submitter;
        this.status = NEW;
    }

    public String getSubmitter()
    {
        return this.submitter;
    }

    public int getStatus()
    {
        return this.status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer( );

        buf.append( "[TroubleTicket: submitter='" );
        buf.append( getSubmitter( ) );
        buf.append( "'; status='" );

        switch ( getStatus( ) )
        {
            case ( NEW ) :
            {
                buf.append( "NEW" );
                break;
            }
            case ( NOTIFIED ) :
            {
                buf.append( "NOTIFIED" );
                break;
            }
            case ( NULL ) :
            {
                buf.append( "/dev/null" );
                break;
            }
        }

        buf.append( "']" );

        return buf.toString( );
    }
}

package org.drools;

/*
 $Id: DroolsException.java,v 1.5 2002-07-27 05:55:59 bob Exp $

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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Locale;

/** Base <code>drools Logic Engine</code> exception.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class DroolsException extends Exception
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Root cause, if any. */
    private Throwable rootCause;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public DroolsException()
    {
    }

    /** Construct with a root cause.
     *
     *  @param rootCause The root cause of this exception.
     */
    public DroolsException(Throwable rootCause)
    {
        this.rootCause = rootCause;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Get the root cause, if any.
     *
     *  @return The root cause of this exception, as a
     *          <code>Throwable</code>, if this exception
     *          has a root cause, else <code>null</code>.
     */
    public Throwable getRootCause()
    {
        return this.rootCause;
    }

    /** Retrieve the message for this exception, 
     *  ignoring any root cause.
     *
     *  @return The message.
     */
    public String getMyMessage()
    {
        String msg = super.getMessage();

        if ( msg == null
             ||
             msg.equals( "" ) )
        {
            msg = getClass().getName();
        }

        return msg;
    }

    /** Retrieve the message for this exception, localized
     *  to the default locale, ignoring any root cause.
     *
     *  @return The localized message.
     */
    public String getMyLocalizedMessage()
    {
        String msg = super.getLocalizedMessage();

        if ( msg == null
             ||
             msg.equals( "" ) )
        {
            msg = getClass().getName();
        }

        return msg;
    }

    /** Retrieve the message for this exception, localized
     *  to a specific locale, ignoring any root cause.
     *
     *  @param locale The locale
     *
     *  @return The localized message.
     */
    public String getMyLocalizedMessage(Locale locale)
    {
        return getMyLocalizedMessage();
    }


    /** Print this exception's stack trace, ignoring
     *  any root cause.
     */
    public void printMyStackTrace()
    {
        super.printStackTrace();
    }

    /** Print this exception's stack trace, ignoring
     *  any root cause.
     *
     *  @param s The output sink.
     */
    public void printMyStackTrace(PrintStream s)
    {
        super.printStackTrace( s );
    }

    /** Print this exception's stack trace, ignoring
     *  any root cause.
     *
     *  @param s The output sink.
     */
    public void printMyStackTrace(PrintWriter s)
    {
        super.printStackTrace( s );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Exception
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the error message.
     *
     *  @return The error message.
     */
    public String getMessage()
    {
        Throwable rootCause = getRootCause();

        if ( rootCause == null )
        {
            return getMyMessage();
        }

        StringBuffer msg = new StringBuffer();

        msg.append( getMyMessage() );

        msg.append( " -> " );

        msg.append( rootCause.getMessage() );

        return msg.toString();
    }

    /** Retrieve the error message localized 
     *  to the default locale.
     *
     *  @return The error message.
     */
    public String getLocalizedMessage()
    {
        Throwable rootCause = getRootCause();

        if ( rootCause == null )
        {
            return getMyLocalizedMessage();
        }

        StringBuffer msg = new StringBuffer();

        msg.append( getMyMessage() );

        msg.append( " -> " );

        msg.append( rootCause.getLocalizedMessage() );

        return msg.toString();
    }

    /** Retrieve the error message localized 
     *  to a specific locale.
     *
     *  @param locale The locale
     *
     *  @return The error message.
     */
    public String getLocalizedMessage(Locale locale)
    {
        Throwable rootCause = getRootCause();

        if ( rootCause == null )
        {
            return getMyLocalizedMessage( locale );
        }

        StringBuffer msg = new StringBuffer();

        msg.append( getMyMessage() );

        msg.append( " -> " );

        if ( rootCause instanceof DroolsException )
        {
            msg.append( ((DroolsException)rootCause).getLocalizedMessage( locale ) );
        }
        else
        {
            msg.append( rootCause.getLocalizedMessage() );
        }

        return msg.toString();
    }

    /** Print the stack trace.
     */
    public void printStackTrace()
    {
        printMyStackTrace();

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            System.err.println( " ->" );

            rootCause.printStackTrace();
        }
    }

    /** Print the stack trace.
     *
     *  @param s The output sink.
     */
    public void printStackTrace(PrintStream s)
    {
        printMyStackTrace();

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            s.println( " ->" );
            rootCause.printStackTrace( s );
        }
    }

    /** Print the stack trace.
     *
     *  @param s The output sink.
     */
    public void printStackTrace(PrintWriter s)
    {
        printMyStackTrace();

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            s.println( " ->" );
            rootCause.printStackTrace( s );
        }
    }
}

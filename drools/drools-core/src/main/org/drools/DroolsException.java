package org.drools;

import java.io.PrintStream;
import java.io.PrintWriter;

/** Base <code>drools Logic Engine</code> exception.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: DroolsException.java,v 1.12 2003-10-28 18:57:15 bob Exp $
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

    /** Construct with a message.
     *
     *  @param msg The message.
     */
    public DroolsException(String msg)
    {
        super( msg );
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Exception
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the error message.
     *
     *  @return The error message.
     */
    public String getMessage()
    {
        String selfMessage = super.getMessage();

        StringBuffer msg = new StringBuffer();

        if ( selfMessage != null )
        {
            msg.append( selfMessage );
        }

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            if ( selfMessage != null )
            {
                msg.append( " : " );
            }

            msg.append( rootCause.getMessage() );
        }

        if ( msg.length() > 0 )
        {
            return msg.toString();
        }

        return null;
    }

    /** Retrieve the error message localized 
     *  to the default locale.
     *
     *  @return The error message.
     */
    public String getLocalizedMessage()
    {
        StringBuffer msg = new StringBuffer();

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            msg.append( rootCause.getLocalizedMessage() );
        }
        else
        {
            msg.append( super.getLocalizedMessage() );
        }

        if ( msg.length() > 0 )
        {
            return msg.toString();
        }

        return null;
    }

    /** Print the stack trace.
     *
     *  @param s The output sink.
     */
    public void printStackTrace(PrintStream s)
    {
        super.printStackTrace( s );

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            System.err.println( "Nested exception was: " );
            rootCause.printStackTrace( s );
        }
    }

    /** Print the stack trace.
     *
     *  @param s The output sink.
     */
    public void printStackTrace(PrintWriter s)
    {
        super.printStackTrace( s );

        Throwable rootCause = getRootCause();

        if ( rootCause != null )
        {
            System.err.println( "Nested exception was: " );
            rootCause.printStackTrace( s );
        }
    }
}


package org.drools;

import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.Locale;
import java.util.ResourceBundle;

/** Base <code>drools Logic Engine</code> exception.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class DroolsException extends Exception
{
    private Throwable rootCause;

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

    public String getMyLocalizedMessage(Locale locale)
    {
        return getMyLocalizedMessage();
    }

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

    public void printMyStackTrace()
    {
        super.printStackTrace();
    }

    public void printMyStackTrace(PrintStream s)
    {
        super.printStackTrace( s );
    }

    public void printMyStackTrace(PrintWriter s)
    {
        super.printStackTrace( s );
    }
}

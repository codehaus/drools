package org.drools.jsr94.jca.spi;

/*
 * $Id: RuleManagedConnectionFactory.java,v 1.8 2004-11-05 20:49:33 dbarnett Exp $
 *
 * Copyright 2002-2004 (C) The Werken Company. All Rights Reserved.
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

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

/**
 * ManagedConnectionFactory instance is a factory of both ManagedConnection and
 * EIS-specific connection factory instances. This interface supports connection
 * pooling by providing methods for matching and creation of ManagedConnection
 * instance.
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleManagedConnectionFactory
    implements ManagedConnectionFactory, Serializable
{
    private PrintWriter logWriter;

    /**
     * Creates a Connection Factory instance. The Connection Factory instance
     * gets initialized with the passed ConnectionManager. In the managed
     * scenario, ConnectionManager is provided by the application server.
     */
    public Object createConnectionFactory( ConnectionManager cxManager )
        throws ResourceException
    {
        logWriter.println(
            "RuleManagedConnectionFactory.createConnectionFactory" );
        return new RuleConnectionFactory( this, cxManager );
    }

    /**
     * Creates a Connection Factory instance. The Connection Factory instance
     * gets initialized with a default ConnectionManager provided by the
     * resource adapter.
     */
    public Object createConnectionFactory( ) throws ResourceException
    {
        logWriter.println( "RuleManagedConnectionFactory.createManagedFactory" );
        return new RuleConnectionFactory( this, null );
    }

    /**
     * Creates a new physical connection to the underlying EIS resource manager.
     * <p>
     * ManagedConnectionFactory uses the security information (passed as
     * Subject) and additional ConnectionRequestInfo (which is specific to
     * ResourceAdapter and opaque to application server) to create this new
     * connection.
     */
    public ManagedConnection createManagedConnection(
        Subject subject, ConnectionRequestInfo info )
    {
        logWriter.println(
            "RuleManagedConnectionFactory.createManagedConnection" );
        return null;
    }

    /**
     * Returns a matched connection from the candidate set of connections.
     * <p>
     * ManagedConnectionFactory uses the security info (as in Subject) and
     * information provided through ConnectionRequestInfo and additional
     * Resource Adapter specific criteria to do matching. Note that criteria
     * used for matching is specific to a resource adapter and is not prescribed
     * by the Connector specification.
     * <p>
     * This method returns a ManagedConnection instance that is the best match
     * for handling the connection allocation request.
     *
     */
    public ManagedConnection matchManagedConnections(
            Set connectionSet, Subject subject, ConnectionRequestInfo info )
        throws ResourceException
    {
        logWriter.println(
            "RuleManagedConnectionFactory.matchManagedConnections" );
        return null;
    }

    /**
     * Set the log writer for this ManagedConnectionFactory instance.
     * <p>
     * The log writer is a character output stream to which all logging and
     * tracing messages for this ManagedConnectionfactory instance will be
     * printed.
     * <p>
     * ApplicationServer manages the association of output stream with the
     * ManagedConnectionFactory. When a ManagedConnectionFactory object is
     * created the log writer is initially null, in other words, logging is
     * disabled. Once a log writer is associated with a
     * ManagedConnectionFactory, logging and tracing for
     * ManagedConnectionFactory instance is enabled.
     * <p>
     * The ManagedConnection instances created by ManagedConnectionFactory
     * "inherits" the log writer, which can be overridden by ApplicationServer
     * using ManagedConnection.setLogWriter to set ManagedConnection specific
     * logging and tracing.
     */
    public void setLogWriter( PrintWriter logWriter ) throws ResourceException
    {
        this.logWriter = logWriter;
    }

    /**
     * Get the log writer for this ManagedConnectionFactory instance.
     * <p>
     * The log writer is a character output stream to which all logging and
     * tracing messages for this ManagedConnectionFactory instance will be
     * printed.
     * <p>
     * ApplicationServer manages the association of output stream with the
     * ManagedConnectionFactory. When a ManagedConnectionFactory object is
     * created the log writer is initially null, in other words, logging is
     * disabled.
     */
    public PrintWriter getLogWriter( ) throws ResourceException
    {
        return logWriter;
    }
}

package org.drools.smf;

import org.drools.DroolsException;

public class ConfigurationException extends DroolsException
{
    public ConfigurationException(String msg)
    {
        super( msg );
    }

    public ConfigurationException(Throwable rootCause)
    {
        super( rootCause );
    }
}


package org.drools;

public class FactException extends DroolsException
{
    public FactException()
    {
    }

    public FactException(Throwable rootCause)
    {
        super( rootCause );
    }
        
}

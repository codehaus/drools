
package org.drools.spi;

import org.drools.DroolsException;

public class RuleConstructionException extends DroolsException
{
    public RuleConstructionException()
    {
    }

    public RuleConstructionException(String msg)
    {
        super( msg );
    }
}

package org.drools.semantics.annotation.testrules;

import java.util.HashMap;
import java.util.Map;

public class AuditService
{
    private Map< String, Integer > values = new HashMap< String, Integer >( );

    public void auditValue( String comment, int value )
    {
        values.put( comment, value );
    }

    public Map< String, Integer > getValues( )
    {
        return values;
    }
}

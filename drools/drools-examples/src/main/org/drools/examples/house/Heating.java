package org.drools.examples.house;

import java.util.HashMap;
import java.util.Map;

public class Heating
{
    private Map map = new HashMap( );

    public void heatingOn(String room)
    {
        map.put( room,
                 "on" );
        System.err.println( room + ":" + "on" );
    }

    public void heatingOff(String room)
    {
        map.put( room,
                 "off" );
        System.err.println( room + ":" + "off" );
    }
}

package org.drools.semantics.annotation.testrules;

public class FooBar
{
    private final String name;
    private int min;
    private int max;
    private int value;

    public FooBar( String name )
    {
        this.name = name;
    }

    public String getName( )
    {
        return name;
    }

    public int getMax( )
    {
        return max;
    }

    public void setMax( int max )
    {
        this.max = max;
    }

    public int getMin( )
    {
        return min;
    }

    public void setMin( int min )
    {
        this.min = min;
    }

    public int getValue( )
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }
}

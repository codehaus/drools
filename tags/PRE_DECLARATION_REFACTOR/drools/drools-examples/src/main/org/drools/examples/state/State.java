package org.drools.examples.state;

public class State
{
    private String name;

    private String state;

    public State(String name)
    {
        this.name = name;
        this.state = "NOTRUN";
    }

    public String getName()
    {
        return this.name;
    }

    public String getState()
    {
        return this.state;
    }

    public String setState(String state)
    {
        return this.state = state;
    }

    public boolean inState(String name, String state)
    {
        boolean b = this.name.equals( name ) && this.state.equals( state );
        return b;
    }

    public String toString()
    {
        return this.name + "[" + this.state + "]";
    }
}
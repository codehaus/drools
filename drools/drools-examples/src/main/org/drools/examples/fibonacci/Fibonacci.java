package org.drools.examples.fibonacci;

public class Fibonacci
{
    private int sequence;
    private int value;

    public Fibonacci(int sequence)
    {
        this.sequence = sequence;
        this.value    = -1;
    }

    public int getSequence()
    {
        return this.sequence;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return "Fibonacci(" + this.sequence + "/" + this.value + ")";
    }
}

package org.drools.examples.house;

public class Room
{
    private String name;

    private int    temperature;

    /**
     * 
     */
    public Room(String name)
    {
        this.name = name;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return Returns the temperature.
     */
    public int getTemperature()
    {
        return this.temperature;
    }
    /**
     * @param temperature
     *            The temperature to set.
     */
    public void setTemperature(int temperature)
    {
        this.temperature = temperature;
    }
}

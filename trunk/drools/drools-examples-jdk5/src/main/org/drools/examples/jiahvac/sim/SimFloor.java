package org.drools.examples.jiahvac.sim;

import org.drools.examples.jiahvac.model.Floor;

public class SimFloor implements Floor
{
    private final int number;
    private SimThermometer thermometer;
    private SimVent vent;
    private SimHeatPump heatPump;
    
    public SimFloor(int number)
    {
        this.number = number;
    }    
    
    public int getNumber()
    {
        return number;
    }
        
    public SimThermometer getThermometer()
    {
        return thermometer;
    }
    
    public void setThermometer(SimThermometer thermometer)
    {
        this.thermometer = thermometer;
    }
    
    public SimVent getVent()
    {
        return vent;
    }
    
    public void setVent(SimVent vent)
    {
        this.vent = vent;
    }
    
    public SimHeatPump getHeatPump()
    {
        return heatPump;
    }
    
    public void setHeatPump(SimHeatPump heatPump)
    {
        this.heatPump = heatPump;
    }
}

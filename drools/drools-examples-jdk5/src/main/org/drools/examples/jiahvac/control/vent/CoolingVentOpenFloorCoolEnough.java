package org.drools.examples.jiahvac.control.vent;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.Vent;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import org.drools.semantics.annotation.Drools;

@Drools.Rule
public class CoolingVentOpenFloorCoolEnough
{
    @Drools.Condition
    public boolean condition(@Drools.Parameter("vent") Vent vent,
                             @Drools.Parameter("thermometer") Thermometer thermometer,
                             @Drools.Parameter("pump") HeatPump pump,
                             @Drools.Parameter("control") TempuratureControl control) {
        return isPumpCooling(pump) 
                && isVentOpen(vent)
                && isCoolEnough(thermometer, control)
                && isSameFloor(vent, thermometer, pump);
    }
    
    //@Drools.Condition
    public boolean isPumpCooling(@Drools.Parameter("pump") HeatPump pump) {
        return pump.getState() == HeatPump.State.COOLING;
     }
    
    //@Drools.Condition
    public boolean isVentOpen(@Drools.Parameter("vent") Vent vent) {
        return vent.getState() == Vent.State.OPEN;
     }
    
    //@Drools.Condition
    public boolean isCoolEnough(@Drools.Parameter("thermometer") Thermometer thermometer,
                                @Drools.Parameter("control") TempuratureControl control) {
        return control.isCoolEnough(thermometer.getReading());
    }
    
    //@Drools.Condition
    public boolean isSameFloor(@Drools.Parameter("vent") Vent vent,
                               @Drools.Parameter("thermometer") Thermometer thermometer,
                               @Drools.Parameter("pump") HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }
    
    @Drools.Consequence
    public void consequence(@Drools.Parameter("vent") Vent vent) {
        vent.setState(Vent.State.CLOSED);
        System.out.println("CoolingVentOpenFloorCoolEnough: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}

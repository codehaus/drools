package org.drools.examples.jiahvac.control.vent;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.Vent;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import org.drools.semantics.annotation.Drools;

@Drools.Rule
public class HeatingVentClosedFloorTooCold
{
    @Drools.Condition
    public boolean condition(@Drools.Parameter("vent") Vent vent,
                             @Drools.Parameter("thermometer") Thermometer thermometer,
                             @Drools.Parameter("pump") HeatPump pump,
                             @Drools.Parameter("control") TempuratureControl control) {
        return isPumpHeating(pump) 
                && isVentClosed(vent)
                && isNotWarmEnough(thermometer, control)
                && isSameFloor(vent, thermometer, pump);
    }
    
    //@Drools.Condition
    public boolean isPumpHeating(@Drools.Parameter("pump") HeatPump pump) {
        return pump.getState() == HeatPump.State.HEATING;
     }
    
    //@Drools.Condition
    public boolean isVentClosed(@Drools.Parameter("vent") Vent vent) {
        return vent.getState() == Vent.State.CLOSED;
     }
    
    //@Drools.Condition
    public boolean isNotWarmEnough(@Drools.Parameter("thermometer") Thermometer thermometer,
                                   @Drools.Parameter("control") TempuratureControl control) {
        return !control.isWarmEnough(thermometer.getReading());
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
        vent.setState(Vent.State.OPEN);
        System.out.println("HeatingVentClosedFloorTooCold: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}

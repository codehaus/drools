package org.drools.spring.examples.jiahvac.control.rules;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.examples.jiahvac.model.Vent;
import org.drools.spring.metadata.annotation.java.*;

@Rule
public class HeatingVentOpenFloorWarmEnough {
    
    private TempuratureControl control;
    
    public void setControl(TempuratureControl control) {
        this.control = control;
    }
    
    @Condition
    public boolean isPumpHeating(HeatPump pump) {
        return pump.getState() == HeatPump.State.HEATING;
     }

    @Condition
    public boolean isVentOpen(Vent vent) {
        return vent.getState() == Vent.State.OPEN;
     }

    @Condition
    public boolean isSameFloor(Vent vent, Thermometer thermometer, HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isWarmEnough(Thermometer thermometer) {
        return control.isWarmEnough(thermometer.getReading());
    }

    @Consequence
    public void consequence(Vent vent) {
        vent.setState(Vent.State.CLOSED);
        System.out.println("HeatingVentOpenFloorWarmEnough: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}

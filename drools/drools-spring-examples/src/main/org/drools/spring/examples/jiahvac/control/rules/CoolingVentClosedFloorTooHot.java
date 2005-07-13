package org.drools.spring.examples.jiahvac.control.rules;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.examples.jiahvac.model.Vent;
import org.drools.spring.metadata.annotation.java.*;

@Rule
public class CoolingVentClosedFloorTooHot {
    
    private TempuratureControl control;
    
    public void setControl(TempuratureControl control) {
        this.control = control;
    }
    
    @Condition
    public boolean isPumpCooling(HeatPump pump) {
        return pump.getState() == HeatPump.State.COOLING;
     }

    @Condition
    public boolean isVentClosed(Vent vent) {
        return vent.getState() == Vent.State.CLOSED;
     }

    @Condition
    public boolean isSameFloor(Vent vent, Thermometer thermometer, HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isNotCoolEnough(Thermometer thermometer) {
        return !control.isCoolEnough(thermometer.getReading());
    }

    @Consequence
    public void consequence(Vent vent) {
        vent.setState(Vent.State.OPEN);
        System.out.println("CoolingVentClosedFloorTooHot: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}

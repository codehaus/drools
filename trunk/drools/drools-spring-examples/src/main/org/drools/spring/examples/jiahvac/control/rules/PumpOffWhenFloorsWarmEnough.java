package org.drools.spring.examples.jiahvac.control.rules;

import static org.drools.spring.examples.jiahvac.model.HeatPump.State.*;

import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.metadata.annotation.java.*;

@Rule
public class PumpOffWhenFloorsWarmEnough {
    
    private TempuratureControl control;
    
    public void setControl(TempuratureControl control) {
        this.control = control;
    }
    
    @Condition
    public boolean isPumpHeating(HeatPump pump) {
        return pump.getState() == HEATING;
     }

    @Condition
    public boolean isPumpServicingFloor(HeatPump pump, Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isAllFloorsWarmEnough(HeatPump pump, Thermometer thermometer) {
        if (!control.isWarmEnough(thermometer.getReading())) {
            return false;
        }
        for (Floor floor : pump.getFloors()) {
            if (floor == thermometer.getFloor()) {
                continue;
            }
            if (!control.isWarmEnough(floor.getThermometer().getReading())) {
                return false;
            }
        }
        return true;
     }

    @Consequence
    public void consequence(HeatPump pump) {
        pump.setState(OFF);
        System.out.println("PumpOffWhenFloorsWarmEnough: " + pump);
    }
}

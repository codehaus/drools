package org.drools.spring.examples.jiahvac.control.rules;

import static org.drools.spring.examples.jiahvac.model.HeatPump.State.*;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.metadata.annotation.java.*;

@Rule
public class FloorTooColdPumpOff {
    
    private TempuratureControl control;
    
    public void setControl(TempuratureControl control) {
        this.control = control;
    }
    
    @Condition
    public boolean isPumpOff(HeatPump pump) {
        return pump.getState() == OFF;
     }

    @Condition
    public boolean isPumpServicingFloor(HeatPump pump, Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isTooCold(Thermometer thermometer) {
        return control.isTooCold(thermometer.getReading());
    }

    @Consequence
    public void consequence(HeatPump pump) {
        pump.setState(HEATING);
        System.out.println("FloorTooColdPumpOff: " + pump);
    }
}

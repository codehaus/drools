package org.drools.examples.jiahvac.control.pump;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.Drools;

@Drools.Rule
public class FloorTooHotPumpOff
{
    @Drools.Condition
    public boolean isTooHot(@Drools.Parameter("thermometer") Thermometer thermometer,
                            @Drools.Parameter("control") TempuratureControl control) {
        return control.isTooHot(thermometer.getReading());
    }
    
    @Drools.Condition
    public boolean isPumpOff(@Drools.Parameter("pump") HeatPump pump,
                             @Drools.Parameter("thermometer") Thermometer thermometer) {
        return pump.getState() == OFF 
                && thermometer.getFloor().getHeatPump() == pump;
     }
    
    @Drools.Consequence
    public void consequence(@Drools.Parameter("pump") HeatPump pump) {
        pump.setState(COOLING);
        System.out.println("FloorTooHotPumpOff: " + pump);
    }
}

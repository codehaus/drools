package org.drools.examples.jiahvac.control.rules;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.Rule;
import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.Condition;
import org.drools.semantics.annotation.Consequence;

@Rule
public class FloorTooHotPumpOff
{
    @Condition
    public boolean isPumpOff(@Parameter HeatPump pump) {
        return pump.getState() == OFF;
     }

    @Condition
    public boolean isPumpServicingFloor(@Parameter HeatPump pump,
                                        @Parameter Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isTooHot(@Parameter Thermometer thermometer,
                            @Parameter TempuratureControl control) {
        return control.isTooHot(thermometer.getReading());
    }

    @Consequence
    public void consequence(@Parameter HeatPump pump) {
        pump.setState(COOLING);
        System.out.println("FloorTooHotPumpOff: " + pump);
    }
}

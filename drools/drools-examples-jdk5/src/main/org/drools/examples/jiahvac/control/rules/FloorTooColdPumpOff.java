package org.drools.examples.jiahvac.control.rules;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;

@DroolsRule
public class FloorTooColdPumpOff
{
    @DroolsCondition
    public boolean isPumpOff(@DroolsParameter("pump") HeatPump pump) {
        return pump.getState() == OFF;
     }

    @DroolsCondition
    public boolean isPumpServicingFloor(@DroolsParameter("pump") HeatPump pump,
                                        @DroolsParameter("thermometer") Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @DroolsCondition
    public boolean isTooCold(@DroolsParameter("thermometer") Thermometer thermometer,
                             @DroolsParameter("control") TempuratureControl control) {
        return control.isTooCold(thermometer.getReading());
    }

    @DroolsConsequence
    public void consequence(@DroolsParameter("pump") HeatPump pump) {
        pump.setState(HEATING);
        System.out.println("FloorTooColdPumpOff: " + pump);
    }
}

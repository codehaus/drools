package org.drools.examples.jiahvac.control.pump;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;

@DroolsRule
public class FloorTooHotPumpOff
{
    @DroolsCondition
    public boolean isTooHot(@DroolsParameter("thermometer") Thermometer thermometer,
                            @DroolsParameter("control") TempuratureControl control) {
        return control.isTooHot(thermometer.getReading());
    }
    
    @DroolsCondition
    public boolean isPumpOff(@DroolsParameter("pump") HeatPump pump,
                             @DroolsParameter("thermometer") Thermometer thermometer) {
        return pump.getState() == OFF 
                && thermometer.getFloor().getHeatPump() == pump;
     }
    
    @DroolsConsequence
    public void consequence(@DroolsParameter("pump") HeatPump pump) {
        pump.setState(COOLING);
        System.out.println("FloorTooHotPumpOff: " + pump);
    }
}

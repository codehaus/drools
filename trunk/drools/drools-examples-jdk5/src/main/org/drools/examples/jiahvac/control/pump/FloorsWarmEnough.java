package org.drools.examples.jiahvac.control.pump;

import org.drools.examples.jiahvac.model.Floor;
import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;

@DroolsRule
public class FloorsWarmEnough
{
    @DroolsCondition
    public boolean isPumpHeating(@DroolsParameter("pump") HeatPump pump) {
        return pump.getState() == HEATING;
     }
    
    @DroolsCondition
    public boolean isAllFloorsWarmEnough(@DroolsParameter("pump") HeatPump pump,
                                         @DroolsParameter("thermometer") Thermometer thermometer,
                                         @DroolsParameter("control") TempuratureControl control) {
        if (thermometer.getFloor().getHeatPump() != pump) {
            return false;
        }
        boolean result = true;
        for (Floor floor : pump.getFloors()) {
            double temp = floor.getThermometer().getReading();
            result &= control.isWarmEnough(temp);
        }
        return result;
     }
    
    @DroolsConsequence
    public void consequence(@DroolsParameter("pump") HeatPump pump) {
        pump.setState(OFF);
        System.out.println("FloorsWarmEnough: " + pump);
    }
}

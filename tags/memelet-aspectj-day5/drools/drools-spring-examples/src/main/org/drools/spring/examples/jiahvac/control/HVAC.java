package org.drools.spring.examples.jiahvac.control;

import org.drools.WorkingMemory;
import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControlImpl;
import org.drools.spring.examples.jiahvac.sim.Simulator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HVAC {

    public static void main(String args[]) throws Exception {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("org/drools/spring/examples/jiahvac/control/hvac.appctx.xml");

        final WorkingMemory workingMemory = (WorkingMemory) applicationContext.getBean("workingMemory");
        final Simulator simulator = (Simulator) applicationContext.getBean("simulator");
        final TempuratureControlImpl control = (TempuratureControlImpl) applicationContext.getBean("tempuratureControl");

        workingMemory.assertObject(control);

        for (Floor floor : simulator.getFloors()) {
            workingMemory.assertObject(floor.getThermometer(), true);
            workingMemory.assertObject(floor.getVent(), true);
        }
        for (HeatPump pump : simulator.getHeatPumps()) {
            workingMemory.assertObject(pump, true);
        }
        System.out.println("---- begin initial fireAllRules");
        workingMemory.fireAllRules();
        System.out.println("---- end initial fireAllRules");

        // Is lazy-init so we must force initialization.
        // TODO Why is it lazy-init?
        applicationContext.getBean("executor");
    }
}

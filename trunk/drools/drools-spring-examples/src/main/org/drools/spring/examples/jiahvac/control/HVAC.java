package org.drools.spring.examples.jiahvac.control;

import org.drools.WorkingMemory;
import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.sim.Simulator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HVAC {

    public static void main(String args[]) throws Exception {

        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "org/drools/spring/examples/jiahvac/control/hvac.appctx.xml");

        final WorkingMemory workingMemory = 
                (WorkingMemory) applicationContext.getBean("workingMemory");
        
        final Simulator simulator = 
                (Simulator) applicationContext.getBean("simulator");
        
        final TempuratureControl control = 
                (TempuratureControl) applicationContext.getBean("tempuratureControl");

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

        // The 'executor' bean is lazy-init so we can control when the simulator starts
        // (which is now). Alternatively, we could have defined a start method to begin 
        // the simulation.
        applicationContext.getBean("executor");
    }
}

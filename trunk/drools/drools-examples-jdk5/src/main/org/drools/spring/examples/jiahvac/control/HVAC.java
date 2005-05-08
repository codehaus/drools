package org.drools.spring.examples.jiahvac.control;

import java.util.concurrent.ScheduledExecutorService;

import org.drools.WorkingMemory;
import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControlImpl;
import org.drools.spring.examples.jiahvac.sim.Simulator;
import org.drools.spring.examples.jiahvac.sim.SimulatorGUI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HVAC {

    public static void main(String args[]) throws Exception {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("org/drools/spring/examples/jiahvac/control/hvac.appctx.xml");

        final WorkingMemory workingMemory = (WorkingMemory) applicationContext.getBean("workingMemory");
        final Simulator simulator = (Simulator) applicationContext.getBean("simulator");
        final SimulatorGUI simulatorGUI = (SimulatorGUI) applicationContext.getBean("simulatorGUI");
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

        final ScheduledExecutorService executor = (ScheduledExecutorService) applicationContext.getBean("executor");
    }
}

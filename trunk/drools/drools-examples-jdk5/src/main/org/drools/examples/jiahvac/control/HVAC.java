package org.drools.examples.jiahvac.control;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.event.ConditionTestedEvent;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.event.DefaultWorkingMemoryEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.examples.jiahvac.control.rules.CoolingVentClosedFloorTooHot;
import org.drools.examples.jiahvac.control.rules.CoolingVentOpenFloorCoolEnough;
import org.drools.examples.jiahvac.control.rules.FloorTooColdPumpOff;
import org.drools.examples.jiahvac.control.rules.FloorTooHotPumpOff;
import org.drools.examples.jiahvac.control.rules.FloorsCoolEnough;
import org.drools.examples.jiahvac.control.rules.FloorsWarmEnough;
import org.drools.examples.jiahvac.control.rules.HeatingVentClosedFloorTooCold;
import org.drools.examples.jiahvac.control.rules.HeatingVentOpenFloorWarmEnough;
import org.drools.examples.jiahvac.model.Floor;
import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControlImpl;
import org.drools.examples.jiahvac.model.Vent;
import org.drools.examples.jiahvac.sim.Simulator;
import org.drools.examples.jiahvac.sim.SimulatorGUI;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder;

public class HVAC
{
    private static final int FLOOR_COUNT = 3*10;
    private static final int INITIAL_SETPOINT = 70;
    private static final int INITIAL_TEMPURATURE_READING = 67;

    private static final long SIMULATOR_PERIOD = 1000;
    private static final TimeUnit SIMULATOR_PERIOD_TIMEUNITS = TimeUnit.MILLISECONDS;

    private static WorkingMemoryEventListener filteredWorkingMemoryEventListener = new DefaultWorkingMemoryEventListener() {
        public void conditionTested(ConditionTestedEvent event) {
            String message = event.toString();
            if (message.contains("rule=CoolingVentClosedFloorTooHot")) {
                System.err.println(message);
            }
        }
    };

    private static WorkingMemoryEventListener debugWorkingMemoryEventListener = new DebugWorkingMemoryEventListener();

    public static void main(String args[]) throws Exception {
        AnnonatedPojoRuleBuilder annoRuleBuilder = new AnnonatedPojoRuleBuilder();

        RuleSet pumpRuleSet = new RuleSet("pump rule-set");
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("FloorsCoolEnough"), new FloorsCoolEnough()));
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("FloorsWarmEnough"), new FloorsWarmEnough()));
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("FloorTooColdPumpOff"), new FloorTooColdPumpOff()));
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("FloorTooHotPumpOff"), new FloorTooHotPumpOff()));

        RuleSet ventRuleSet = new RuleSet("vent rule-set");
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("HeatingVentOpenFloorWarmEnough"), new HeatingVentOpenFloorWarmEnough()));
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("CoolingVentOpenFloorCoolEnough"), new CoolingVentOpenFloorCoolEnough()));
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("HeatingVentClosedFloorTooCold"), new HeatingVentClosedFloorTooCold()));
        pumpRuleSet.addRule(annoRuleBuilder.buildRule(
                new Rule("CoolingVentClosedFloorTooHot"), new CoolingVentClosedFloorTooHot()));

        RuleBaseBuilder builder = new RuleBaseBuilder();
        builder.addRuleSet(pumpRuleSet);
        builder.addRuleSet(ventRuleSet);
        RuleBase ruleBase = builder.build();

        final WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        //workingMemory.addEventListener(filteredWorkingMemoryEventListener);

        final Simulator simulator = new Simulator(FLOOR_COUNT, INITIAL_TEMPURATURE_READING, Vent.State.OPEN);
        final SimulatorGUI simulatorGUI = new SimulatorGUI(simulator);

        TempuratureControlImpl control = new TempuratureControlImpl(INITIAL_SETPOINT, 1);
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

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                simulator.run();
                try {
                    workingMemory.fireAllRules();
                } catch (FactException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, SIMULATOR_PERIOD, SIMULATOR_PERIOD_TIMEUNITS);
        executor.scheduleAtFixedRate(simulatorGUI, 0, 1, TimeUnit.SECONDS);
    }
}

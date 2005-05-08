package org.drools.spring.examples.jiahvac.control;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.drools.FactException;
import org.drools.WorkingMemory;
import org.drools.spring.examples.jiahvac.sim.Simulator;
import org.drools.spring.examples.jiahvac.sim.SimulatorGUI;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ExecutorFactoryBean implements InitializingBean, FactoryBean {

    private WorkingMemory workingMemory;
    private long simulatorPeriod = Long.MIN_VALUE;
    private TimeUnit simulatorPeriodUnit;
    private Simulator simulator;
    private SimulatorGUI simulatorGUI;
    private ScheduledExecutorService executor;

    public void setWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void setSimulatorPeriod(long simulatorPeriodUnit) {
        this.simulatorPeriod = simulatorPeriodUnit;
    }

    public void setSimulatorPeriodUnit(TimeUnit simulatorTimeUnit) {
        this.simulatorPeriodUnit = simulatorTimeUnit;
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }

    public void setSimulatorGUI(SimulatorGUI simulatorGUI) {
        this.simulatorGUI = simulatorGUI;
    }

    public void afterPropertiesSet() throws Exception {
        if (workingMemory == null) {
            throw new IllegalArgumentException("WorkingMemory not set");
        }
        if (simulator == null) {
            throw new IllegalArgumentException("Simulator not set");
        }
        if (simulatorPeriod == Long.MIN_VALUE) {
            throw new IllegalArgumentException("SimulatorPeriod not set");
        }
        if (simulatorPeriodUnit == null) {
            throw new IllegalArgumentException("SimulatorPeriodUnit not set");
        }
        if (simulatorGUI == null) {
            throw new IllegalArgumentException("SimulatorGUI not set");
        }
    }

    public Object getObject() throws Exception {
        if (executor == null) {
            executor = new ScheduledThreadPoolExecutor(1);
            executor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    simulator.run();
                    try {
                        workingMemory.fireAllRules();
                    } catch (FactException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, simulatorPeriod, simulatorPeriodUnit);
            executor.scheduleAtFixedRate(simulatorGUI, 0, 1, TimeUnit.SECONDS);

        }
        return executor;
    }

    public Class getObjectType() {
        return ScheduledExecutorService.class;
    }

    public boolean isSingleton() {
        return true;
    }
}

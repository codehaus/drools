package org.drools.spring.examples.jiahvac.control;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */



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


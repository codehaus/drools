package org.drools.spring.examples.jiahvac.sim;

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



import org.drools.spring.examples.jiahvac.model.Floor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimulatorGUI implements Runnable{

    private Simulator simulator;
    private int floorCount;
    private JLabel[] labels;
    private JTextField outdoorField = new JTextField();

    public SimulatorGUI(Simulator simulator) {
        this.simulator = simulator;
        floorCount = simulator.getFloors().length;
        JFrame frame = createControls();
        renderFloors();
        frame.pack();
        frame.setVisible(true);
    }

    private JFrame createControls() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel floorsPanel = new JPanel();
        floorsPanel.setLayout(new GridLayout(floorCount, 1));

        labels = new JLabel[floorCount];
        for (int floorIndex = 0; floorIndex < floorCount; floorIndex++) {
            JLabel label = new JLabel();
            label.setFont(new Font("Courier", 0, 12));
            labels[floorIndex] = label;
            floorsPanel.add(labels[floorIndex]);
        }

        String text = String.valueOf(simulator.getOutdoorTempurature());
        outdoorField.setText(text);
        outdoorField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    double value = Double.parseDouble(outdoorField.getText());
                    simulator.setOutdoorTempurature(value);
                }
            });

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(floorsPanel, BorderLayout.CENTER);
        content.add(outdoorField, BorderLayout.SOUTH);

        return frame;
    }

    public void run() {
        renderFloors();
    }

    private void renderFloors() {
        Floor[] floors = simulator.getFloors();
        for (int i = 0; i < floors.length; i++) {
            Floor floor = floors[i];
            JLabel label = labels[i];
            label.setText(String.format(
                    "%2d) %3.2f - pump-%1d %-8s, vent %-8s",
                    floor.getNumber()+1,
                    floor.getThermometer().getReading(),
                    floor.getHeatPump().getId(),
                    floor.getHeatPump().getState(),
                    floor.getVent().getState()));
        }
    }
}


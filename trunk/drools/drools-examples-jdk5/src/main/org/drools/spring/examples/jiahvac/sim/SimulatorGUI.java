package org.drools.spring.examples.jiahvac.sim;

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

package org.drools.spring.examples.jiahvac.model;

public interface Vent {
    
    Floor getFloor();

    enum State {
        OPEN("open", 1),
        CLOSED("closed", 0);

        private final String name;
        private final double value;

        State(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    };

    void setState(State state);
    State getState();
}
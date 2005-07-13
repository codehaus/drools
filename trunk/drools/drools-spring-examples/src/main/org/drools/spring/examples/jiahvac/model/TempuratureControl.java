package org.drools.spring.examples.jiahvac.model;

public class TempuratureControl {
    
    private double setPoint;
    private double guardAmount;

    public TempuratureControl(double setPoint, double guardAmount) {
        this.setPoint = setPoint;
        this.guardAmount = guardAmount;
    }

    public double getSetPoint() {
        return setPoint;
    }

    public double getGuardAmount() {
        return guardAmount;
    }

    public boolean isTooCold(double temperature) {
        return temperature < (setPoint - guardAmount);
    }

    public boolean isTooHot(double temperature) {
        return temperature > (setPoint + guardAmount);
    }

    public boolean isCoolEnough(double tempurature) {
        return tempurature < setPoint;
    }

    public boolean isWarmEnough(double tempurature) {
        return tempurature > setPoint;
    }
}

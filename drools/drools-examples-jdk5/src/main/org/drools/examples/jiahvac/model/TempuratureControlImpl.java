package org.drools.examples.jiahvac.model;

public class TempuratureControlImpl implements TempuratureControl
{
    private double setPoint;
    private double guardAmount;

    public TempuratureControlImpl(double setPoint, double guardAmount) {
        this.setPoint = setPoint;
        this.guardAmount = guardAmount;
    }
    
//    public void setSetPoint(double setPoint) 
//    {
//        this.setPoint = setPoint;
//    }
    
    public double getSetPoint()
    {
        return setPoint;
    }
    
    public double getGuardAmount()
    {
        return guardAmount;
    }
    
//    public void setGuardAmount(double guardAmount)
//    {
//        this.guardAmount = guardAmount;
//    }

    public boolean isTooCold(double temperature)
    {
        return temperature < (setPoint - guardAmount);
    }

    public boolean isTooHot(double temperature)
    {
        return temperature > (setPoint + guardAmount);
    }

    public boolean isCoolEnough(double tempurature)
    {
        return tempurature < setPoint;
    }

    public boolean isWarmEnough(double tempurature)
    {
        return tempurature > setPoint;
    }
}

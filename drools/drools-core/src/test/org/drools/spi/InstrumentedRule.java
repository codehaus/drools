
package org.drools.spi;

public class InstrumentedRule extends Rule
{
    public Boolean isValid;

    public InstrumentedRule(String name)
    {
        super( name );
    }

    public void isValid(boolean isValid)
    {
        if ( isValid )
        {
            this.isValid = Boolean.TRUE;
        }
        else
        {
            this.isValid = Boolean.FALSE;
        }
    }

    public boolean isValid()
    {
        if ( this.isValid == null )
        {
            return super.isValid();
        }

        return this.isValid.booleanValue();
    }

    public void checkValidity() throws InvalidRuleException
    {
        if ( this.isValid == null )
        {
            super.checkValidity();
            return;
        }
        else if ( this.isValid.booleanValue() )
        {
            return;
        }
        else
        {
            throw new InvalidRuleException( this );
        }
    }
}

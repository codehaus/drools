package dimensions;

import org.python.core.PyFloat;

/**
 * @author <a href="mailto:christiaan@dacelo.nl">Christiaan ten Klooster</a> 
 */
public class Length extends PyFloat
{
 private double value;
	
	/**
	 * Constructor for LengthBean.
	 * @param arg0
	 */
	public Length(double arg0)
	{
		super(arg0);
	}

	/**
	 * Constructor for LengthBean.
	 * @param arg0
	 */
	public Length(float arg0)
	{
		super(arg0);
	}

}

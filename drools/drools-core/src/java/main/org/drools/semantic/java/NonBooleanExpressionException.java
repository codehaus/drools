
package org.drools.semantic.java;

import org.drools.spi.ConditionException;

/** Indicates a non-boolean BeanShell expression was used
 *  for a filter expression, which <b>must</b> be boolean.
 */
public class NonBooleanExpressionException extends ConditionException
{
    
}

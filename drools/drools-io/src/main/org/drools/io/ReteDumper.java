package org.drools.io;

import java.io.PrintStream;

import org.drools.RuleBase;
import org.drools.reteoo.Dumper;

/**
 * A Rete network dumper.
 * This is a proxy for the Dumper implemented in the core module.
 * Users should not need to refer to reteoo classes.
 * 
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 */
public class ReteDumper {
    
    private Dumper dumper;
    
    public ReteDumper(RuleBase ruleBase) {
        if (ruleBase instanceof SerializableRuleBaseProxy) {
            SerializableRuleBaseProxy proxy = (SerializableRuleBaseProxy) ruleBase;
            dumper = new Dumper(proxy.getWrappedRuleBase());
        } else {
            dumper = new Dumper(ruleBase);
        }
    }
    
    public void dumpRete(PrintStream out)
    {
        dumper.dumpRete( out );
    }

    public void dumpRete(PrintStream out,
                         String indent)
    {
        dumper.dumpRete(out, indent);
    }

    /**
     * Compatible with the GraphViz DOT format.
     */
    public void dumpReteToDot(PrintStream out)
    {
        dumper.dumpReteToDot(out);
    }    

}


package org.drools.semantic.java;

import org.drools.RuleBase;
import org.drools.DroolsException;
import org.drools.spi.RuleSet;

import org.drools.semantic.java.io.RuleSetReader;

import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;

/** Convenience class for loading rules defined purely through
 *  Java Semantics XML ruleset file into a {@link RuleBase}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class RuleLoader
{
    /** Load a rule-set from a local file.
     *
     *  @param ruleBase The <code>RuleBase</code> to load into.
     *  @param ruleSetFile The <code>File</code> containing the rule-set definition.
     *
     *  @throws DroolsException if there is an error reading or parsing the file.
     *  @throws MalformedURLException if the <code>ruleSetFile</code> does not
     *          represent a well-formed URL.
     */
    public static void load(RuleBase ruleBase,
                            File ruleSetFile) throws DroolsException, MalformedURLException
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( ruleSetFile );

        ruleBase.addRuleSet( ruleSet );
    }

    /** Load a rule-set from a URL.
     *
     *  @param ruleBase The <code>RuleBase</code> to load into.
     *  @param ruleSetUrl The <code>URL</code> containing the rule-set definition.
     *
     *  @throws DroolsException if there is an error reading or parsing the file.
     *  @throws MalformedURLException if the <code>ruleSetUrl</code> does not
     *          represent a well-formed URL.
     */
    public static void load(RuleBase ruleBase,
                            URL ruleSetUrl) throws DroolsException, MalformedURLException
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( ruleSetUrl );

        ruleBase.addRuleSet( ruleSet );
    }

    /** Load a rule-set from a character stream.
     *
     *  @param ruleBase The <code>RuleBase</code> to load into.
     *  @param charStream The character stream containing the rule-set definition.
     *
     *  @throws DroolsException if there is an error reading or parsing the file.
     */
    public static void load(RuleBase ruleBase,
                            Reader charStream) throws DroolsException
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( charStream );

        ruleBase.addRuleSet( ruleSet );
    }
        
    /** Load a rule-set from a stream of bytes.
     *
     *  @param ruleBase The <code>RuleBase</code> to load into.
     *  @param byteStream The byte stream containing the rule-set definition.
     *
     *  @throws DroolsException if there is an error reading or parsing the file.
     */
    public static void load(RuleBase ruleBase,
                            InputStream byteStream) throws DroolsException
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( byteStream );

        ruleBase.addRuleSet( ruleSet );
    }
}

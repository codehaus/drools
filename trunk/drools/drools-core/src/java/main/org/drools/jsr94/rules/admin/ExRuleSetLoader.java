package org.drools.jsr94.rules.admin;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;
import org.drools.io.RuleSetLoader;
import org.drools.rule.RuleSet;
import org.drools.tags.rule.RuleTagLibrary;

import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads <code>RuleSet</code> definitions from XML descriptor.
 * <p>
 * Extends the <code>RuleSetLoader</code>.
 */
public class ExRuleSetLoader extends RuleSetLoader {

   /** This is because. */
   private List ruleSets;

   /**
    * Add a <code>RuleSet</code> to this loader.
    *
    *  @param ruleSet The rule-set to add.
    */
   public void addRuleSet(RuleSet ruleSet) {
      ruleSets.add(ruleSet);
   }

   /** Load <code>RuleSet</code> deifnitions from a URL.
    *
    * @see RuleSetLoader#load(URL)
    */
   public List load(Reader ruleReader) throws Exception {

      ruleSets = new ArrayList();

      XMLParser parser = new XMLParser();
      JellyContext context = new JellyContext();

      context.registerTagLibrary("http://drools.org/rules", new RuleTagLibrary());
      context.setVariable("org.drools.io.RuleSetLoader", this);
      parser.setContext(context);

      Script script = parser.parse(ruleReader);
      XMLOutput output = XMLOutput.createXMLOutput(System.err, false);
      script.run(context, output);

      // [TODO] please explain
      List answer = ruleSets;
      ruleSets = null;
      return answer;
   }

}

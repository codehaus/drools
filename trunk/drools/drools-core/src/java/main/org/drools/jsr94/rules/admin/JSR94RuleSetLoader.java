package org.drools.jsr94.rules.admin;

/*
 $Id: JSR94RuleSetLoader.java,v 1.1 2003-03-15 01:24:20 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

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
 *
 *  @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class JSR94RuleSetLoader extends RuleSetLoader {

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

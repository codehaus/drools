package org.drools.io;

/*
 $Id: RuleSetLoader.java,v 1.4 2003-06-19 09:27:10 tdiesler Exp $

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

import org.drools.RuleBase;
import org.drools.rule.RuleSet;
import org.drools.tags.rule.RuleTagLibrary;

import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;

/** Loads <code>RuleSet</code> definitions from XML descriptor.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleSetLoader.java,v 1.4 2003-06-19 09:27:10 tdiesler Exp $
 */
public class RuleSetLoader
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Rule-sets. */
    private List ruleSets;

    /**
     * Cache a list of RuleSet objects, key is the url.
     * This is a temporary workaround for the OutOfMemoryError in OutOfMemory when a script is parsed repeatedly.
     */
    private static HashMap ruleSetCache = new HashMap();

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public RuleSetLoader()
    {
        // intentionally left blank
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Add a <code>RuleSet</code> to this loader.
     *
     *  @param ruleSet The rule-set to add.
     */
    public void addRuleSet(RuleSet ruleSet)
    {
        this.ruleSets.add(ruleSet);
    }

    /** Load <code>RuleSet</code> definitions from a Reader.
     *
     *  @param reader The Reader of the rule-set definitions.
     *
     *  @return The list of loaded rule-sets.
     *
     *  @throws IOException If an IO errors occurs.
     *  @throws Exception If an error occurs evaluating the definition.
     */
    public List load(Reader reader) throws IOException, Exception
    {

        this.ruleSets = new ArrayList();

        XMLParser parser = new XMLParser();

        JellyContext context = new JellyContext();

        context.registerTagLibrary("http://drools.org/rules",
                new RuleTagLibrary());

        context.setVariable("org.drools.io.RuleSetLoader",
                this);

        parser.setContext(context);

        Script script = parser.parse(reader);

        XMLOutput output = XMLOutput.createXMLOutput(System.err,
                false);

        script.run(context,
                output);

        List answer = this.ruleSets;

        this.ruleSets = null;

        return answer;
    }

    /** Load <code>RuleSet</code> definitions from a URL.
     *
     *  @param url The URL of the rule-set definitions.
     *
     *  @return The list of loaded rule-sets.
     *
     *  @throws IOException If an IO errors occurs.
     *  @throws Exception If an error occurs evaluating the definition.
     */
    public List load(URL url) throws IOException, Exception
    {
        // This is a workaround for an OutOfMemoryError in Jelly when a script is parsed repeatedly
        // Note, the rules for a given url cannot be changed after they are chached
        List cachedRuleSets = (List) ruleSetCache.get(url);
        if (cachedRuleSets != null)
        {
            return cachedRuleSets;
        }

        InputStream urlStream = url.openStream();

        try
        {
            List answer = load (new InputStreamReader(urlStream));

            ruleSetCache.put(url, answer);

            return answer;
        }
        finally
        {
            urlStream.close();
        }
    }

    /** Load <code>RuleSet</code> deifnitions from a URL
     *  into a <code>RuleBase</code>.
     *
     *  @param url The URL of the rule-set definitions.
     *  @param ruleBase The rule-base to populate.
     *
     *  @throws IOException If an IO errors occurs.
     *  @throws Exception If an error occurs evaluating the definition.
     */
    public void load(URL url,
                     RuleBase ruleBase) throws IOException, Exception
    {
        List ruleSets = load(url);

        Iterator ruleSetIter = ruleSets.iterator();
        RuleSet eachRuleSet = null;

        while (ruleSetIter.hasNext())
        {
            eachRuleSet = (RuleSet) ruleSetIter.next();

            ruleBase.addRuleSet(eachRuleSet);
        }
    }

    /** Load <code>RuleSet</code> deifnitions from a URL.
     *
     *  @param url The URL of the rule-set definitions.
     *
     *  @return The list of loaded rule-sets.
     *
     *  @throws IOException If an IO errors occurs.
     *  @throws Exception If an error occurs evaluating the definition.
     */
    public List load(String url) throws IOException, Exception
    {
        return load(new URL(url));
    }
}

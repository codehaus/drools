package sisters;

/*
 $Id: JSR94SistersStateful.java,v 1.1 2003-03-22 22:04:21 tdiesler Exp $

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

import org.drools.jsr94.rules.RuleServiceProviderImpl;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.HashMap;

/**
 *
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class JSR94SistersStateful {

    public static final String SISTERS_RULES_URI = "http://drools.org/rules/example/sisters";

    public static void main( String[] args ) throws Exception
    {

        // First, setup the RuleServiceProvider and RuleAdministrator
        RuleServiceProviderManager.registerRuleServiceProvider( "http://drools.org/rules", RuleServiceProviderImpl.class );
        RuleServiceProvider ruleServiceProvider = RuleServiceProviderManager.getRuleServiceProvider( "http://drools.org/rules" );
        RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();

        // Load the rules
        LocalRuleExecutionSetProvider ruleSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider( null );
        Reader ruleReader = new InputStreamReader( JSR94SistersStateful.class.getClassLoader().getResourceAsStream( "sisters/sisters.drl" ) );
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet( ruleReader, null );

        // register the rule set
        ruleAdministrator.registerRuleExecutionSet( SISTERS_RULES_URI, ruleSet, null );

        // Create some application specific data that will be needed by our consequence.
        HashMap map = new HashMap();
        map.put( "message", "This came from the appData HashMap" );

        // obtain the stateless rule session
        RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
        StatefulRuleSession session = (StatefulRuleSession) ruleRuntime.createRuleSession( SISTERS_RULES_URI, map, RuleRuntime.STATEFUL_SESSION_TYPE );

        Person person = new Person( "bob" );
        session.addObject( person );

        person = new Person( "rebecca" );
        person.addSister( "jeannie" );
        session.addObject( person );

        person = new Person( "jeannie" );
        person.addSister( "rebecca" );
        session.addObject( person );

        // execute the rules
        session.executeRules();
        List outList = session.getObjects();

        session.release();

        System.out.println();
        for (int i = 0; i < outList.size(); i++)
        {
            Object obj = outList.get( i );
            System.out.println( "[" + obj.getClass().getName() + "] " + obj );
        }
    }
}

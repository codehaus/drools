package org.drools.io;

/*
 $Id: RuleBaseBuilder.java,v 1.2 2004-01-02 04:20:30 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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
import org.drools.io.RuleSetReader;
import org.drools.rule.RuleSet;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/** Factory for constructing a <code>RuleBase</code>.
 *
 *  <p>
 *  The <code>RuleBaseBuilder</code> integrates the added <code>RuleSet</code>s
 *  into the <b>Rete</b> network.  A <code>RuleBaseBuilder</code> may be re-used
 *  after building a <code>RuleBase</code> but it may not be used to build multiple
 *  <code>RuleBase</code>s simultaneously by multiple threads.
 *  </p>
 *
 *  @see #build
 *  @see RuleSet
 *  @see RuleBase
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleBaseBuilder.java,v 1.2 2004-01-02 04:20:30 bob Exp $
 */
public class RuleBaseBuilder
{
    public static RuleBase buildFromUrl(URL url)
        throws Exception
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( url );

        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();

        builder.addRuleSet( ruleSet );

        return builder.build();
    }

    public static RuleBase buildFromInputStream(InputStream in)
        throws Exception
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( in );

        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();

        builder.addRuleSet( ruleSet );

        return builder.build();
    }

    public static RuleBase buildFromReader(Reader in)
        throws Exception
    {
        RuleSetReader reader = new RuleSetReader();

        RuleSet ruleSet = reader.read( in );

        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();

        builder.addRuleSet( ruleSet );

        return builder.build();
    }
}
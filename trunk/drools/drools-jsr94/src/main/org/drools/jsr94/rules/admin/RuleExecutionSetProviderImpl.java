package org.drools.jsr94.rules.admin;

/*
 $Id: RuleExecutionSetProviderImpl.java,v 1.10 2004-04-07 19:40:03 n_alex Exp $

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
import org.drools.RuleIntegrationException;
import org.drools.smf.DefaultSemanticsRepository;
import org.drools.io.RuleSetReader;
import org.w3c.dom.Element;

import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetProvider;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * The <code>RuleExecutionSetProvider</code>�interface defines <code>RuleExecutionSet</code>�
 * creation methods for defining <code>RuleExecutionSets</code>�
 * from potentially serializable resources.
 *
 * @see RuleExecutionSetProvider
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class RuleExecutionSetProviderImpl implements RuleExecutionSetProvider
{
    /**
     * Creates a <code>RuleExecutionSet</code>�implementation from an XML Document and additional
     * vendor-specific properties.
     *
     * @see RuleExecutionSetProvider#createRuleExecutionSet(Element, Map)
     */
    public RuleExecutionSet createRuleExecutionSet(
            Element element,
            Map properties )
            throws RuleExecutionSetCreateException,
            RemoteException
    {

        // Prepare the DOM source
        Source source = new DOMSource(element);

        RuleSetReader reader = null;
        try
        {
            // Create a reader to handle the SAX events
            reader = new RuleSetReader(DefaultSemanticsRepository.getInstance());
        }
        catch (Exception e)
        {
            throw new RuleExecutionSetCreateException("Couldn't get an instance of the DefaultSemanticsRepository: " + e);
        }

        try
        {
            // Prepare the result
            SAXResult result = new SAXResult(reader);

            // Create a transformer
            Transformer xformer = TransformerFactory.newInstance().newTransformer();

            // Traverse the DOM tree
            xformer.transform(source, result);
            System.out.println(result.toString());

        }
        catch (TransformerException e)
        {
            throw new RuleExecutionSetCreateException( "could not create RuleExecutionSet: " + e );
        }

        RuleBase ruleBase = null;

        try
        {
            org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();
            builder.addRuleSet( reader.getRuleSet() );
            ruleBase = builder.build();
            LocalRuleExecutionSetProviderImpl localRuleExecutionSetProvider = new LocalRuleExecutionSetProviderImpl();
            return localRuleExecutionSetProvider.createRuleExecutionSet( ruleBase, properties );
        }
        catch (RuleIntegrationException e)
        {
            throw new RuleExecutionSetCreateException("could not create RuleExecutionSet: " + e);
        }
    }

    /**
     * <p>Creates a <code>RuleExecutionSet</code>�implementation from a vendor
     * specific Abstract Syntax Tree (AST) representation and vendor-specific
     * properties.</p>
     *
     * <p>This method accepts a <code>org.drools.RuleBase</code> object as its
     * vendor specific AST representation.</p>
     *
     * @see RuleExecutionSetProvider#createRuleExecutionSet(Serializable, Map)
     */
    public RuleExecutionSet createRuleExecutionSet(
            Serializable serializable,
            Map properties )
            throws RuleExecutionSetCreateException,
            RemoteException
    {
        if(serializable instanceof RuleBase)
        {
            LocalRuleExecutionSetProviderImpl localRuleExecutionSetProvider = new LocalRuleExecutionSetProviderImpl();
            return localRuleExecutionSetProvider.createRuleExecutionSet( serializable, properties );
        }
        else
        {
            throw new IllegalArgumentException("Serializable object must be " +
                    "an instance of org.drools.RuleBase.  It was "
                    + serializable.getClass().getName());
        }
    }

    /**
     * Creates a <code>RuleExecutionSet</code>�implementation from a URI.
     *
     * @see RuleExecutionSetProvider#createRuleExecutionSet(String,Map)
     */
    public RuleExecutionSet createRuleExecutionSet(
            String ruleExecutionSetUri,
            Map properties)
            throws RuleExecutionSetCreateException,
            IOException,
            RemoteException
    {
        InputStream in = null;
        try
        {
            LocalRuleExecutionSetProviderImpl localRuleExecutionSetProvider = new LocalRuleExecutionSetProviderImpl();
            in = new URL( ruleExecutionSetUri ).openStream();
            Reader reader = new InputStreamReader(in);
            return localRuleExecutionSetProvider.createRuleExecutionSet( reader, properties );
        }
        catch ( IOException ex )
        {
            throw ex;
        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetCreateException( "cannot create rule set", ex );
        }
        finally
        {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
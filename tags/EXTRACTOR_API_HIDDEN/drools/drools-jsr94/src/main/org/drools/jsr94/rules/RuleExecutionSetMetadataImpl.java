package org.drools.jsr94.rules;

/*
 * $Id: RuleExecutionSetMetadataImpl.java,v 1.5 2004-11-15 01:12:22 dbarnett Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import javax.rules.RuleExecutionSetMetadata;

/**
 * The Drools implementation of the <code>RuleExecutionSetMetadata</code>
 * interface which exposes some simple properties of the
 * <code>RuleExecutionSet</code> to the runtime user.
 *
 * @see RuleExecutionSetMetadata
 */
public class RuleExecutionSetMetadataImpl implements RuleExecutionSetMetadata
{
    /** The URI for this <code>RuleExecutionSet</code>. */
    private String uri = null;

    /** The name of this RuleExecutionSet. */
    private String name = null;

    /** The description of this <code>RuleExecutionSet</code>. */
    private String description = null;

    /**
     * Constructs an instance of <code>RuleExecutionSetMetadata</code>.
     *
     * @param uri The URI for this <code>RuleExecutionSet</code>.
     * @param name The name of this <code>RuleExecutionSet</code>.
     * @param description The description of this <code>RuleExecutionSet</code>.
     */
    public RuleExecutionSetMetadataImpl(
        String uri, String name, String description )
    {
        this.uri = uri;
        this.name = name;
        this.description = description;
    }

    /**
     * Get the URI for this <code>RuleExecutionSet</code>.
     *
     * @return The URI for this <code>RuleExecutionSet</code>.
     */
    public String getUri( )
    {
        return uri;
    }

    /**
     * Get the name of this <code>RuleExecutionSet</code>.
     *
     * @return The name of this <code>RuleExecutionSet</code>.
     */
    public String getName( )
    {
        return name;
    }

    /**
     * Get a short description about this <code>RuleExecutionSet</code>.
     *
     * @return The description of this <code>RuleExecutionSet</code>
     *         or <code>null</code>.
     */
    public String getDescription( )
    {
        return description;
    }
}

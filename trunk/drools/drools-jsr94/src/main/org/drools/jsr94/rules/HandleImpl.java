package org.drools.jsr94.rules;

/*
 $Id: HandleImpl.java,v 1.4 2003-06-19 09:28:35 tdiesler Exp $

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

import javax.rules.Handle;
import java.io.Serializable;
import java.math.BigInteger;


/**
 * Implements the marker interface for vendor specific object identity mechanism.
 * When using the <code>StatefulRuleSession</code> objects that are added to working memory are
 * identified using a vendor supplied <code>Handle</code> implementation.
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class HandleImpl implements Handle, Serializable
{

    // the id of this handle
    private BigInteger handleid;

    /**
     * Hide constructor, use getInstance.
     *
     */
    HandleImpl( BigInteger handleid )
    {
        this.handleid = handleid;
    }

    /**
     * Returns true if the two handles are equal.
     */
    public boolean equals( Object obj )
    {
        if ( this == obj ) return true;
        if ( !( obj instanceof HandleImpl ) ) return false;
        HandleImpl handle = (HandleImpl) obj;
        if ( !handleid.equals( handle.handleid ) ) return false;
        return true;
    }

    /** The hash code of a handle. */
    public int hashCode()
    {
        return handleid.hashCode();
    }

    /**
     * Gets the string representation of this object.
     */
    public String toString()
    {
        return "HDL-" + handleid;
    }
}

package org.drools.semantics.python;

/*
 * $Id: PythonFunctions.java,v 1.3 2005-11-10 04:54:43 mproctor Exp $
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

import org.drools.smf.SemanticComponent;
import org.drools.spi.Functions;

/**
 * Python block semantics <code>Functions</code>.
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * 
 * @version $Id: PythonFunctions.java,v 1.3 2005-11-10 04:54:43 mproctor Exp $
 */
public class PythonFunctions implements Functions, SemanticComponent
{
    private String text;

    protected final String        semanticType    = "python";

    protected final String        name;    
    
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param text The block text.
     */
    public PythonFunctions(String name, String text)
    {
        this.text = PythonInterp.stripOuterIndention( text );
        this.name = "functions";        
    }
    
    public String getSemanticType()
    {
        return semanticType;
    }

    public String getName()
    {
        return this.name;
    }       

    public String getText()
    {
        return this.text;
    }
    
    public String getSemantic()
    {
        return "python";
    }
}


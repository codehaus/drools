package org.drools.semantics.java;

/*
 $Id: Interp.java,v 1.21 2004-07-28 13:55:41 mproctor Exp $

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

import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;
import org.drools.spi.KnowledgeHelper;
import org.drools.WorkingMemory;

import net.janino.EvaluatorBase;

import java.lang.reflect.InvocationTargetException;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.StringTokenizer;

import java.io.Serializable;

/** Base class for BeanShell interpreter-based Java semantic components.
 *
 *  @see ExprCondition
 *  @see ExprExtractor
 *  @see BlockConsequence
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Interp.java,v 1.21 2004-07-28 13:55:41 mproctor Exp $
 */
public class Interp implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Interpreted text. */
    private String imports;
    private String text;

    private String newline = System.getProperty("line.separator");

    //private EvaluatorBase code;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     */
    protected Interp()
    {
    }


    protected Interp(String text)
   {
        StringTokenizer st = new StringTokenizer(text, newline, true);
        int i = 0;
        int k = 0;
        //get last import
        while (st.hasMoreTokens())
        {
            if (st.nextToken().trim().startsWith("import"))
            {
                i++;
            }
            k++;
        }

        StringBuffer newImports = new StringBuffer();
        StringBuffer newText = new StringBuffer();
        st = new StringTokenizer(text, newline, true);
        int j = 0;
        while (st.hasMoreTokens())
        {
            if ((i != 0)&&(j <= i+1))
            //if (j <= i)
            {
                newImports.append(st.nextToken());
            } else
            {
                newText.append(st.nextToken());
            }
            j++;
        }
        newImports.append(newline);
        this.imports = newImports.toString();
        if (this instanceof ExprCondition)
        {
            this.text = "return (" + newText.toString().trim() + ");" + newline;
        }
        else if (this instanceof ExprExtractor)
        {
            this.text = "return (" + newText.toString().trim() + ");" + newline;
        }
        else if (this instanceof BlockConsequence)
        {
            this.text = newText.toString();
        }
    }

    //protected void setCode(EvaluatorBase code)
    //{
    //    this.code = code;
    //}

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    public String getPreparedText(Tuple tuple, Declaration[] availDecls)
    {
        WorkingMemory workingMemory = tuple.getWorkingMemory();
        Map appData = workingMemory.getApplicationDataMap();
        StringBuffer buffer = new StringBuffer();
        ObjectType objectType = null;
        Declaration eachDecl = null;
        Declaration[] params = availDecls;

        String type;
        int nestedClassPosition;
        for ( int i = 0 ; i < params.length; i++ ) {
            eachDecl = params[i];
            objectType = eachDecl.getObjectType();

            //Import classes for each of the declarations

            if ( objectType instanceof ClassObjectType )
            {
                type = ((ClassObjectType)objectType).getType().getName();

                nestedClassPosition = type.indexOf('$');

                if (nestedClassPosition != -1)
                {
                    type = type.substring(0, nestedClassPosition);
                }

                if (type.indexOf("java.lang") == -1)
                {
                    buffer.append("import " + type + ";" + newline);
                }

                if ( objectType instanceof ExtraImports )
                {
                    String[] extra = ((ExtraImports)objectType).getExtraImports();

                    for ( int e = 0 ; e < extra.length ; ++e )
                    {
                        if (extra[e].indexOf("java.lang") == -1)
                        {
                            buffer.append( "import " + extra[e] + ";" + newline );
                        }
                    }
                }
            }
        }

        Set keys = appData.keySet();
        Iterator it = keys.iterator();
        String key;
        Object object;
        while (it.hasNext())
        {
            object = appData.get(it.next());
            type = object.getClass().getName();

            nestedClassPosition = type.indexOf('$');
            if (nestedClassPosition != -1)
            {
                type = type.substring(0, nestedClassPosition);
            }

            if (type.indexOf("java.lang") == -1)
            {
                buffer.append("import " + type + ";" + newline);
            }
        }
        buffer.append(imports);

        //assign declarations
        for ( int i = 0 ; i < availDecls.length ; i++ ) {
            objectType =  availDecls[i].getObjectType();
            buffer.append(((ClassObjectType)objectType).getType().getName());
            buffer.append(" " + availDecls[i].getIdentifier() + " = ");
            buffer.append("(" + ((ClassObjectType)objectType).getType().getName() + ")");
            buffer.append("tuple.get(  decls[" + i + "] );" + newline);
        }

        //assign application data
        it = keys.iterator();
        while (it.hasNext())
        {
            key = (String) it.next();
            //only do the cast if variable exists in the block
            if (text.indexOf(key) != -1)
            {
                object = appData.get(key);
                buffer.append(object.getClass().getName());
                buffer.append(" " + key + " = ");
                buffer.append("(" + object.getClass().getName() + ")applicationData.get(\"" + key + "\");" + newline);
            }
        }

        String script = buffer.append(text).toString();
        //System.err.println(script);
        return script;
    }

     /** Retrieve the text to evaluate.
     *
     *  @return The text to evaluate.
     */
    public String getText()
    {
        return this.text;
    }

    /** Retrieve the imports for the evaluation
    *
    *  @return The imports for evaluation.
    */
   public String getImports()
   {
       return this.imports;
   }

    public String toString()
    {
        return "[[ " + this.imports + this.text + " ]]";
    }

}

package org.drools;

/*
 * $Id: RuleBase.java,v 1.38 2005-11-05 04:23:57 michaelneale Exp $
 *
 * Copyright 2001-2004 (C) The Werken Company. All Rights Reserved.
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

import java.io.Serializable;
import java.util.List;

import org.drools.reteoo.FactHandleFactory;
import org.drools.spi.ConflictResolver;
import org.drools.spi.RuleBaseContext;

/**
 * Active collection of <code>Rule</code>s.
 *
 * <p>
 * From a <code>RuleBase</code> many <code>WorkingMemory</code> rule
 * sessions may be instantiated. Additionally, it may be inspected to determine
 * which <code>RuleSet</code> s it contains.
 * </p>
 *
 * @see WorkingMemory
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * @version $Id: RuleBase.java,v 1.38 2005-11-05 04:23:57 michaelneale Exp $
 */
public interface RuleBase
    extends
    Serializable
{
    /**
     * Create a new <code>WorkingMemory</code> session for this
     * <code>RuleBase</code>.
     *
     * <p>
     * The created <code>WorkingMemory</code> uses the default conflict
     * resolution strategy.
     * </p>
     *
     * @see WorkingMemory
     * @see org.drools.conflict.DefaultConflictResolver
     *
     * @return A newly initialized <code>WorkingMemory</code>.
     */
    WorkingMemory newWorkingMemory( );

    /**
     * Retrieve the <code>ConflictResolver</code>.
     *
     * @return The conflict resolution strategy.
     */
    ConflictResolver getConflictResolver( );

    /**
     * Retrieve the <code>FactHandleFactor</code>.
     *
     * @return The fact handle factory.
     */
    FactHandleFactory getFactHandleFactory( );

    List getRuleSets( );
    
    RuleBaseContext getRuleBaseContext( );
    
    /**
     * Get or create a <code>WorkingMemory</code> session for this
     * <code>RuleBase</code> and the current thread.
     * 
     * <p>
     * This is useful for web application users, and allows a long running working memory 
     * that will be garbage collected when the thread ends. Uses a <code>ThreadLocal</code> instance.
     * </p>
     *
     * <p>
     * The created <code>WorkingMemory</code> uses the default conflict
     * resolution strategy.
     * </p>
     *
     * @see WorkingMemory
     * @see org.drools.conflict.DefaultConflictResolver
     *
     * @return the current threads <code>WorkingMemory</code> or a new one if the current thread did
     * not have a working memory assigned.
     */    
    WorkingMemory getCurrentThreadWorkingMemory();
}

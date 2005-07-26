package org.drools.spi;

/*
 * $Id: KnowledgeHelper.java,v 1.1 2005-07-26 01:06:32 mproctor Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

import java.util.List;

import org.drools.FactException;

/**
 * KnowledgeHelper implementation types are injected into consequenses
 * instrumented at compile time and instances passed at runtime. It provides
 * convenience methods for users to interact with the WorkingMemory.
 * <p>
 * Of particular interest is the modifyObject method as it allows an object to
 * be modified without having to specify the facthandle, because they are not
 * passed to the consequence at runtime. To achieve this the implementation will
 * need to lookup the fact handle of the object form the WorkingMemory.
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * @author <a href="mailto:mproctor@codehaus.org">mark proctor</a>
 */
public interface KnowledgeHelper
{
    /**
     * Asserts an object, notice that it does not return the FactHandle
     * 
     * @param object -
     *            the object to be asserted
     * @throws FactException -
     *             Exceptions can be thrown by conditions which are wrapped and
     *             returned as a FactException
     */
    void assertObject(Object object) throws FactException;

    /**
     * Asserts an object specifying that it implement the onPropertyChange
     * listener, notice that it does not return the FactHandle.
     * 
     * @param object -
     *            the object to be asserted
     * @param dynamic -
     *            specifies the object implements onPropertyChangeListener
     * @throws FactException -
     *             Exceptions can be thrown by conditions which are wrapped and
     *             returned as a FactException
     */
    void assertObject(Object object,
                      boolean dynamic) throws FactException;

    /**
     * Modifies an object. Notice that the FactHandle is not passed so the
     * implementation must lookup the FactHandle form the working memory.
     * 
     * @param object -
     *            the object to be modified
     * @throws FactException -
     *             Exceptions can be thrown by conditions which are wrapped and
     *             returned as a FactException
     */
    void modifyObject(Object object) throws FactException;

    /**
     * Modifies an object by looking up the handle of the oldObject and
     * replacing the oldObject with the newObject for the FactHandle. This is
     * used for updating immutable objects.
     * 
     * @param oldObject -
     *            The old object to be modified
     * @param newObject -
     *            the new modified object
     * @throws FactException
     */
    void modifyObject(Object oldObject,
                      Object newObject) throws FactException;

    /**
     * Retracts an object from the WorkingMemory. All Activations on the Agenda
     * that are cancelled should emit ActivationCancelled events.
     * 
     * @param object -
     *            the object to be retracted.
     * @throws FactException -
     *             Wraps and returns any exception that may occur.
     */
    void retractObject(Object object) throws FactException;

    /**
     * @return - The rule name
     */
    String getRuleName();

    /** @return - A List of the objects in the WorkingMemory */
    List getObjects();

    /**
     * Retruns a List of Objects that match the given Class in the paremeter.
     * 
     * @param objectClass -
     *            The Class to filter by
     * @return - All the Objects in the WorkingMemory that match the given Class
     *         filter
     */
    List getObjects(Class objectClass);

    /**
     * Clears the agenda causing all existing Activations to fire
     * ActivationCancelled events. <br>
     */
    void clearAgenda();
}

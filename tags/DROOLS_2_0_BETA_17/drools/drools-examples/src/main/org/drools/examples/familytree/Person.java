package org.drools.examples.familytree;

/*
$Id: Person.java,v 1.1 2004-07-07 04:45:21 dbarnett Exp $

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

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

/**
 * @author searle
 */
public class Person implements Serializable {

    private final String name;
    private boolean isMale;
    final private Set children = new HashSet();

    /**
     * Constructor.
     * @param n the name of the person
     */
    public Person(final String n, boolean isMale)
    {
        super();
        name = n;
        this.isMale=isMale;
    }

    /**
     * Indicates whether the object equals the receiver.
     * @param obj the object to compare this object with
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object obj)
    {
        if (obj.getClass().getName().equals(this.getClass().getName()))
        {
            Person p = (Person) obj;

          if (p.name == null)
          {
              return name == null;
          } else {
              return p.name.equals(name);
          }
        }

        return false;
    }

    /**
     * Get the name.
     * @return the name of the person
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the hash code of the object.
     * @return the hash value
     */
    public int hashCode()
    {
        if (name == null) {
            return 0;
        } else {
            return name.hashCode();
        }
    }

    /**
     * Get the children of this person.
     * @return an empty set if no children
     */
    public Set getChildren()
    {
        return children;
    }

    /**
     * Method hasChild.
     * @param child possible child of this person
     * @return true if this person has the child
     */
    public boolean hasChild (Person child)
    {
        if (children.isEmpty())  return false;
        return children.contains(child);
    }

    /**
     * Method isMale.
     * @return true if the person is Male
     */
    public boolean isMale()
    {
        return isMale;
    }
    /**
     * Convert the receiver to a string.
     * @return the string representation of this object
     */
    public String toString()
    {
          return name;
    }
}

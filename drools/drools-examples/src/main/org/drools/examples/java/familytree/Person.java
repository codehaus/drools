package org.drools.examples.java.familytree;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

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

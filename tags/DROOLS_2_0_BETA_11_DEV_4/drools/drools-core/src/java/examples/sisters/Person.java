package sisters;

import java.util.Set;
import java.util.HashSet;

public class Person
{
    private String name;
    private Set sisters;

    public Person(String name)
    {
        this.name = name;
        this.sisters = new HashSet();
    }

    public String getName()
    {
        return this.name;
    }

    public void addSister(String name)
    {
        this.sisters.add( name );
    }

    public boolean hasSister(Person person)
    {
        return this.sisters.contains( person.getName() );
    }

    public String toString()
    {
        return this.name;
    }
}

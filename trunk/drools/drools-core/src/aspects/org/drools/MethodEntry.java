package org.drools;

public class MethodEntry implements Comparable
{
    private String method;
    private int count;

    public MethodEntry(String method, int count)
    {
        this.method = method;
        this.count = count;
    }

    public String getMethod()
    {
      return this.method;
    }

    public int getCount()
    {
      return this.count;
    }

    public int compareTo(Object o) throws ClassCastException
    {
        int a = this.count;
        int b = ((MethodEntry) o).getCount();
        if (a > b)
        {
          return -1;
        }
        else if (a == b)
        {
          return 0;
        }
        else
        {
          return 1;
        }
    }

    public int hashCode()
    {
      return this.method.hashCode();
    }

    public boolean equals(Object object)
    {
        if (object == null) return false;
        if (!(object instanceof MethodEntry)) return false;
        MethodEntry otherEntry = (MethodEntry) object;
        if (otherEntry.getMethod().equals(this.method)
            &&(otherEntry.getCount() == this.count))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
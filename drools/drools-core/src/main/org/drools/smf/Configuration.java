package org.drools.smf;

public interface Configuration
{
    static final Configuration[] EMPTY_ARRAY = new Configuration[0];

    String getName();
    String getText();
    String getAttribute(String name);
    String[] getAttributeNames();
    Configuration getChild(String name);
    Configuration[] getChildren(String name);
    Configuration[] getChildren();
}

package org.drools.jsr94.rules;

import javax.rules.RuleExecutionSetMetadata;

/**
 * @author Andy Barnett
 */
public class RuleExecutionSetMetadataImpl implements RuleExecutionSetMetadata
{

    private String uri         = null;

    private String name        = null;

    private String description = null;

    public RuleExecutionSetMetadataImpl(String uri,
                                        String name,
                                        String description)
    {
        super( );

        this.uri = uri;
        this.name = name;
        this.description = description;
    }

    public String getUri()
    {
        return uri;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

}
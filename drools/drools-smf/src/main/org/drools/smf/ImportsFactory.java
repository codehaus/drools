package org.drools.smf;

import org.drools.rule.Imports;

public interface ImportsFactory
{
    Imports newImports(Configuration config) throws FactoryException;
}
package org.drools.io;

import org.drools.DroolsException;

public class ReaderMultiException
    extends DroolsException
{
    private Throwable[] errors;

    public ReaderMultiException(Throwable[] errors)
    {
        this.errors = errors;
    }

    public Throwable[] getErrors()
    {
        return this.errors;
    }
}

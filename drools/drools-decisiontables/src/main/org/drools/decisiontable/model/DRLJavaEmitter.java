/*
 * Created on 14/05/2005
 * 
 * Copyright 2005 (C) Michael D Neale. All Rights Reserved.
 *
 *	 Redistribution and use of this software and associated documentation
 *	 ("Software"), with or without modification, are permitted provided
 *	 that the following conditions are met:
 *
 *	 1. Redistributions of source code must retain copyright
 *	    statements and notices.  Redistributions must also contain a
 *	    copy of this document.
 *	 2. Due credit should be given to Michael D Neale.	     
 */
package org.drools.decisiontable.model;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * Classes that implement this interface should generate DRL fragments according
 * to the drools java semantic module.
 * 
 * TODO: change this to use a stream (optionally) that is injected. This can
 * allow a large chunk of DRL to be written out to a temp file, and then a
 * stream to this returned for input into RuleBaseLoader.
 */
public interface DRLJavaEmitter
{

    /**
     * 
     * @return an XML snippet (well formed) for the current object. This should
     *         NOT be a complete XML document, just well formed.
     * 
     */
    String toXML();

}

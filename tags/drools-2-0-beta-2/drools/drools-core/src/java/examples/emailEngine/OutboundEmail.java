package emailEngine;

/**
 * A single outbound email.  As the Drools engine uses the object
 * class type to help descriminate which rules should be executed we
 * wrap all important events with thier own classes.  Since an outbound
 * email requires no additional methods or attributes we simply extend
 * from the base Email class.
 *
 * <P>
 * $Id: OutboundEmail.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public class OutboundEmail extends Email
   {
   }

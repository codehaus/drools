package org.drools.jsr94.jca.spi;

import org.drools.jsr94.rules.RuleServiceProviderImpl;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.cci.*;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * ConnectionFactory provides an interface for getting connection to an EIS instance.
 * An implementation of ConnectionFactory interface is provided by a resource adapter.
 * <p>
 * Application code looks up a ConnectionFactory instance from JNDI namespace and uses it to get EIS connections.
 * <p>
 * An implementation class for ConnectionFactory is required to implement java.io.Serializable and
 * javax.resource.Referenceableinterfaces to support JNDI registration.
 * <p>
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class RuleConnectionFactory extends RuleServiceProviderImpl implements ConnectionFactory, Serializable, Referenceable {

   private String desc;
   private ManagedConnectionFactory mcf;
   private ConnectionManager cm;
   private Reference reference;
   private PrintWriter logWriter;

   public RuleConnectionFactory(ManagedConnectionFactory mcf, ConnectionManager cm) throws ResourceException {
      this.mcf = mcf;
      this.cm = cm;
      this.logWriter = mcf.getLogWriter();
      logWriter.println("RuleConnectionFactory");
   }

   /**
    * ConnectionFactory provides an interface for getting connection to an EIS instance.
    * An implementation of ConnectionFactory interface is provided by a resource adapter.
    * <p>
    * Application code looks up a ConnectionFactory instance from JNDI namespace and uses it to get EIS connections.
    * <p>
    * An implementation class for ConnectionFactory is required to implement java.io.Serializable and
    * javax.resource.Referenceableinterfaces to support JNDI registration.
    */
   public Connection getConnection() throws ResourceException {
      logWriter.println("RuleConnectionFactory.getConnection,1");
      return null;
   }

   /**
    * Gets a connection to an EIS instance. A component should use the getConnection variant with
    * javax.resource.cci.ConnectionSpec parameter, if it needs to pass any resource adapter specific security
    * information and connection parameters. the component- managed sign-on case, an application component passes
    * security information (example: username, password) through the ConnectionSpec instance.
    * <p>
    * It is important to note that the properties passed through the getConnection method should be client-specific
    * (example: username, password, language) and not related to the configuration of a target EIS instance
    * (example: port number, server name). The ManagedConnectionFactory instance is configured with complete set of
    * properties required for the creation of a connection to an EIS instance.
    */
   public Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException {
      logWriter.println("RuleConnectionFactory.getConnection,2");
      return null;
   }

   /**
    * Gets a RecordFactory instance. The RecordFactory is used for the creation of generic Record instances.
    */
   public RecordFactory getRecordFactory() throws ResourceException {
      logWriter.println("RuleConnectionFactory.getRecordFactory");
      return null;
   }

   /**
    * Gets metadata for the Resource Adapter. Note that the metadata information is about the ResourceAdapter
    * and not the EIS instance. An invocation of this method does not require that an active connection to an EIS
    * instance should have been established.
    */
   public ResourceAdapterMetaData getMetaData() throws ResourceException {
      logWriter.println("RuleConnectionFactory.getMetaData");
      return null;
   }

   /**
    * Sets the Reference instance. This method is called by the deployment code to set the Reference that can be
    * later returned by the getReference method (as defined in the javax.naming.Referenceable interface).
    */
   public void setReference(Reference reference) {
      logWriter.println("RuleConnectionFactory.setReference");
      this.reference = reference;
   }

   /**
    * Retrieves the Reference of this object.
    */
   public Reference getReference() throws NamingException {
      logWriter.println("RuleConnectionFactory.getReference");
      return reference;
   }

}
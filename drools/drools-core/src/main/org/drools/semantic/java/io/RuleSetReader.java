
package org.drools.semantic.java.io;

import org.drools.spi.RuleSet;
import org.drools.spi.Rule;
import org.drools.spi.Declaration;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.Map;
import java.util.HashMap;

/** Reads Java XML ruleset files, producing a {@link RuleSet}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class RuleSetReader
{
    private SAXReader reader;

    private ImportManager importManager;
    private RuleSet       ruleSet;
    private Rule          currentRule;

    private Map           decls;

    /** Construct.
     *
     *  <p>
     *  A <code>RuleSetReader</code> may be re-used repeatedly,
     *  but may <b>not</b> be used by two threads simultaneously.
     *  It is not synchronized nor re-entrant.
     *  </p>
     */
    public RuleSetReader()
    {
        this.importManager = new ImportManager();
        this.reader        = new SAXReader();

        this.reader.addHandler( "/ruleset",
                                new RuleSetHandler( this ) );

        this.reader.addHandler( "/ruleset/import",
                                new ImportHandler( this ) );

        this.reader.addHandler( "/ruleset/rule",
                                new RuleHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/param",
                                new ParamHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/decl",
                                new DeclHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/when/cond",
                                new CondHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/when/duration",
                                new DurationHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/then",
                                new ThenHandler( this ) );

        this.decls = new HashMap();
    }

    void addDeclaration(Declaration decl)
    {
        this.decls.put( decl.getIdentifier(),
                             decl );
    }

    Declaration getDeclaration(String identifier)
    {
        return (Declaration) this.decls.get( identifier );
    }

    Rule getCurrentRule()
    {
        return this.currentRule;
    }

    void setCurrentRule(Rule currentRule)
    {
        this.currentRule = currentRule;
    }

    ImportManager getImportManager()
    {
        return this.importManager;
    }

    void setRuleSet(RuleSet ruleSet)
    {
        this.ruleSet = ruleSet;
    }

    RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    /** Read a Java ruleset XML file from a URL.
     *
     *  @param url The URL of the XML ruleset.
     *
     *  @return The resulting {@link RuleSet}.
     *
     *  @throws RuleSetReaderException if an error occurs
     *          while reading or parsing the file, or
     *          constructing the ruleset or rules.
     */
    public RuleSet read(URL url) throws RuleSetReaderException
    {
        reset();

        try
        {
            Document doc = this.reader.read( url );
        }
        catch (DocumentException e)
        {
            throw new RuleSetReaderException( e );
        }

        return getRuleSet();
    }

    /** Read a Java ruleset XML file from a local file.
     *
     *  @param file The File of the XML ruleset.
     *
     *  @return The resulting {@link RuleSet}.
     *
     *  @throws RuleSetReaderException if an error occurs
     *          while reading or parsing the file, or
     *          constructing the ruleset or rules.
     */
    public RuleSet read(File file) throws MalformedURLException, RuleSetReaderException
    {
        reset();
        return read( file.toURL() );
    }

    /** Read a Java ruleset XML file from a character stream.
     *
     *  @param charStream The character stream containing the XML ruleset.
     *
     *  @return The resulting {@link RuleSet}.
     *
     *  @throws RuleSetReaderException if an error occurs
     *          while reading or parsing the file, or
     *          constructing the ruleset or rules.
     */
    public RuleSet read(Reader charStream) throws RuleSetReaderException
    {
        reset();

        try
        {
            Document doc = this.reader.read( charStream );
        }
        catch (DocumentException e)
        {
            throw new RuleSetReaderException( e );
        }

        return getRuleSet();
    }

    /** Read a Java ruleset XML file from a stream of bytes.
     *
     *  @param byteStream The byte stream containing the XML ruleset.
     *
     *  @return The resulting {@link RuleSet}.
     *
     *  @throws RuleSetReaderException if an error occurs
     *          while reading or parsing the file, or
     *          constructing the ruleset or rules.
     */
    public RuleSet read(InputStream byteStream) throws RuleSetReaderException
    {
        reset();

        try
        {
            Document doc = this.reader.read( byteStream );
        }
        catch (DocumentException e)
        {
            throw new RuleSetReaderException( e );
        }

        return getRuleSet();
    }

    /**
     * Reset the environment to prepare for importing
     * a new set of rules.
     */

    private void reset() 
      {
        getImportManager().reset();
        setCurrentRule( null );
        setRuleSet( null );
      }
}

package org.drools.tags.ruleset;

import org.drools.smf.SemanticModule;
import org.drools.spi.ObjectType;
import org.drools.spi.Condition;
import org.drools.spi.FactExtractor;
import org.drools.spi.Consequence;

import org.apache.commons.beanutils.ConvertingWrapDynaBean;
import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.DynaBeanTagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.impl.DynamicTagLibrary;
import org.apache.commons.jelly.impl.TagFactory;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

class SemanticsTagLibrary extends DynamicTagLibrary
{
    private SemanticModule module;

    SemanticsTagLibrary(SemanticModule module) throws Exception
    {
        super( module.getUri() );
        this.module = module;

        registerObjectTypes();
        registerConditions();
        registerExtractions();
        registerConsequences();
    }

    protected SemanticModule getSemanticModule()
    {
        return this.module;
    }

    protected void registerObjectTypes() throws Exception
    {
        Set names = getSemanticModule().getObjectTypeNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getObjectType( eachName );

            registerObjectType( eachName,
                                eachClass );
        }
    }

    protected void registerObjectType(final String name,
                                      final Class beanClass) throws Exception
    {
        registerBeanTag( name,
                         new TagFactory()
                         {
                             public Tag createTag() throws Exception
                             {
                                 Tag tag =
                                     new DynaBeanTagSupport()
                                     {
                                         private ObjectType objectType;
                                         
                                         public void beforeSetAttributes() throws Exception
                                         {
                                             try
                                             {
                                                 this.objectType = (ObjectType) beanClass.newInstance();
                                                 setDynaBean( new ConvertingWrapDynaBean( this.objectType ) );
                                             }
                                             catch (Exception e)
                                             {
                                                 throw new JellyException( "Unable to instantiate: " + beanClass.getName() );
                                             }
                                         }

                                         public void doTag(XMLOutput output)  throws Exception
                                         {
                                             DeclarationTag tag = (DeclarationTag) findAncestorWithClass( DeclarationTag.class );

                                             if ( tag == null )
                                             {
                                                 throw new JellyException( "No delcaration for object type" );
                                             }

                                             tag.setObjectType( this.objectType );
                                         }
                                     };

                                 return tag;
                             }
                         }
                         );
    }

    protected void registerConditions() throws Exception
    {
        Set names = getSemanticModule().getConditionNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getCondition( eachName );

            registerCondition( eachName,
                                eachClass );
        }
    }

    protected void registerCondition(final String name,
                                     final Class beanClass) throws Exception
    {
        registerBeanTag( name,
                         new TagFactory()
                         {
                             public Tag createTag() throws Exception
                             {
                                 Tag tag =
                                     new DynaBeanTagSupport()
                                     {
                                         private Condition condition;
                                         
                                         public void beforeSetAttributes() throws Exception
                                         {
                                             try
                                             {
                                                 this.condition = (Condition) beanClass.newInstance();
                                                 setDynaBean( new ConvertingWrapDynaBean( this.condition ) );
                                             }
                                             catch (Exception e)
                                             {
                                                 throw new JellyException( "Unable to instantiate: " + beanClass.getName() );
                                             }
                                         }

                                         public void doTag(XMLOutput output)  throws Exception
                                         {
                                             ConditionTag tag = (ConditionTag) findAncestorWithClass( ConditionTag.class );

                                             if ( tag == null )
                                             {
                                                 throw new JellyException( "No wrapper for condition" );
                                             }

                                             tag.setCondition( this.condition );
                                         }
                                     };

                                 return tag;
                             }
                         }
                         );
    }

    protected void registerExtractions() throws Exception
    {
        Set names = getSemanticModule().getFactExtractorNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getFactExtractor( eachName );

            registerExtraction( eachName,
                                eachClass );
        }
    }

    protected void registerExtraction(final String name,
                                      final Class beanClass) throws Exception
    {
        registerBeanTag( name,
                         new TagFactory()
                         {
                             public Tag createTag() throws Exception
                             {
                                 Tag tag =
                                     new DynaBeanTagSupport()
                                     {
                                         private FactExtractor extractor;
                                         
                                         public void beforeSetAttributes() throws Exception
                                         {
                                             try
                                             {
                                                 this.extractor = (FactExtractor) beanClass.newInstance();
                                                 setDynaBean( new ConvertingWrapDynaBean( this.extractor ) );
                                             }
                                             catch (Exception e)
                                             {
                                                 throw new JellyException( "Unable to instantiate: " + beanClass.getName() );
                                             }
                                         }

                                         public void doTag(XMLOutput output)  throws Exception
                                         {
                                             ExtractionTag tag = (ExtractionTag) findAncestorWithClass( ExtractionTag.class );

                                             if ( tag == null )
                                             {
                                                 throw new JellyException( "No wrapper for extraction" );
                                             }

                                             tag.setFactExtractor( this.extractor );
                                         }
                                     };

                                 return tag;
                             }
                         }
                         );
    }

    protected void registerConsequences() throws Exception
    {
        Set names = getSemanticModule().getConsequenceNames();

        Iterator nameIter  = names.iterator();
        String   eachName  = null;
        Class    eachClass = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            eachClass = getSemanticModule().getConsequence( eachName );

            registerConsequence( eachName,
                                 eachClass );
        }
    }

    protected void registerConsequence(final String name,
                                       final Class beanClass) throws Exception
    {
        registerBeanTag( name,
                         new TagFactory()
                         {
                             public Tag createTag() throws Exception
                             {
                                 Tag tag =
                                     new DynaBeanTagSupport()
                                     {
                                         private Consequence consequence;
                                         
                                         public void beforeSetAttributes() throws Exception
                                         {
                                             try
                                             {
                                                 this.consequence = (Consequence) beanClass.newInstance();
                                                 setDynaBean( new ConvertingWrapDynaBean( this.consequence ) );
                                             }
                                             catch (Exception e)
                                             {
                                                 throw new JellyException( "Unable to instantiate: " + beanClass.getName() );
                                             }
                                         }

                                         public void doTag(XMLOutput output)  throws Exception
                                         {
                                             ConsequenceTag tag = (ConsequenceTag) findAncestorWithClass( ConsequenceTag.class );

                                             if ( tag == null )
                                             {
                                                 throw new JellyException( "No wrapper for consequence" );
                                             }

                                             tag.setConsequence( this.consequence );
                                         }
                                     };

                                 return tag;
                             }
                         }
                         );
    }
}

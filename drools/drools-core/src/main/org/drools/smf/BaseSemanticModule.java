package org.drools.smf;

public class BaseSemanticModule
    extends SimpleSemanticModule
{
    public static final String NAMESPACE_URI = "http://drools.org/rules";

    private static final BaseSemanticModule INSTANCE = new BaseSemanticModule();

    public static BaseSemanticModule getInstance()
    {
        return INSTANCE;
    }

    protected BaseSemanticModule()
    {
        super( NAMESPACE_URI );

        addRuleFactory( "rule",
                        new BaseRuleFactory() );
    }
}

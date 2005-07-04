package org.drools.spring.metadata;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NameMatchMethodMetadataSource implements MethodMetadataSource{

    static final MethodMetadata METHOD_CONDITION_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONDITION);
    static final MethodMetadata METHOD_CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONSEQUENCE);
    static final MethodMetadata OBJECT_CONDITION_METADATA = new MethodMetadata(MethodMetadata.OBJECT_CONDITION);
    
    private Map nameMap = new HashMap(); // Map<String methodName, MetadataInfo>

    public MethodMetadata getMethodMetadata(Method method) {
        return (MethodMetadata) nameMap.get(method.getName());
    }

    /**
     * Set the map of name to RuleMethodAttributes. The methodName keys must match
     * the value returned by Method.getName(). Overloaded methods are not explicity
     * supported. eg:
     *
     * </pre>
     * public class PojoRule {
     *     public boolean conditionOne(..) {...}
     *     public boolean conditionTwo(..) {...}
     *     public void consequence(..) {...}
     * }
     *
     * Map nameMap = new HashMap();
     * nameMap.put("conditionOne", new RuleMethodAttributes(RuleMethodAttributes.CONDITION));
     * nameMap.put("conditionTwo", new RuleMethodAttributes(RuleMethodAttributes.CONDITION));
     * nameMap.put("consequence", new RuleMethodAttributes(RuleMethodAttributes.CONSEQUENCE));
     *
     * source.setNameMap(nameMap);
     * </pre>
     *
     * @param nameMap Map<String methodName, RuleMethodAttributes attributes>
     */
    public void setNameMap(Map nameMap) {
        this.nameMap = nameMap;
    }

}

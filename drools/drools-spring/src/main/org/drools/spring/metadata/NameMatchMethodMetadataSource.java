package org.drools.spring.metadata;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NameMatchMethodMetadataSource implements MethodMetadataSource{

    static final MethodMetadata CONDITION_METADATA = new MethodMetadata(MethodMetadata.CONDITION);
    static final MethodMetadata CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.CONSEQUENCE);

    private static class MetadataInfo {
        public MethodMetadata methodAttributes;
        public ArgumentMetadata[] argumentMetadata;
    }

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

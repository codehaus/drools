package org.drools.ide.debug;

import java.util.ArrayList;
import java.util.List;

import org.drools.ide.DroolsIDEPlugin;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.internal.ui.elements.adapters.DeferredVariableLogicalStructure;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * The Application Data View content provider.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class ApplicationDataViewContentProvider extends DroolsDebugViewContentProvider {

    private DroolsDebugEventHandlerView view;
    private IWorkbenchAdapter logicalStructureAdapter =
        new DeferredVariableLogicalStructure();
    
    public ApplicationDataViewContentProvider(DroolsDebugEventHandlerView view) {
        this.view = view;
    }
    
    public Object[] getChildren(Object obj) {
        try {
            IVariable[] variables = null;
            if (obj != null && obj instanceof IJavaObject
                    && "org.drools.reteoo.WorkingMemoryImpl".equals(
                        ((IJavaObject) obj).getReferenceTypeName())) {
                variables = getApplicationDataElements((IJavaObject) obj);
            } else if (view.isShowLogicalStructure()) {
                Object[] result = logicalStructureAdapter.getChildren(obj);
                if (result != null) {
                    return result;
                }
            } else if (obj instanceof IVariable) {
                variables = ((IVariable) obj).getValue().getVariables();
            }            if (variables == null) {
                return new Object[0];
            } else {
                cache(obj, variables);
                return variables;
            }
        } catch (DebugException e) {
            if (getExceptionHandler() != null) {
                getExceptionHandler().handleException(e);
            } else {
               DroolsIDEPlugin.log(e);
            }
            return new Object[0];
        }
    }
    
    private IVariable[] getApplicationDataElements(IJavaObject stackObj) throws DebugException {
        IValue objects = DebugUtil.getValueByExpression("return getApplicationDataMap().entrySet().toArray();", stackObj);
        if (objects instanceof IJavaArray) {
            IJavaArray array = (IJavaArray) objects;
            List result = new ArrayList();
            IJavaValue[] javaVals = array.getValues();
            for ( int i = 0; i < javaVals.length; i++ ) {
                IJavaValue mapEntry = javaVals[i];
                String key = null;
                IJavaValue value = null;
                
                IVariable[] vars = mapEntry.getVariables();
                for ( int j = 0; j < vars.length; j++ ) {
                    IVariable var = vars[i];
                    if ("key".equals(var.getName())) {
                        key = var.getValue().getValueString();
                    } else if ("value".equals(var.getName())) {
                        value = (IJavaValue) var.getValue();
                    }
                }
                result.add(new VariableWrapper(key, value));
            }
            return (IVariable[]) result.toArray(new IVariable[0]);
        }
        return null;
    }    
}

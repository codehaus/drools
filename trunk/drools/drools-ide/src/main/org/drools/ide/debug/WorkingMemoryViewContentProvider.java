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
import org.eclipse.jdt.internal.debug.core.logicalstructures.JDIPlaceholderVariable;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * The Working Memory view content provider.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class WorkingMemoryViewContentProvider extends DroolsDebugViewContentProvider {

    private DroolsDebugEventHandlerView view;
    private IWorkbenchAdapter logicalStructureAdapter =
        new DeferredVariableLogicalStructure();
    
    public WorkingMemoryViewContentProvider(DroolsDebugEventHandlerView view) {
        this.view = view;
    }
    
    public Object[] getChildren(Object obj) {
        try {
            IVariable[] variables = null;
            if (obj != null && obj instanceof IJavaObject
                    && "org.drools.reteoo.WorkingMemoryImpl".equals(
                        ((IJavaObject) obj).getReferenceTypeName())) {
                variables = getWorkingMemoryElements((IJavaObject) obj);
            } else if (view.isShowLogicalStructure()) {
                Object[] result = logicalStructureAdapter.getChildren(obj);
                if (result != null) {
                    return result;
                }
            } else if (obj instanceof IVariable) {
                variables = ((IVariable) obj).getValue().getVariables();
            }
            if (variables == null) {
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
    
    private IVariable[] getWorkingMemoryElements(IJavaObject stackObj) throws DebugException {
        IValue objects = DebugUtil.getValueByExpression("return getObjects().toArray();", stackObj);
        if (objects instanceof IJavaArray) {
            IJavaArray array = (IJavaArray) objects;
            List result = new ArrayList();
            
            IJavaValue[] vals = array.getValues();
            
            for ( int i = 0; i < vals.length; i++ ) {
                result.add(new JDIPlaceholderVariable("[" + i + "]", vals[i]));
            }
            
            
            return (IVariable[]) result.toArray(new IVariable[0]);
        }
        return null;
    }
    
}

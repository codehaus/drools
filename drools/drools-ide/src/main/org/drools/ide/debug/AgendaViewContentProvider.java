package org.drools.ide.debug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.ide.DroolsIDEPlugin;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.internal.ui.elements.adapters.DeferredVariableLogicalStructure;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * The Agenda View content provider.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class AgendaViewContentProvider extends DroolsDebugViewContentProvider {

    private DroolsDebugEventHandlerView view;
    private IWorkbenchAdapter logicalStructureAdapter =
        new DeferredVariableLogicalStructure();
    
    public AgendaViewContentProvider(DroolsDebugEventHandlerView view) {
        this.view = view;
    }
    
    public Object[] getChildren(Object obj) {
        try {
            Object[] variables = null;
            if (obj != null && obj instanceof IJavaObject
                    && "org.drools.reteoo.WorkingMemoryImpl".equals(
                        ((IJavaObject) obj).getReferenceTypeName())) {
                variables = getAgendaElements((IJavaObject) obj);
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
    
    private Object[] getAgendaElements(IJavaObject workingMemoryImpl) throws DebugException {
        IValue objects = DebugUtil.getValueByExpression("return getAgenda().getActivations().toArray();", workingMemoryImpl);
        if (objects instanceof IJavaArray) {
            IJavaArray array = (IJavaArray) objects;
            List result = new ArrayList();

            IJavaValue[] vals = array.getValues();
            for ( int j = 0; j < vals.length; j++ ) {
                result.add(new VariableWrapper("[" + j + "]", vals[j]));
            }
            
            return result.toArray(new IVariable[result.size()]);
        }
        return null;
    }

}

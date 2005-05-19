package org.drools.util.concurrent;

import org.drools.WorkingMemory;

/**
 * Helper class that allows multiple WorkingMemory methods to be invoked in a
 * single synchronized block via the use of a WorkingMemoryCallback. For example:
 *     <pre><code>
 *     private void updateWorkingMemory(Foo foo, Bar oldBar, Bar newBar) {
 *         new WorkingMemoryTemplate(workingMemory).execute(new WorkingMemoryCallback() {
 *             public Object doInWorkingMemory(final WorkingMemory workingMemory) {
 *                 workingMemory.assertObject(foo);
 *
 *                 FactHandle handle = workingMemory.getFactHandle(oldBar)
 *                 workingMemory.modifyObject(handle, newBar);
 *
 *                 workingMemory.fireAllRules();
 *             }
 *         }
 *     }
 *     </code></pre>
 *
 *  The abstract method <code>getWorkingMemory</code> allows concrete subclass to define
 *  their own strategy for obtaining the WorkingMemory instance.
 */
public abstract class AbstractWorkingMemorySynchronizedTemplate {

    /**
     * Callback interface for invoking WorkingMemory methods within a synchronized block.
     */
    public interface Callback {
        /**
         * Gets called by <code>WorkingMemorySynchronizedTemplate.execute</code> with a
         * WorkingMemory instance protected by a synchronized block.
         * @param workingMemory
         * @return Any object or null.
         */
        Object doInWorkingMemory(final WorkingMemory workingMemory);
    }

    /**
     * Concrete subclasses must implement this method to provide the WorkingMemory instance.
     * @return WorkingMemory
     */
    protected abstract WorkingMemory getWorkingMemory();

    /**
     * Invokes the callback within a synchronized block on the workingMemory instance
     * passed to the constructor.
     * @param callback
     * @return Any object or null.
     */
    public Object execute(Callback callback) {
        synchronized(getWorkingMemory()) {
            return callback.doInWorkingMemory(getWorkingMemory());
        }
    }
}

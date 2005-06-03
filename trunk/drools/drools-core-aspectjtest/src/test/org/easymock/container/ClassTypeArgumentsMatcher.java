package org.easymock.container;

import org.easymock.container.EasymockContainer;

public class ClassTypeArgumentsMatcher extends AbstractArgumentsMatcher {

    private final Class<?>[] expectedClasses;

    public ClassTypeArgumentsMatcher(EasymockContainer container, Class<?>... expectedClasses) {
        super(container);
        this.expectedClasses = expectedClasses;
    }

    @Override
    protected boolean doRecordMatches(Object[] expected, Object[] actual) {
        return true;
    }

    @Override
    protected boolean doReplayMatches(Object[] expected, Object[] actual) {
        for (int i = 0; i < actual.length; i++) {
            if (!expectedClasses[i].isAssignableFrom(actual[i].getClass())) {
                return false;
            }
        }
        return true;
    }
}
package org.easymock.container;

import java.util.ArrayList;

import org.easymock.container.EasymockContainer;

public class CapturingArgumentsMatcher extends AbstractArgumentsMatcher {

    private ArrayList<Object[]> capturedActuals = new ArrayList<Object[]>();

    public CapturingArgumentsMatcher(EasymockContainer container) {
        super(container);
    }

    public Object[] getActual(int callCount) {
        return capturedActuals.get(callCount);
    }

    @Override
    protected boolean doReplayMatches(Object[] expected, Object[] actual) {
        capturedActuals.add(actual);
        return true;
    }
}
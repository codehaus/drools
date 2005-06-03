package org.easymock.container;

import org.easymock.ArgumentsMatcher;
import org.easymock.container.EasymockContainer;

public abstract class AbstractArgumentsMatcher implements ArgumentsMatcher {

    protected boolean doRecordMatches(Object[] expected, Object[] actual) { return true; }
    protected boolean doReplayMatches(Object[] expected, Object[] actual) { return true; }
    protected boolean doVerifyMatches(Object[] expected, Object[] actual) { return true; }

    private final EasymockContainer container;

    public AbstractArgumentsMatcher(EasymockContainer container) {
        this.container = container;
    }

    // The names of the variables are swapped to correctly reflect the
    // values passed in. (See http://groups.yahoo.com/group/easymock/message/350)
    public boolean matches(Object[] actual, Object[] expected) {
        switch (container.getState()) {
        case RECORD:
            return doRecordMatches(actual, expected);
        case REPLAY:
            return doReplayMatches(expected, actual);
        case VERIFY:
            return doVerifyMatches(expected, actual); // TODO args in right order?
        default:
            throw new IllegalStateException("Unsupported state: " + container.getState());
        }
    }

    public String toString(Object[] arguments) {
        if (arguments == null)
            arguments = new Object[0];

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < arguments.length; i++) {
            if (i > 0)
                result.append(", ");
            result.append(argumentToString(arguments[i]));
        }
        return result.toString();
    }

    protected String argumentToString(Object argument) {
        if (argument instanceof String) {
            return "\"" + argument + "\"";
        }
        return "" + argument;
    }
}

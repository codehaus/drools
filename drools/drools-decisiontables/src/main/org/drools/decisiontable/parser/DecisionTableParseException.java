package org.drools.decisiontable.parser;

public class DecisionTableParseException extends RuntimeException
{
    public DecisionTableParseException(String message) {
        super(message);        
    }
    
    public DecisionTableParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

package org.drools.semantics.java;

import org.apache.commons.jci.problems.CompilationProblem;

/**
 * This exception gets thrown when there is an actual error compiling a java semantic components.
 * Errors are passed back from JCI.
 * 
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 */
public class JavaSemanticCompileError extends RuntimeException {
    

    private static final long serialVersionUID = 34846969604394084L;
    
    private CompilationProblem[] errors;
    private String summaryMessage;
    
    public JavaSemanticCompileError(CompilationProblem[] errors) {
        this.errors = errors;
        StringBuffer buf = new StringBuffer();
        buf.append("A problem occured compiling the embedded code: \n");
        
        constructMessage( errors,
                          buf );
        
        this.summaryMessage = buf.toString();
        
    }
    
    public CompilationProblem[] getErrors() {
        return this.errors;
    }

    private void constructMessage(CompilationProblem[] errors,
                                  StringBuffer buf) {
        for ( int i = 0; i < errors.length; i++ ) {
            String errorSummary = errors[i].getFileName() + "(" + errors[i].getStartLine() + ")" + "\n" + errors[i].getMessage();
            buf.append(errorSummary + " \n");
        }
    }

    /**
     * This will return information about the errors that the semantic compiler returned. 
     * More detail can be obtained by inspecting getErrors() array of CompilationProblems.
     */
    public String getMessage() {
        return this.summaryMessage;
    }
    
    
    

}

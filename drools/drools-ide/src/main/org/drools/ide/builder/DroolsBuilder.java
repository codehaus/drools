package org.drools.ide.builder;

import java.io.StringReader;
import java.util.Map;

import org.drools.ide.DroolsIDEPlugin;
import org.drools.rule.NoConsequenceException;
import org.drools.semantics.java.CompilationException;
import org.drools.smf.FactoryException;
import org.drools.spi.RuleBaseContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.xml.sax.SAXParseException;

import antlr.NoViableAltException;

/**
 * Automatically syntax checks .drl files and adds possible
 * errors or warnings to the problem list.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class DroolsBuilder extends IncrementalProjectBuilder {

    public static final String BUILDER_ID = "org.drools.ide.droolsbuilder";

    protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
            throws CoreException {
        IProject currentProject = getProject();
        if (currentProject == null || !currentProject.isAccessible()) {
            return new IProject[0];
        }
        try {
            if (monitor != null && monitor.isCanceled())
                throw new OperationCanceledException();

            if (kind == IncrementalProjectBuilder.FULL_BUILD) {
                fullBuild(monitor);
            } else {
                IResourceDelta delta = getDelta(getProject());
                if (delta == null) {
                    fullBuild(monitor);
                } else {
                    incrementalBuild(delta, monitor);
                }
            }
        } catch (CoreException e) {
            IMarker marker = currentProject.createMarker(IDroolsModelMarker.DROOLS_MODEL_PROBLEM_MARKER);
            marker.setAttribute(IMarker.MESSAGE, "Error when trying to build Drools project: " + e.getLocalizedMessage());
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
        }
        return null;
    }
    
    protected void fullBuild(final IProgressMonitor monitor)
            throws CoreException {
        getProject().accept(new DroolsBuildVisitor());
    }
    
    protected void incrementalBuild(IResourceDelta delta,
            IProgressMonitor monitor) throws CoreException {
        delta.accept(new DroolsBuildDeltaVisitor());
    }

    private class DroolsBuildVisitor implements IResourceVisitor {
        public boolean visit(IResource res) {
            return parseResource(res);
        }
    }

    private class DroolsBuildDeltaVisitor implements IResourceDeltaVisitor {
        public boolean visit(IResourceDelta delta) throws CoreException {
            return parseResource(delta.getResource());
        }
    }
    
    public static boolean parseResource(IResource res) {
        if (res instanceof IFile && "drl".equals(res.getFileExtension())) {
            removeProblemsFor(res);
            try {
                IJavaProject project = JavaCore.create(res.getProject());
                if (project.getOutputLocation().isPrefixOf(res.getFullPath())) {
                    return false;
                }
            } catch (JavaModelException e) {
                // do nothing
            }

            RuleBaseContext factoryContext = new RuleBaseContext();
            RuleSetReaderWithErrors reader = new RuleSetReaderWithErrors(factoryContext);
            try {
                ClassLoader oldLoader = Thread.currentThread()
                        .getContextClassLoader();
                ClassLoader newLoader = DroolsBuilder.class.getClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(newLoader);
                    reader.read(new StringReader(new String(Util.getResourceContentsAsCharArray((IFile) res))));
                    for (SAXParseException e: reader.getErrors()) {
                        createMarker(res, e.getMessage(), e.getLineNumber(), e.getColumnNumber());
                    }
                    for (SAXParseException e: reader.getWarnings()) {
                        createWarning(res, e.getMessage(), e.getLineNumber(), e.getColumnNumber());
                    }
                } catch (Exception t) {
                    throw t;
                } finally {
                    Thread.currentThread().setContextClassLoader(oldLoader);
                }
            } catch (SAXParseException e) {
                Exception ex = e.getException();
                if (ex instanceof FactoryException) {
                    Throwable t = ex.getCause();
                    if (t instanceof CompilationException) {
                        CompilationException exc = (CompilationException) t;
                        createMarker(res, exc.getErrorMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                    } else if (t instanceof NoViableAltException) {
                        NoViableAltException exc = (NoViableAltException) t;
                        createMarker(res, exc.getMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                    } else if (t == null) {
                        createMarker(res, ex.getMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                    } else {
                        createMarker(res, t.getMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                    }
                } else if (ex instanceof NoConsequenceException) {
                    createMarker(res, "No consequence", reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                } else if (ex == null) {
                	if (reader.getLocator() != null) {
                		createMarker(res, e.getMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                	} else {
                		createMarker(res, e.getMessage(), -1, -1);
                	}
                } else {
                	if (reader.getLocator() != null) {
                		createMarker(res, e.getMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
                	} else {
                		createMarker(res, e.getMessage(), -1, -1);
                	}
                }
            } catch (Exception e) {
                createMarker(res, e.getMessage(), reader.getLocator().getLineNumber(), reader.getLocator().getColumnNumber());
            }
            return false;
        }
        return true;
    }
    
    private static void createMarker(IResource res, String message, int lineNumber, int charStart) {
        try {
            IMarker marker = res
                    .createMarker(IDroolsModelMarker.DROOLS_MODEL_PROBLEM_MARKER);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY,
                    IMarker.SEVERITY_ERROR);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
            marker.setAttribute(IMarker.CHAR_START, charStart);
        } catch (CoreException e) {
            DroolsIDEPlugin.log(e);
        }
    }
    
    private static void createWarning(IResource res, String message, int lineNumber, int charStart) {
        try {
            IMarker marker = res
                    .createMarker(IDroolsModelMarker.DROOLS_MODEL_PROBLEM_MARKER);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY,
                    IMarker.SEVERITY_WARNING);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
            marker.setAttribute(IMarker.CHAR_START, charStart);
        } catch (CoreException e) {
            DroolsIDEPlugin.log(e);
        }
    }
    
    public static void removeProblemsFor(IResource resource) {
        try {
            if (resource != null && resource.exists()) {
                resource.deleteMarkers(
                        IDroolsModelMarker.DROOLS_MODEL_PROBLEM_MARKER, false,
                        IResource.DEPTH_INFINITE);
            }
        } catch (CoreException e) {
            DroolsIDEPlugin.log(e);
        }
    }
    
}

package org.drools.ide.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.drools.ide.DroolsIDEPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ProjectClassLoader {

    public static URLClassLoader getProjectClassLoader(IJavaProject project) {
        List<URL> pathElements = getProjectClassPathURLs(project);
        URL urlPaths[] = pathElements.toArray(new URL[pathElements.size()]);
        return new URLClassLoader(urlPaths, Thread.currentThread().getContextClassLoader());
    }

    private static URL getRawLocationURL(IPath simplePath)
            throws MalformedURLException {
        File file = getRawLocationFile(simplePath);
        return file.toURL();
    }

    private static File getRawLocationFile(IPath simplePath) {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(simplePath);
        File file = null;
        if (resource != null) {
            file = ResourcesPlugin.getWorkspace().getRoot().findMember(
                    simplePath).getRawLocation().toFile();
        } else {
            file = simplePath.toFile();
        }
        return file;
    }

    public static List<URL> getProjectClassPathURLs(IJavaProject project) {
        List<URL> pathElements = new ArrayList<URL>();
        try {
            IClasspathEntry[] paths = project.getResolvedClasspath(true);
            if (paths != null) {
                for (IClasspathEntry path: paths) {
                    if (path.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
                        URL url = getRawLocationURL(path.getPath());
                        pathElements.add(url);
                    }
                }
            }
            IPath location = getProjectLocation(project.getProject());
            IPath outputPath = location.append(project.getOutputLocation()
                    .removeFirstSegments(1));
            pathElements.add(outputPath.toFile().toURL());
            
            // also add classpath of required projects
            for (String projectName: project.getRequiredProjectNames()) {
                IProject reqProject = project.getProject().getWorkspace()
                    .getRoot().getProject(projectName);
                if (reqProject != null) {
                    IJavaProject reqJavaProject = JavaCore.create(reqProject);
                    pathElements.addAll(getProjectClassPathURLs(reqJavaProject));
                }
            }
        } catch (JavaModelException e) {
            DroolsIDEPlugin.log(e);
        } catch (MalformedURLException e) {
            DroolsIDEPlugin.log(e);
        }
        return pathElements;
    }
    
    public static IPath getProjectLocation(IProject project) {
        if (project.getRawLocation() == null) {
            return project.getLocation();
        } else {
            return project.getRawLocation();
        }
    }
}

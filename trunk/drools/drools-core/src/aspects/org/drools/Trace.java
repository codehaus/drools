package org.drools;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;

import org.drools.WorkingMemory;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collections;

/**
 * This is a simple Drools profiler. The MethodEntry point is before the first newWorkingMemory
 * and the exit point is after the following fireAllRules. Tracing will not resume again
 * until a newWorkingMemory is created. You cannot call the next newWorkingMemory until after
 * the first fireAllRules is fired, as it confuses the output. Each MethodEntry/exit creates a
 * method-trace-<number>.txt and method-count-<number>.txt, where <number> is a counter
 * increased each time fireAllRules is called.
 *
 * Trace Levels
 * 0 - off
 * 1 - count-methods.txt
 * 2 - count-methods.txt and trace-methods (entry/exit method method names)
 * 3 - count-methods.txt and trace-methods (entry/exit method method names and count)
 * 4 - count-methods.txt and trace-methods (entry/exit method method names, count and timer)
 * @author mproctor
 */

public aspect Trace {

    private static PrintWriter  traceOut;

    private static boolean trace = false;

    private static int runCount = 1;

    private static int callDepth = 0;

    private static Map methodCount;

    private static int traceLevel = 4;


    protected static void traceEntry(String str)
    {
        if (traceLevel == 0) return;
        if (trace == false) return;
        try
        {
            if (traceOut == null)
            {
                if (System.getProperty("trace-level") != null)
                {
                  traceLevel = Integer.parseInt(System.getProperty("trace-level"));
                }
                traceOut = new PrintWriter(new BufferedWriter(new FileWriter("target/method-trace-" + runCount + ".txt")));
                methodCount = new HashMap();
            }
            callDepth++;

            if (!methodCount.containsKey(str))
            {
              methodCount.put(str, new Integer(0));
            }
            Integer count = (Integer) methodCount.get(str);
            count = new Integer(count.intValue() + 1);
            methodCount.put(str, count);

            printEntering(str);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    protected static void traceExit(String str)
    {
        if (traceLevel == 0) return;
        if (trace == false) return;
        if (traceOut == null) return;
        try
        {
            printExiting(str);
            callDepth--;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void printEntering(String str) throws Exception
    {
        if (traceLevel < 2) return;
        printIndent();
        if (traceLevel == 2)
        {
          traceOut.println("--> " + str);
        }
        else if (traceLevel == 3)
        {
            traceOut.println("--> " + str + " : " + methodCount.get(str) + "");
        }
        else if (traceLevel == 4)
        {
            traceOut.println("--> " + str + " : " + methodCount.get(str) + " (" + System.currentTimeMillis() + ")");
        }
    }

    private static void printExiting(String str) throws Exception
    {
        if (traceLevel < 2) return;
        printIndent();
        if (traceLevel == 2)
        {
          traceOut.println("--> " + str);
        }
        else if (traceLevel == 3)
        {
            traceOut.println("--> " + str + " : " + methodCount.get(str) + "");
        }
        else if (traceLevel == 4)
        {
            traceOut.println("--> " + str + " : " + methodCount.get(str) + " (" + System.currentTimeMillis() + ")");
        }
    }

    private static void printIndent() throws Exception
    {
        for (int i = 0; i < callDepth; i++)
        {
            traceOut.print("  ");
        }
    }

    private static void printCount()
    {
      if (traceLevel == 0) return;
      if (traceOut == null) return;
        try
        {
            List methods = new ArrayList();
            Iterator it = methodCount.keySet().iterator();
            String method;
            Integer count;
            MethodEntry methodEntry;
            while (it.hasNext())
            {
              method = (String) it.next();
              count = (Integer) methodCount.get(method);
              methods.add(new MethodEntry(method, count.intValue()));
            }
            Collections.sort(methods);

            PrintWriter countOut = new PrintWriter(new BufferedWriter(new FileWriter("target/method-count" + runCount + ".txt")));
            it = methods.iterator();
            while (it.hasNext())
            {
              methodEntry = (MethodEntry) it.next();
              countOut.println(methodEntry.getMethod() + " : " + methodEntry.getCount());
            }
            countOut.close();
            countOut = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    pointcut droolsClass(): within(org.drools..*) && within(! org.drools.Trace) && within(! org.drools.MethodEntry);
    pointcut droolsConstructor(): droolsClass() && execution(new(..));
    pointcut droolsMethod(): droolsClass() && call(* org.drools..*(..));

    pointcut workingMemoryConst(): within(WorkingMemory+) && execution(new(..));

    pointcut fireAllRules(WorkingMemory workingMemory):
        execution(void WorkingMemory+.fireAllRules())
        && target(workingMemory);

    before():
        workingMemoryConst() {
            trace = true;
        }

    before (WorkingMemory workingMemory) :
        fireAllRules(workingMemory) {

        }

    after (WorkingMemory workingMemory) :
        fireAllRules(workingMemory) {
          printCount();
          traceOut.close();
          traceOut = null;
          runCount++;
          trace = false;
        }


    before() : droolsConstructor() {
        traceEntry("" + thisJoinPointStaticPart.getSignature());
    }
    after() : droolsConstructor() {
        traceExit("" + thisJoinPointStaticPart.getSignature());
    }

    before() : droolsMethod() {
        traceEntry("" + thisJoinPointStaticPart.getSignature());
    }
    after() : droolsMethod() {
        traceExit("" + thisJoinPointStaticPart.getSignature());
    }

}

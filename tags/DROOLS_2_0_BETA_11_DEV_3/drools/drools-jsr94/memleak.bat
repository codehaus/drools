@echo off
setlocal

set CLASSPATH=target/classes
set CLASSPATH=%CLASSPATH%;target/test-classes
set CLASSPATH=%CLASSPATH%;target/examples-classes
set CLASSPATH=%CLASSPATH%;lib/antlrall-2.7.1.jar
set CLASSPATH=%CLASSPATH%;lib/bsh-1.2b7.jar
set CLASSPATH=%CLASSPATH%;lib/commons-beanutils-1.6.jar
set CLASSPATH=%CLASSPATH%;lib/commons-collections-2.1.jar
set CLASSPATH=%CLASSPATH%;lib/commons-jelly-1.0-beta-4-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;lib/commons-jexl-1.0-beta-2.jar
set CLASSPATH=%CLASSPATH%;lib/commons-logging-1.0.3.jar
set CLASSPATH=%CLASSPATH%;lib/dom4j-1.4.jar
set CLASSPATH=%CLASSPATH%;lib/junit-3.8.1.jar
set CLASSPATH=%CLASSPATH%;lib/xerces-2.3.0.jar
set CLASSPATH=%CLASSPATH%;lib/xml-apis-2.0.2.jar
set CLASSPATH=%CLASSPATH%;common-lib/jsr94-1.0-pr.jar

rem java -cp %CLASSPATH% %JAVA_OPTS% org.drools.jsr94.rules.StatelessRuleSessionTestCase

rem JProfiler
rem
rem set PATH=D:\jprofiler\bin\windows;%PATH%
rem set JAVA_OPTS=-Xint -Xrunjprofiler:port=8849 "-Xbootclasspath/a:D:\jprofiler\bin\agent.jar" %JAVA_OPTS%

java -cp %CLASSPATH% %JAVA_OPTS% sisters.Sisters %1

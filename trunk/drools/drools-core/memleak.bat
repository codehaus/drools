@echo off
setlocal

set CLASSPATH=target/classes
set CLASSPATH=%CLASSPATH%;target/test-classes
set CLASSPATH=%CLASSPATH%;target/example-classes
set CLASSPATH=%CLASSPATH%;lib/antlrall-2.7.1.jar
set CLASSPATH=%CLASSPATH%;lib/bsh-1.2b7.jar
set CLASSPATH=%CLASSPATH%;lib/commons-beanutils-1.6.jar
set CLASSPATH=%CLASSPATH%;lib/commons-collections-2.1.jar
set CLASSPATH=%CLASSPATH%;lib/commons-jelly-1.0-beta-4-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;lib/commons-jexl-1.0-beta-2.jar
set CLASSPATH=%CLASSPATH%;lib/commons-logging-1.0.3.jar
set CLASSPATH=%CLASSPATH%;lib/dom4j-1.4.jar
set CLASSPATH=%CLASSPATH%;lib/junit-3.8.1.jar
set CLASSPATH=%CLASSPATH%;common-lib/jsr94-1.0-pr.jar

rem java -cp %CLASSPATH% %JAVA_OPTS% org.drools.jsr94.rules.StatelessRuleSessionTestCase

java -cp %CLASSPATH% %JAVA_OPTS% sisters.Sisters %1

= Dependencies =

If you are using org.drools.RuleBaseBuilder to build your RuleBases or to run
drools-examples - which needs io.RuleBaseBuilder - you must include the
following JARs:

drools-core
drools-smf
drools-io
drools-base

Then depending on which semantic module you are using you must include
drools-<semantic> and also its dependencies:

drools-java
 -antlr
 -janino

drools-python
 -jython

drools-groovy
 -asm
 -asm-util
 -groovy

drools-decisiontables
 -antlr
 -janino
 -drools-java

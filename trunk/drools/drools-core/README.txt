 $Id: README.txt,v 1.1 2003-07-04 14:44:58 tdiesler Exp $

 Drools Readme
 =============

    1. How to run the drools examples

    2. How to run the drools webapp in catalina

    3. How to run the drools webapp in JBoss

    4. How to build drools

    5. How thankful we are


1. How to run the drools examples
---------------------------------

    With the binary distribution you can run the examples like this:

        bin/run.sh some_example_class

    For the list of available examples see bin/run.sh

    With the source distribution you can run the examples through ant.

2. How to run the drools webapp in catalina
-------------------------------------------

    Just copy the drools.war to $CATALINA_HOME/webapps

    It has been tested with jakarta-tomcat-4.1.24

3. How to run the drools webapp in JBOSS
----------------------------------------

    Copy the files in $DROOLS_HOME/etc to $JBOSS_HOME/server/default/deploy and watch the JBoss console, it should tell
    you the JNDI name under which drools is available.

    Copy the drools.war to $JBOSS_HOME/server/default/deploy

    It has been tested with jboss-3.2.1

4. How to build drools
----------------------

    The very brave use the latest maven snapshot.

    The sane use ant.

5. How thankful we are
----------------------

    We continuously receive valuable feedback through the mailing lists and other channels - thanks to all of you.
    Big bunch of flowers.

-the drools team


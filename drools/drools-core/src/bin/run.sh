#! /bin/sh
#
#  Copyright 2002 (C) The Werken Company. All Rights Reserved.
#
# This is a very simple drools example run script
#
# It should be run from DROOLS_HOME
#
# Currently you can run these examples:
#
#   bin/run.sh sisters.Sisters
#   bin/run.sh escalation.Escalation
#   bin/run.sh fishmonger.FishMonger
#   bin/run.sh dimensions.Dimensions
#
#   bin/run.sh sisters.JSR94SistersStateful
#   bin/run.sh sisters.JSR94SistersStateless
#
# $Id: run.sh,v 1.1 2003-07-04 14:45:59 tdiesler Exp $

if [ -z "$DROOLS_HOME" ] ; then
    DROOLS_HOME=.
fi

DROOLS_LIB="$DROOLS_HOME/lib"
DROOLS_EXAMPLES_LIB="$DROOLS_HOME/examples"

# OS specific support
cygwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
esac

if [ -n "$CLASSPATH" ] ; then
  LOCALCLASSPATH="$CLASSPATH"
fi

# add in the dependency .jar files
for i in "${DROOLS_LIB}"/*.jar
do
  # if the directory is empty, then it will return the input string
  # this is stupid, so case for it
  if [ -f "$i" ] ; then
    if [ -z "$LOCALCLASSPATH" ] ; then
      LOCALCLASSPATH="$i"
    else
      LOCALCLASSPATH="$LOCALCLASSPATH":"$i"
    fi
  fi
done

# add in the examples .jar files
for i in "${DROOLS_EXAMPLES_LIB}"/*.jar
do
  # if the directory is empty, then it will return the input string
  # this is stupid, so case for it
  if [ -f "$i" ] ; then
    if [ -z "$LOCALCLASSPATH" ] ; then
      LOCALCLASSPATH="$i"
    else
      LOCALCLASSPATH="$LOCALCLASSPATH":"$i"
    fi
  fi
done

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  LOCALCLASSPATH=`cygpath --path --windows "$LOCALCLASSPATH"`
fi

echo "LOCALCLASSPATH=$LOCALCLASSPATH"
echo ""

echo "--------------------------------------------------"
echo " R U N N I N G  $@"
echo "--------------------------------------------------"
echo ""

java -cp $LOCALCLASSPATH $@


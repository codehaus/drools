#!/bin/sh

function target_prepare()
{
  mkdir -p build
  mkdir -p build/classes
  mkdir -p build/sources

  ( cd ./src/main/ ; find ./ -depth \( -name '*.java' \) -print | cpio -pudm --quiet ../../build/sources )
}

function target_precompile()
{
  if [ -f build.sh ] ; then
    /bin/sh build.sh
  fi
}

function target_compile()
{
  $JAVAC \
    -classpath $(dyn_classpath) \
    -sourcepath ./build/sources/ \
    -d ./build/classes/ \
    -deprecation \
    -g \
    $(find ./build/sources/ -name '*.java')
}

function target_copy_resources()
{
  if [ -d ./src/conf/ ] ; then
    ( cd ./src/conf/ ; find ./ -depth \( -name '*.java' \) -print | cpio -pudm --quiet ../../build/classes )
  fi
}

function target_jar()
{
  ( cd ./build/ ; $JAR -cf drools-$MODULE-$VERSION.jar -C ./classes/ . )
}

function target_copy_jar()
{
  cp ./build/drools-$MODULE-$VERSION.jar ../build/lib
}

function target_copy_lib()
{
  if [ ! -d ./lib/ ] ; then
    return
  fi
  ( cd ./lib/ ; find ./ -depth \( -name '*.jar' \) -print | cpio -pudm --quiet ../../build/lib )
}

function target_javadoc()
{
  local sourcepath=""


  for module in $MODULES ; do 
    if [ -z $sourcepath ] ; then
      ( cd drools-$module/src/main/ ; find ./ -depth \( -name '*.html' \) -print | cpio -pudm --quiet ../../build/sources )
      sourcepath=./drools-$module/build/sources/
    else
      sourcepath=$sourcepath:./drools-$module/build/sources/
    fi
  done

  $JAVADOC \
      -classpath $(dyn_javadoc_classpath) \
      -sourcepath $sourcepath \
      -windowtitle "Drools $VERSION Public API" \
      -use \
      -version \
      -author \
      -d ./build/docs/api/ -subpackages org.drools:bsh.commands\
      -group "Core Engine" org.drools:org.drools.rule:org.drools.conflict \
      -group "Semanic Providers Interface" org.drools.spi \
      -group "Semantic Module Framework" org.drools.smf \
      -group "Rule I/O" org.drools.io \
      -group "Base Semantic Module" org.drools.semantics.base \
      -group "Java Semantic Module" org.drools.semantics.java:org.drools.semantics.java.parser:bsh.commands \
      -group "Python Semantic Module" org.drools.semantics.python \
      -group "Groovy Semantic Module" org.drools.semantics.groovy \
      -group "JSR-94 Binding" org.drools.jsr94:org.drools.jsr94.rules:org.drools.jsr94.rules.admin:org.drools.jsr94.jca.spi \
      -exclude org.drools.reteoo \
      org.drools \
      bsh.commands 

}

function dyn_classpath()
{
  local common=$(dyn_common_classpath)
  local lib=$(dyn_lib_classpath)

  local cp=""

  if [ ! -z $common ] ; then
    cp=$common
  fi

  if [ ! -z $lib ] ; then
    if [ -z $cp ] ; then
      cp=$lib
    else
      cp=$cp:$lib
    fi
  fi 

  echo $cp 
}

function dyn_javadoc_classpath()
{
  local jars=./build/lib/*.jar

  local cp=""

  for jar in $jars ; do
    if [ -z $cp ] ; then
      cp=$jar
    else
      cp="$cp:$jar"
    fi
  done

  echo $cp
}

function dyn_common_classpath()
{
  local jars=../build/lib/*.jar

  local cp=""

  for jar in $jars ; do
    if [ -z $cp ] ; then
      cp=$jar
    else
      cp="$cp:$jar"
    fi
  done

  echo $cp
}

function dyn_lib_classpath()
{
  if [ ! -d ./lib/ ] ; then
    echo ""
    return
  fi;

  local jars="lib/*.jar"

  local cp=""

  for jar in $jars ; do
    if [ -z $cp ] ; then
      cp=$jar
    else
      cp="$cp:$jar"
    fi
  done

  echo $cp
}

function build()
{
  ( build_i $* )
}

function clean()
{
  (clean_i $* )
}

function build_i()
{
  local module=$*

  mkdir -p ./build/
  mkdir -p ./build/lib/
  mkdir -p ./build/docs/api/

  cd "drools-$module"

  echo "building $module"

  old_MODULE=$MODULE
  MODULE=$module

  target_prepare
  target_precompile
  target_compile
  target_copy_resources
  target_jar
  target_copy_jar
  target_copy_lib
  
  MODULE=$old_MODULE
}

function clean_i()
{
  local module=$*

  cd "drools-$module"

  echo "cleaning $module"
  
  rm -Rf build
}

function do_build
{
  for module in $MODULES ; do
    build $module;
  done
}

function do_javadoc
{
  target_javadoc
}

function do_clean
{
  for module in $MODULES ; do
    clean $module;
  done
  rm -Rf ./build
}

function do_
{
  do_build
}

VERSION=2.0-beta-12
#MODULES="core"
MODULES="core smf io base java python jsr94 groovy"
#MODULES="core smf io base python jsr94 groovy"
#MODULES="core smf io jsr94 base java python groovy"
JAVA=java
JAVAC=javac
JAR=jar
JAVADOC=javadoc

export JAVA JAVAC JAR JAVADOC

if [ -z "$*" ] ; then
  do_
else
  for target in $* ; do
    do_$target
  done
fi


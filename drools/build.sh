#!/bin/bash

VERSION=2.0-beta-12
JAVADOC=javadoc
JAVAC=javac
JAVA=java
JAR=jar

DEPLOY_SITE=codehaus.org
DEPLOY_DIR=/www/drools.codehaus.org/preview

#IFS=""
MODULES="core io base java python groovy smf jsr94"
COMPILATION_MODULES="core smf io base java python groovy jsr94"

export JAVA JAVAC JAR
##
##
##

build()
{
  local target;

  for target in $* ; do
    done_var=done_$target
    done=${!done_var}
    if [ "${done}" != "done" ] ; then
      export ${done_var}="done"
      target_${target}
    fi
  done 
}

target_all()
{
  build compile javadoc site
}

target_site()
{
  build javadoc pdf

  echo "building site"

  for path in $(cd $BASE/site; find . -name '*.html') ; do
    generate_root_page $path 
  done

  for module in $MODULES ; do
    module_site $module
  done

  copy_tree $BASE/build/docs/api $BASE/build/site/api 
  cp $BASE/build/docs/*.pdf $BASE/build/site/
}

module_site()
{
  local module=$1

  if [ ! -d drools-$module/site ] ; then
    return
  fi
  
  for path in $(cd drools-$module/site; find . -name '*.html') ; do
    generate_module_page $path $module
  done
}

generate_root_page()
{
  local path=$1
  local page=$(basename $path)
  local out=$BASE/build/site/$page

  mkdir -p $(dirname $out)

  local TOP="."

  echo "" > $out

  cat $BASE/lib/site/first.html | sed -e s/VERSION/$VERSION/g -e s/TOP/$TOP/g -e s/NOW_DAY/$NOW_DAY/g -e s/NOW_MONTH/$NOW_MONTH/g >> $out
  generate_root_nav $page $out
  cat $BASE/lib/site/middle.html >> $out
  cat ./site/$page | sed -e s/VERSION/$VERSION/g -e s/TOP/$TOP/g -e s/NOW_DAY/$NOW_DAY/g -e s/NOW_MONTH/$NOW_MONTH/g >> $out
  cat $BASE/lib/site/last.html >> $out
}

generate_module_page()
{
  local path=$1
  local module=$2
  local page=$(basename $path)
  local out=$BASE/build/site/$module/$page

  mkdir -p $(dirname $out)

  local TOP=".."
 
  echo "" > $out

  cat $BASE/lib/site/first.html | sed -e s/VERSION/$VERSION/g -e s/TOP/$TOP/g >> $out
  generate_module_nav $page $module $out

  cat $BASE/lib/site/middle.html >> $out

  cat ./drools-$module/site/$page | sed -e s/VERSION/$VERSION/g -e s/TOP/$TOP/g >> $out
  cat $BASE/lib/site/last.html >> $out
}

generate_root_nav()
{
  local page=$1
  local out=$2

  generate_local_nav $page $out ./site/nav.nav

  local module

  for module in $MODULES ; do 
    if [ -f drools-$module/site/nav.nav ] ; then
      generate_nonlocal_nav "$out" "drools-$module/site/nav.nav" "$module" ""
    fi
  done
  
}

generate_module_nav()
{
  local page=$1
  local thismodule=$2
  local out=$3

  local module

  generate_nonlocal_nav $out $BASE/site/nav.nav ".." ""

  for module in $MODULES ; do 
    if [ -f drools-$module/site/nav.nav ] ; then
      if [ "$module" == "$thismodule" ] ; then 
        generate_local_nav "$page" "$out" "drools-$module/site/nav.nav"
      else
        generate_nonlocal_nav "$out" "drools-$module/site/nav.nav" "$module" "../"
      fi
    fi
  done
}

generate_local_nav()
{
  local page=$1
  local out=$2
  local nav=$3

  local first="first"

  echo '<div class="navSection">' >> $out

  while read line ; do
    echo "$line" | grep '^=' 2>&1 > /dev/null
    if [ $? -eq 0 ] ; then
      if [ $first == "first" ] ; then
        first=notfirst
      else
        echo "</div>" >> $out
        echo '<div class="navSection">' >> $out
      fi
      local header=$(echo $line | cut -f 2 -d =)
      echo "  <div class=\"navSectionHead\">$header</div>" >> $out
    else
      local url=$(echo $line | cut -f 1 -d \|)
      local desc=$(echo $line | cut -f 2 -d \|)
      echo "$url" | grep '^+' 2>&1 > /dev/null
      if [ $? -eq 0 ] ; then
          local url=$(echo $url | cut -f 2 -d +)
          echo "    <div class=\"navLink\"><small><i><a href=\"$url\">$desc</a></i></small></div>" >> $out
      else
        if [ "$url" == "$page" ] ; then
          echo "    <div class=\"navLink\"><small><a href=\"$url\" style=\"font-weight: bold\">[&nbsp;$desc&nbsp;]</a></small></div>" >> $out
        else
          echo "    <div class=\"navLink\"><small><a href=\"$url\">$desc</a></small></div>" >> $out
        fi
      fi
    fi
  done < "$nav"

  echo '</div>' >> $out
}

generate_nonlocal_nav()
{
  local out=$1
  local nav=$2
  local navmodule=$3
  local prefix=$4

  local first="first"

  echo '<div class="navSection">' >> $out

  while read line ; do
    echo "$line" | grep '^=' 2>&1 > /dev/null
    if [ $? -eq 0 ] ; then
      if [ $first == "first" ] ; then
        first=notfirst
      else
        echo "</div>" >> $out
        echo '<div class="navSection">' >> $out
      fi
      local header=$(echo $line | cut -f 2 -d =)
      echo "  <div class=\"navSectionHead\">$header</div>" >> $out
    else
      local url=$(echo $line | cut -f 1 -d \|)
      local desc=$(echo $line | cut -f 2 -d \|)
      echo "$url" | grep '^+' 2>&1 > /dev/null
      if [ $? -eq 0 ] ; then
        local url=$(echo $url | cut -f 2 -d +)
        echo "    <div class=\"navLink\"><i><small><a href=\"$url\">$desc</a></i></small></div>" >> $out
      else
        echo "    <div class=\"navLink\"><small><a href=\"$prefix$navmodule/$url\">$desc</a></small></div>" >> $out
      fi
    fi
  done < "$nav"

  echo '</div>' >> $out
}

target_jsr94()
{
  build core smf io 
  target_compile jsr94
}

target_groovy()
{
  build core smf
  target_compile groovy
}

target_python()
{
  build core smf
  target_compile python
}

target_java()
{
  build core smf
  target_compile java
}

target_base()
{
  build core smf
  target_compile base
}

target_io()
{
  build core smf
  target_compile io
}

target_smf()
{
  build core
  target_compile smf
}

target_core()
{
  target_compile core
}

target_prepare()
{
  echo "preparing filesystem"
  mkdir -p $BASE/build/
}

target_compile()
{
  local modules=$1

  if [ -z $modules ] ; then
    modules=$COMPILATION_MODULES
  fi
  build prepare

  local module 

  for module in $modules ; do 
    module_build     $module
    module_compile   $module
    module_make_jar  $module
    module_copy_jar  $module
    module_copy_deps $module
  done
}

module_build()
{
  if [ ! -f drools-$module/build.sh ] ; then
    return
  fi

  cd drools-$module
  /bin/sh build.sh  
  cd -
}

module_compile()
{
  local module=$1

  mkdir -p drools-$module/build/classes/
  echo "compiling module $module"
  copy_tree drools-$module/src/main/ drools-$module/build/sources/ java 
  _javac $module
}

module_make_jar()
{
  local module=$1

  echo "jarring module $module"
  copy_tree drools-$module/src/main/ drools-$module/build/classes/ properties 
  copy_tree drools-$module/src/conf/ drools-$module/build/classes/ properties
  _jar $module
}

module_copy_jar()
{
  local module=$1

  echo "copying $module jar"
  mkdir -p $BASE/build/lib/

  cp drools-$module/build/drools-$module-$VERSION.jar $BASE/build/lib
}

module_copy_deps()
{
  local module=$1

  echo "copy $module dependencies"
  mkdir -p $BASE/build/lib/
  if [ -d drools-$module/lib ] ; then
    cp drools-$module/lib/*.jar $BASE/build/lib
  fi
}

target_javadoc()
{
  build compile

  echo "building javadocs"

  local sourcepath=""

  for module in $MODULES ; do 
    if [ -z $sourcepath ] ; then
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
      -d $BASE/build/docs/api/ -subpackages org.drools:bsh.commands\
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

target_pdf()
{
  echo "building PDFs from LaTeX"

  mkdir -p $BASE/build/latex

  #local module
  #for module in $MODULES ; do
  # if [ -d drools-$module/src/latex ] ; then
  #   copy_tree drools-$module/src/latex $BASE/build/latex/$module/ tex 
  # fi 
  #done

  copy_tree $BASE/src/latex/ $BASE/build/latex/ tex pdf 

  local doc
 
  for doc in $(ls -d $BASE/build/latex/*) ; do
    local docname="drools-$(basename $doc)-$VERSION.pdf"
    echo "generating $docname"
    cd ${doc}
    pdflatex ${doc}/document.tex
    makeindex ${doc}/document.idx
    pdflatex ${doc}/document.tex
    pdflatex ${doc}/document.tex
    pdflatex ${doc}/document.tex
    cd - 
    mkdir -p $BASE/build/docs
    cp $doc/document.pdf $BASE/build/docs/drools-$(basename $doc)-$VERSION.pdf
  done
}

target_dists()
{
  build source_dist binary_dist
}

target_source_dist()
{
  echo "creating source distribution" 

  mkdir -p $BASE/build/dists/drools-$VERSION-src/

  cp *.txt $BASE/build/dists/drools-$VERSION-src/
  cp *.sh  $BASE/build/dists/drools-$VERSION-src/
  cp -R ./lib $BASE/build/dists/drools-$VERSION-src/
  cp -R ./site $BASE/build/dists/drools-$VERSION-src/

  local module

  for module in $MODULES ; do
    copy_tree drools-$module/ $BASE/build/dists/drools-$VERSION-src/drools-$module java html properties sh jar g nav
  done

  cd $BASE/build/dists
  tar zcvf drools-$VERSION-src.tar.gz drools-$VERSION-src
  zip -r9 drools-$VERSION-src.zip drools-$VERSION-src
  cd -
}

target_binary_dist()
{
  echo "creating binary distribution" 

  build compile javadoc

  mkdir -p $BASE/build/dists/drools-$VERSION/

  cp *.txt  $BASE/build/dists/drools-$VERSION/
  copy_tree $BASE/build/lib/ $BASE/build/dists/drools-$VERSION/lib
  copy_tree $BASE/build/docs/api $BASE/build/dists/drools-$VERSION/docs

  cd $BASE/build/dists
  tar zcvf drools-$VERSION.tar.gz drools-$VERSION
  zip -r9 drools-$VERSION.zip drools-$VERSION
  cd -
}

target_deploy_site()
{
  build clean site

  cd $BASE/build/site
  tar zcvf drools-$VERSION-site.tar.gz .
  ssh $DEPLOY_SITE "rm -Rf $DEPLOY_DIR"
  ssh $DEPLOY_SITE "mkdir -p $DEPLOY_DIR"
  scp drools-$VERSION-site.tar.gz $DEPLOY_SITE:$DEPLOY_DIR
  cd -

  ssh $DEPLOY_SITE "cd $DEPLOY_DIR && tar zxvf drools-$VERSION-site.tar.gz"
}

target_clean()
{
  echo "cleaning filesystem"
  rm -Rf $BASE/build

  local module 

  for module in $MODULES ; do 
    rm -Rf drools-$module/build 
  done
}

_javac()
{
  local module=$1

  cd drools-$module

  $JAVAC \
    -classpath $(dyn_classpath) \
    -sourcepath ./build/sources/ \
    -d ./build/classes/ \
    -deprecation \
    -g \
    $(find ./build/sources/ -name '*.java')

  cd -
}

_jar()
{
  local module=$1

  cd drools-$module

  $JAR -cf ./build/drools-$module-$VERSION.jar -C ./build/classes . 

  cd - 
}

copy_tree()
{
  local source=$1
  local dest=$2
  local exts=""

  if [ ! -d $source ] ; then
    return
  fi

  if [ ! -z "$3" ] ; then 
    exts="$exts $3"
  fi
  if [ ! -z "$4" ] ; then 
    exts="$exts $4"
  fi
  if [ ! -z "$5" ] ; then 
    exts="$exts $5"
  fi
  if [ ! -z "$6" ] ; then 
    exts="$exts $6"
  fi
  if [ ! -z "$7" ] ; then 
    exts="$exts $7"
  fi
  if [ ! -z "$8" ] ; then 
    exts="$exts $8"
  fi
  if [ ! -z "$9" ] ; then 
    exts="$exts $9"
  fi

  mkdir -p $dest
  cd $dest
  dest=$PWD
  cd -

  cd $source

  if [ -z "$exts" ] ; then
    find ./ -depth -print | cpio -pudm --quiet $dest
  else
    for ext in $exts ; do
      find ./ -depth -name \*.$ext -print | cpio -pudm --quiet $dest
    done
  fi

  cd -
}

function dyn_javadoc_classpath()
{
  local jars=$BASE/build/lib/*.jar

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

dyn_classpath()
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

dyn_common_classpath()
{
  local jars=$BASE/build/lib/*.jar

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


##
##
##

default_targets="all"

if [ -z "$*" ] ; then
  targets=$default_targets
else
  targets=$*
fi

BASE=$PWD
export NOW_DAY="$(date +'%d')"
export NOW_MONTH="$(date +'%B')"

build $targets


basedir=$PWD

DIR=$basedir/build/sources/org/drools/semantics/java/parser

mkdir -p $DIR

cd ./src/main/org/drools/semantics/java/parser/

$JAVA \
    -classpath $basedir/lib/antlr-*.jar \
    antlr.Tool \
      -o $DIR \
      java.g 2>&1 > /dev/null

$JAVA \
    -classpath $basedir/lib/antlr-*.jar \
    antlr.Tool \
      -o $DIR \
      java.tree.g 2>&1 > /dev/null

cd -

BASE=$PWD
DIR=$BASE/build/sources/org/drools/semantics/java/parser
mkdir -p $DIR

( 
cd ./src/main/org/drools/semantics/java/parser/
$JAVA \
    -classpath $BASE/lib/antlr-*.jar \
    antlr.Tool \
      -o $DIR \
      java.g 2>&1
) 2>&1 > /dev/null

( 
cd ./src/main/org/drools/semantics/java/parser/
$JAVA \
    -classpath $BASE/lib/antlr-*.jar \
    antlr.Tool \
      -o $DIR \
      java.tree.g 2>&1
) 2>&1 > /dev/null

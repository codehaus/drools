@ECHO OFF

echo.
echo -----------------------------------------------------------------------
echo  YOU HAVE TO SET PATHS TO YOUR JAR FILES IN ORDER FOR THIS TO WORK !!!
echo -----------------------------------------------------------------------
echo.

if '%1 == ' goto no-params

if %1 == help goto myhelp

set rootdir=..\..\..\

set which_xerces=xerces-2_2_0
set which_ibm=IBM_SQC-2_0
set which_xsv=xsv12

if %1 == -i goto ibm

if %1 == -x goto xerces

goto xsv

:xerces
   if not exist %rootdir%jre\1.2\lib\rt.jar goto no-rt
   if not exist %2 goto no-file
   if not exist %rootdir%xmlParsers\%which_xerces%\xercesImpl.jar goto no-xer-jar
   if not exist %rootdir%xmlParsers\%which_xerces%\xercesSamples.jar goto no-xer-samples
   if not exist %rootdir%xmlParsers\%which_xerces%\xmlParserAPIs.jar goto no-xml-Parser_APIs

   set oldPath=%path%
   set oldCLASSPATH=%CLASSPATH%

   set path=%rootdir%jre\1.2\bin

   set CLASSPATH=.

   set xerces_CLASSPATH=%CLASSPATH%;%rootdir%xmlParsers\%which_xerces%\xercesImpl.jar
   set xerces_CLASSPATH=%xerces_CLASSPATH%;%rootdir%xmlParsers\%which_xerces%\xmlParserAPIs.jar
   set xerces_CLASSPATH=%xerces_CLASSPATH%;%rootdir%xmlParsers\%which_xerces%\xercesSamples.jar

   java -classpath %xerces_CLASSPATH% sax.Counter -v -s -f %2

   echo.
   echo Done.
   echo.
   set path=%oldPath%
   set CLASSPATH=%oldCLASSPATH%
   goto eof

:xsv
   if not exist %1 goto no-file
   if not exist %rootdir%xmlParsers\%which_xsv%\xsv.exe goto no-xsv-exe
   set oldPath=%path%
   set path=%rootdir%xmlParsers\%which_xsv%
   echo.
   echo ---------------------------------------------------------
   echo Running the XSV Schema Validator with the following specification:
   echo    - XML File Being Validated: %1
   echo ---------------------------------------------------------
   xsv %1
   echo.
   echo Done.
   echo.
   set path=%oldPath%
   goto eof

:ibm
   if not exist %rootdir%jre\1.2\lib\rt.jar goto no-rt
   if not exist %2 goto no-file

   set oldPath=%path%
   set oldCLASSPATH=%CLASSPATH%

   set path=%rootdir%jre\1.2\bin

   set CLASSPATH=%rootdir%jre\1.2\lib\rt.jar
   set CLASSPATH=%CLASSPATH%;.

   set STYLESHEET=%rootdir%xmlParsers\%which_ibm%\.
   set XML4J=%rootdir%xmlParsers\%which_ibm%\xml4j.jar
   set MOF=%rootdir%xmlParsers\%which_ibm%\mofrt.jar
   set REGEX=%rootdir%xmlParsers\%which_ibm%\regex4j.jar
   set XSCHEMA=%rootdir%xmlParsers\%which_ibm%\xschemaREC.jar
   set BASE64DECODER=%rootdir%xmlParsers\%which_ibm%\mail.jar
   set XERCES=%rootdir%xmlParsers\%which_ibm%\xerces.jar
   set CLASSPATH=%CLASSPATH%;%XSCHEMA%;%XERCES%;%XML4J%;%MOF%;%REGEX%;%BASE64DECODER%;%STYLESHEET%

   echo.
   echo ---------------------------------------------------------
   echo Running the IBM Schema Quality Checker with the following specification:
   echo    - XML Schema Being Validated: %2
   echo ---------------------------------------------------------

   java -classpath %CLASSPATH% com.ibm.sketch.util.SchemaQualityChecker -indicateProgress %2

   echo.
   echo Done.
   echo.
   set path=%oldPath%
   set CLASSPATH=%oldCLASSPATH%
   goto eof

:no-params
    cls
    echo Oops! You need to run the batch file with an argument.
    echo Here's how to use it ...
    echo.
    echo Usage: "validate <xml filename>" to invoke xsv
    echo Usage: "validate -i <xsd filename>" to invoke the IBM Schema Quality Checker
    echo Usage: "validate -x <xml filename>" to invoke the Xerces (Apache) Schema Validator
    echo Usage: "validate -m <xml filename> <xsd filename>" to invoke the MSXML4.0 Schema Validator
    echo.
    goto eof

:myhelp
    cls
    echo Usage: "validate <xml filename>" to invoke xsv
    echo Usage: "validate -x <xml filename>" to invoke the Xerces (Apache) Schema Validator
    echo Usage: "validate -m <xml filename> <xsd filename>" to invoke the MSXML4.0 Schema Validator
    echo.
    goto eof

:no-file
   cls
   echo Oops! %1 does not exist
   echo Most likely reason: mistyped filename
   echo Exiting ...
   echo.
   goto eof

:no-rt
   cls
   echo Oops! Missing this Java jre file: %rootdir%jre\1.1\lib\rt.jar
   echo Most likely reason: jre not installed in the correct directory
   echo Exiting ...
   goto eof

:no-xer-jar
   cls
   echo Oops! Missing this xerces file: = %rootdir%xmlParsers\%which_xerces%\xercesImpl.jar
   echo Most likely reason: xerces not installed in the correct directory
   echo Exiting ...
   goto eof

:no-xer-samples
   cls
   echo Oops! Missing this xerces file: = %rootdir%xmlParsers\%which_xerces%\xercesSamples.jar
   echo Most likely reason: xerces not installed in the correct directory
   echo Exiting ...
   goto eof

:no-xml_Parser_APIs
   cls
   echo Oops! Missing this xerces file: = %rootdir%xmlParsers\%which_xerces%\xmlParserAPIs.jar
   echo Most likely reason: xerces not installed in the correct directory
   echo Exiting ...
   goto eof

:no-xsv-exe
   cls
   echo Oops! Missing this xsv file: = %rootdir%xmlParsers\%which_xsv%\xsv.exe
   echo Most likely reason: xsv not installed in the correct directory
   echo Exiting ...
   goto eof

:no-xsd
   cls
   echo Oops! You must supply a schema file to run MSXML4.0
   echo Exiting ...
   goto eof


:eof
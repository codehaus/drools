
DRL-Schemata set version 1.0 
September 7, 2003
Roger F. Gay
roger.f.gay@picslt.org

This delivery of the DRL-Schemata set is sufficient for use in performing W3C
xml validation on drl files. Additional code is provided to assist in performing
validation. Additional software is needed from, e.g. Microsoft or Apache Xerces.
Software is included in this package that will perform validation once the 
appropriate software is available (much or all of which people involved with 
drools or java xml processing generally may already have). The schemata are 
filled with comments to assist in quick understanding.


DRL-Schemata set includes;

drl.xsd (DRL), java.xsd, python.xsd, jelly.xsd, jellyTags.xsd


The delivery zip file (drlxsd-v10.zip) includes;
  drl.xsd (DRL-Schema), 
  java.xsd, python.xsd, jelly.xsd, jellyTags.xsd (semantic-module support schemata)
  test samples (xsd declared versions of sisters.drl, dimensions.drl, 
                                         escalation.drl, and fishmonger.drl)
  readme.txt (this message)
  msxsd4_validator.htm (easy to use html validation page relies on MS validator)
  alt_validator.bat (batch file for running other validation software)


The DRL-Schemata version 1.0 apparently takes drl file validation against a W3C 
schema as far as W3C schema validation can go, or at least nearly so. Additional 
work is being done to complete drl file validation with supplementary programs 
which may include non-W3C schema supplementary validation (there is a prototype). 

Attention has been focused on the definition of drools rule language (DRL) more 
than the details of semantic module use. The use of drl's consequence tag for example, 
is well defined. The use of the java:consequence tag is merely defined. The schemata
do not define any validation at all for the contents of a java:consequence tag.

I'm still asking questions about the use of application jelly tags that are embedded 
within jelly semantic declaration tags (like know:assert within jelly:consequence).
At present the jelly tag schema is sufficient for the example that I have (sisters.drl).


Known limitations include:

not checking to assure that semantic modules are declared before they are used. 
The current schema will catch java:consequence (for example) if the java related 
namespace has not been declared, but it does not know if you've used the semantics
tag to declare the java semantics module. It does check to see if the semantic 
tags used in a drl file declare currently supported semantic modules, and perhaps
gratuitously; it will not allow you to declare the same semantics module more 
than once.

not checking to assure that declarations are populated by extractions. 

content within semantics tag usage is merely defined as type string. No checks 
are performed to assure that the syntax of java, python, or jelly script is 
correct, that external objects are available, that they in any way 
perform the function that they should, or even that they exist.
       

Known problem:

The version of drools that I have (today's date is September 7, 2003) throws an 
error when the drl file contains reference to a schema. This means that at 
present, if reference to drl.xsd is included (as it is in all the examples in
this delivery), it must be removed before running it through drools. This is
accomplised by removing two attributes from the rules tag.

       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
       xsi:schemaLocation="http://drools.org/rules drl.xsd"



<?xml version="1.0" encoding="utf-8"?> is also an addition in this delivery. 
Every xml file should be declared as such, and drl files are xml files.
In the spirit of documenting all changes; "I added that."



Roger F. Gay
roger.f.gay@picslt.org

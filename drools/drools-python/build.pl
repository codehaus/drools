#!/usr/bin/perl

########################################################################
##
##  Universal Java Application Launcher
##
########################################################################

use strict;
use Cwd;

##
## Constants because I Like Java's concept of booleans
##

my $true  = 1;
my $false = 0;


##
## Configuration;
##

my %uja;

$uja{mainClass}              = "org.apache.tools.ant.Main";
$uja{libPath}                = "./lib";
$uja{includeSystemClassPath} = $false;
$uja{includeJavaTools}       = $true;
$uja{shouldDumpInfo}         = $true;

##
## Java-centric variables
##

my $classpath;

##
## Platform variables
##
my $cp_delim;

if ( $^O =~ /win32/i ) {
  $cp_delim = ";";
} else {
  $cp_delim = ":";
}

$| = 1;

my @tools_classpath;
my @lib_classpath;

## ------------------------------------------------------------------------
## ------------------------------------------------------------------------

if ( &checkJavaHome() ) {
  exit( 1 );
}

&setupJava();
&dumpInfo();
&execProg();

## ------------------------------------------------------------------------
## ------------------------------------------------------------------------

sub dumpInfo()
  {
	if ( ! $uja{shouldDumpInfo} ) {
	  return;
	}
	
	print "-----------------------------------------------\n\n";

	my $sec;
	my $min;
	my $hour;
	my $mday;
	my $mon;
	my $year;
	my $wday;
	my $yday;
	my $isdst;

	($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime(time);

	$year = $year + 1900;

	my $weekday = ("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")[$wday];
	my $month   = ("January","February","March","April","May","June","July","August","September","October","November","December")[$mon];


	print "executable: $0\n";
	print " arguments: @ARGV\n\n";
	print "      date: $hour:$min:$sec $weekday $mday-$month-$year\n"; 
	print " directory: ", cwd(), "\n\n";

	print "-- CLASSPATH ----------------------------------\n";

	my $element;
	foreach $element ( split $cp_delim, $uja{classPath} ) {
	  print "    $element\n";
	}

	print "-----------------------------------------------\n";
  }

sub execProg()
  {
	system "$uja{javaHome}/bin/java -cp $uja{classPath} $uja{mainClass} @ARGV";
  }

sub setupJava()
  {
	$uja{javaHome} = $ENV{"JAVA_HOME"};

	my $classpath = "";

	if ( $uja{includeJavaTools} )
	  {
		my $toolsJar = "$uja{javaHome}/lib/tools.jar";

		if ( $classpath eq "" )
		  {
			$classpath = $toolsJar;
		  }
		else
		  {
			$classpath = join $cp_delim, $classpath, $toolsJar;
		  }
	  }

	my @lib_jars   = glob("$uja{libPath}/*.jar");
	my @lib_zips   = glob("$uja{libPath}/*.zip");

	if ( $classpath eq "" ) 
	  {
		$classpath = join $cp_delim, @lib_jars, @lib_zips;
	  }
	else
	  {
		$classpath = join $cp_delim, $classpath, @lib_jars, @lib_zips;
	  }
		
	if ( $uja{includeSystemClassPath} )
	  {
		my $system_classpath;

		$system_classpath = $ENV{"CLASSPATH"};

		if ( $classpath eq "" )
		  {
			$classpath = $system_classpath;
		  }
		else
		  {
			$classpath = join $cp_delim, $classpath, $system_classpath;
		  }
	  }
	
	$uja{classPath} = $classpath;
	$ENV{"CLASSPATH"} = $classpath;
  }


sub checkJavaHome()
  {
	if ( $ENV{"JAVA_HOME"} eq "" ) {
	  print "ERROR: JAVA_HOME not found in your environment.\n\n";
	  print "Please, set the JAVA_HOME variable in your environment\n";
	  print "to match the location of the Java Virtual Machine\n";
	  print "that you want to use.\n\n";
	
	  return 1;
	}

	$uja{javaHome} = $ENV{"JAVA_HOME"};
	return 0;
  }

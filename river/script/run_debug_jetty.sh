#!/bin/bash
#
# Run a local jetty dev server that's configured for IntelliJ debugging
# IntelliJ must have a debugger configured

# the jetty port can be customized in pom.xml
port=8080

# pass --clean to run mvn clean prior to starting jetty
if [[ $1 == "--clean" ]]; then
	clean=true
	shift
fi

if $clean; then
  cmd="mvn clean jetty:run"
else
  cmd="mvn jetty:run"
fi

MAVEN_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${port}" $cmd

#!/usr/bin/env bash

if [ ! -z "${JAVA_17_HOME}" ]; then
  echo JAVA_17_HOME: ${JAVA_17_HOME}
  JAVA_HOME=${JAVA_17_HOME}
fi

MVN="`which mvn`"
if [ ! -z "${MAVEN_HOME}" ]; then
  echo MAVEN_HOME: ${MAVEN_HOME}
  MVN="${MAVEN_HOME}/bin/mvn"
fi

if [ -z "$MVN" ]; then
    echo maven not found.
  exit 1
else
    #$MVN -Dtycho.debug.resolver=true -X -f ./pom.xml clean package
    $MVN -f ./pom.xml clean package
fi

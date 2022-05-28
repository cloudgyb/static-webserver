#!/bin/sh

currDir=$(cd "$(dirname "$0")" && pwd)

if [ ! -d "$JAVA_HOME" ]; then
  echo "No set JAVA_HOME!"
  exit 1
fi

echo Using JAVA_HOME: "$JAVA_HOME"

EXECUTABLE=java

if [ ! -x "$JAVA_HOME"/bin/"$EXECUTABLE" ]; then
  echo "Cannot find $JAVA_HOME/bin/$EXECUTABLE"
  echo "The file is absent or does not have execute permission"
  echo "This file is needed to run this program"
  exit 1
fi

"$JAVA_HOME"/bin/$EXECUTABLE -Dserver.host=0.0.0.0 -Dserver.port=8080 -jar "$currDir/../lib/webserver.jar" \
  --web-root="$currDir"/../WEBROOT

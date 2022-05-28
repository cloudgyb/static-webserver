currDir=$(cd "$(dirname "$0")" && pwd)

[ -e "$currDir/target/dist" ] && rm -rf "$currDir/target/dist"

BIN_DIR="$currDir/target/dist/bin"
LIB_DIR="$currDir/target/dist/lib"
LOG_DIR="$currDir/target/dist/logs"
WEB_ROOT="$currDir/target/dist/WEBROOT"

mkdir -p "$BIN_DIR"
mkdir -p "$LIB_DIR"
mkdir -p "$LOG_DIR"
mkdir -p "$WEB_ROOT"

cp "$currDir"/bin/startup.* "$BIN_DIR"

cp "$currDir"/target/*.jar "$LIB_DIR"
mv "$LIB_DIR"/static-webserver-*.jar "$LIB_DIR/webserver.jar"

cp "$currDir"/target/lib/* "$LIB_DIR"

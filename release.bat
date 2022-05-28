@echo off

cd /d %~dp0

setlocal


del /Q /S "target/dist"
set "BIN_DIR=target\dist\bin"
set "LIB_DIR=target\dist\lib"
set "LOG_DIR=target\dist\logs"
set "WEB_ROOT=target\dist\WEBROOT"


mkdir %BIN_DIR%
mkdir %LIB_DIR%
mkdir %LOG_DIR%
mkdir %WEB_ROOT%

copy /Y bin\startup.bat %BIN_DIR%

copy /Y target\*.jar %LIB_DIR%
move /Y %LIB_DIR%\static-webserver-*.jar %LIB_DIR%\webserver.jar

copy /Y target\lib\* %LIB_DIR%

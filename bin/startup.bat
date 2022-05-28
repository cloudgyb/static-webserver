@echo off
cd /d %~dp0
setlocal

if not exist "%JAVA_HOME%\bin\java.exe" goto noJAVA
goto startup

:noJAVA
echo "No set JAVA_HOME!"
exit 1

:startup
echo Using JAVA_HOME: "%JAVA_HOME%"
rem 支持java系统属性选项：
rem    -Dserver.host=<host/ip>  设置服务绑定的主机名或ip，默认localhost
rem    -Dserver.port=<port>     设置服务绑定的端口号，默认80
rem    -Dweb.root=<DIR>         设置静态资源部署目录，默认为./WEBROOT
rem -----------------------------------------------------------
rem 支持的程序参数（优先级高于系统属性选项）：
rem    --server-host=<host/ip>  设置服务绑定的主机名或ip，默认localhost
rem    --server-port=<port>     设置服务绑定的端口号，默认80
rem    --web-root=<DIR>         设置静态资源部署目录，默认为./WEBROOT

"%JAVA_HOME%\bin\java.exe" -Dserver.port=80 -jar ..\lib\webserver.jar --web-root=..\WEBROOT
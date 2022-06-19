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
rem    -Dserver.port=<port>     设置服务绑定的端口号，http默认80，https、h2默认443
rem    -Dweb.root=<DIR>         设置静态资源部署目录，默认为./WEBROOT
rem    -Dserver.ssl.key=<path>  设置ssl证书私钥文件路径
rem    -Dserver.ssl.cert=<path> 设置ssl证书文件路径（PEM格式）
rem -----------------------------------------------------------
rem 支持的程序参数（优先级高于系统属性选项）：
rem    --server-host=<host/ip>  设置服务绑定的主机名或ip，默认localhost
rem    --server-port=<port>     设置服务绑定的端口号，http默认80，https、h2默认443
rem    --web-root=<DIR>         设置静态资源部署目录，默认为./WEBROOT
rem    --server-ssl-key=<path>  设置ssl证书私钥文件路径
rem                             例如：--server-ssl-key=C:\Users\ASUS\Downloads\private.key
rem    --server-ssl-cert=<path> 设置ssl证书文件路径（PEM格式）
rem                             例如：--server-ssl-cert=C:\Users\ASUS\Downloads\cert.pem

"%JAVA_HOME%\bin\java.exe" -Dserver.port=80 -jar ..\lib\webserver.jar --web-root=..\WEBROOT
@echo on

setlocal
set JAVA_HOME=D:\Java\jdk-11.0.2+9
set PATH=%JAVA_HOME%\bin;%PATH%
set WEBAPP_HOME=D:\webapps\ksbysample-webapp-lending
set WEBAPP_JAR=ksbysample-webapp-lending-2.1.3-RELEASE.jar
set LOGS_DIR=%WEBAPP_HOME%\logs
set PATH_HEAPDUMPFILE=%LOGS_DIR%\heapdump.hprof
set PATH_ERRORFILE=%LOGS_DIR%\hs_err.log

cd /d %WEBAPP_HOME%
java -Xms1024m -Xmx1024m ^
     -XX:MaxMetaspaceSize=384m ^
     -XX:+UseParallelGC ^
     -Xlog:gc*=debug:logs/gc.log:time,level,tags:filesize=10m,filecount=5 ^
     -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%PATH_HEAPDUMPFILE% ^
     -XX:+CrashOnOutOfMemoryError ^
     -XX:ErrorFile=%PATH_ERRORFILE% ^
     -Dsun.net.inetaddr.ttl=100 ^
     -Dcom.sun.management.jmxremote ^
     -Dcom.sun.management.jmxremote.port=7900 ^
     -Dcom.sun.management.jmxremote.ssl=false ^
     -Dcom.sun.management.jmxremote.authenticate=false ^
     -Dspring.profiles.active=product ^
     -jar lib\%WEBAPP_JAR%

set YYYYMMDD=%date:~0,4%%date:~5,2%%date:~8,2%
set HHMMSS=%time:~0,8%
set HHMMSS=%HHMMSS::=%
set HHMMSS=%HHMMSS: =%
if exist %PATH_HEAPDUMPFILE% rename %PATH_HEAPDUMPFILE% heapdump_%YYYYMMDD%%HHMMSS%.hprof
if exist %PATH_ERRORFILE% rename %PATH_ERRORFILE% hs_err_%YYYYMMDD%%HHMMSS%.log

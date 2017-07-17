@echo on

setlocal
set JAVA_HOME=C:\Java\jdk1.8.0_131
set PATH=%JAVA_HOME%\bin;%PATH%
set WEBAPP_HOME=C:\webapps\ksbysample-webapp-lending
set WEBAPP_JAR=ksbysample-webapp-lending-1.5.4-RELEASE.jar
set LOGS_DIR=%WEBAPP_HOME%\logs
set PATH_HEAPDUMPFILE=%LOGS_DIR%\heapdump.hprof
set PATH_ERRORFILE=%LOGS_DIR%\hs_err.log

cd /d %WEBAPP_HOME%
java -server ^
     -Xms1024m -Xmx1024m ^
     -XX:MaxMetaspaceSize=384m ^
     -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled ^
     -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75 ^
     -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark ^
     -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps ^
     -Xloggc:%WEBAPP_HOME%/logs/gc.log ^
     -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M ^
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

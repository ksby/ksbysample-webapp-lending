#!/bin/sh
# 改行コードを LF にすること。LF でないと実行されない。

export JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MaxMetaspaceSize=384m"

exec java $JAVA_OPTS \
          -Djava.security.egd=file:/dev/./urandom \
          -Dspring.profiles.active=product \
          -Dserver.tomcat.basedir=/ \
          -Dlogging.appender=CONSOLE \
          -jar /app.jar

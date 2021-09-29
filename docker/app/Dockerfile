FROM eclipse-temurin:17_35-jdk-alpine

RUN apk update && \
    apk upgrade; \
    wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub; \
    wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-2.31-r0.apk; \
    wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-bin-2.31-r0.apk; \
    wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-i18n-2.31-r0.apk; \
    apk add --no-cache --update \
    bash \
    tzdata \
    glibc-2.31-r0.apk \
    glibc-bin-2.31-r0.apk \
    glibc-i18n-2.31-r0.apk; \
    rm glibc-2.31-r0.apk glibc-bin-2.31-r0.apk glibc-i18n-2.31-r0.apk

RUN /usr/glibc-compat/bin/localedef -i ja_JP -f UTF-8 ja_JP.UTF-8

ENV TZ Asia/Tokyo
ENV LANG ja_JP.utf8
ENV LC_ALL ja_JP.utf8

VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["docker-entrypoint.sh"]

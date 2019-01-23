FROM openjdk:8-jdk-alpine
RUN apk add --no-cache tzdata
ENV TZ="Asia/Tokyo"
ENV LANG="ja_JP.UTF-8"
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["docker-entrypoint.sh"]

FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-alpine-slim
RUN apk add --no-cache tzdata
ENV TZ="Asia/Tokyo"
ENV LANG="ja_JP.UTF-8"
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["docker-entrypoint.sh"]

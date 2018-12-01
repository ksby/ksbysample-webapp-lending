ARG REDIS_VERSION
FROM redis:${REDIS_VERSION}
RUN apt-get update -qq && apt-get install -y expect
ADD redis.conf /etc/redis/

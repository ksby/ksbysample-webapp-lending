ARG RABBITMQ_VERSION
FROM rabbitmq:${RABBITMQ_VERSION}-alpine
RUN apk add --no-cache tzdata
COPY --chown=rabbitmq:rabbitmq .erlang.cookie /tmp/
RUN chmod 600 /tmp/.erlang.cookie

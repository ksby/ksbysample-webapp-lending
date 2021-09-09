ARG PGADMIN4_VERSION
FROM dpage/pgadmin4:${PGADMIN4_VERSION}

USER root
RUN mkdir /var/lib/pgadmin_session
RUN chown pgadmin:pgadmin /var/lib/pgadmin_session
RUN chmod 0700 /var/lib/pgadmin_session

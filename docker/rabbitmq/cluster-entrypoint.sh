#!/bin/bash

set -e

cp -p /tmp/.erlang.cookie /var/lib/rabbitmq/
ls -al /var/lib/rabbitmq/
rabbitmq-diagnostics erlang_cookie_sources

# Start RMQ from entry point.
# This will ensure that environment variables passed
# will be honored
rabbitmq-server -detached

# Wait a while for rabbitmq1 to start
sleep 15s

# Do the cluster dance
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster rabbit@rabbitmq1

# Stop the entire RMQ server. This is done so that we
# can attach to it again, but without the -detached flag
# making it run in the forground
rabbitmqctl stop

# Wait a while for the app to really stop
sleep 3s

# Start it
rabbitmq-server

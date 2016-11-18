#!/bin/sh

#export DOCKER_HOST,DOCKER_TLS_VERIFY,DOCKER_MACHINE_NAME,DOCKER_CERT_PATH

cd integration-test && ./gradlew clean test


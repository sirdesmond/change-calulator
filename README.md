# changecalculator implementation with kotlin, spring-cloud and docker


##run from docker hub
images have been pushed to docker hub
you can simply run:
```
docker-compose up
```
visit [docker-machine-ip]:8082/change/[amount]

###MANUALLY

To build docker images for each individual service, `cd` into the service folder and run: 
```
./gradlew buildDocker
```
or simply run attached `build-docker-images.sh` shell script.

###Compose Up
To start all the services from the command line, just run `docker-compose up`.

###Run All Tests
To run all tests run the `run-all-tests.sh` script.

###URIs/Endpoints
- [Master Service](http://192.168.99.100:8081/change/<amount>)
- [Calculator Service](http://192.168.99.100:8082/optimalChange/<amount>)

#Entry point in docker for debug. FYI I couldn't get this to work
```
"java","-Djava.security.egd=file:/dev/.urandom/","-Xdebug","-Xrunjdwp:server=y,transport=dt_socket,suspend=n","-jar","/app.jar"
```

###For Integration Tests

#Added the following to IDE configurations but they are exported when you run 
`eval $(docker-machine env default)`

```
DOCKER_TLS_VERIFY=1
DOCKER_HOST=tcp://192.168.99.100:2376
DOCKER_CERT_PATH=/Users/kofikyei/.docker/machine/machines/default
DOCKER_MACHINE_NAME=default
```
run the `run-integration-tests.sh` script.


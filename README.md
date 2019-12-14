# A "message board" service built with Spring Boot

An implementation of a small message board service.

> At the time of writing this is just a toy project used to try out Spring Boot.  

- [A &quot;message board&quot; service built with Spring Boot](#a-quotmessage-boardquot-service-built-with-spring-boot)
  - [API](#api)
    - [Create a message](#create-a-message)
    - [Fetch all messages](#fetch-all-messages)
    - [Fetch one message](#fetch-one-message)
    - [Modify a message](#modify-a-message)
    - [Delete a message](#delete-a-message)
  - [Backlog](#backlog)
  - [Build and deploy with Docker](#build-and-deploy-with-docker)
    - [Alternative 1](#alternative-1)
    - [Alternative 2](#alternative-2)
  - [Build and deploy with local JDK](#build-and-deploy-with-local-jdk)
  - [Convenience scripts to test the api](#convenience-scripts-to-test-the-api)

## Description

A RESTful API to serve as the backend for a public message board. 

Features:
- A client can create a message in the service
- A client can modify their own messages
- A client can delete their own messages
- A client can view all messages in the service

For now an in-memory solution for storing data is used.

## API

### Create a message

- Http verb: POST
- Endpoint: `<host>/api/v1/message`
- Example:

    ```bash
    curl  --request POST \
      --header "Content-Type: application/json" \
      --data "{"userId":"Doris", "messageId":"Martha", "message":"A message about a book." }" \
      --url  http://localhost:8080/api/v1/message
    ```
> Also see convenience scripts below.

### Fetch all messages

- Http verb: `GET`
- Endpoint: `<host>/api/v1/message/all`
- Example: `$ curl --silent http://localhost:8080/api/v1/message/all`

### Fetch one message

- Http verb: `GET`
- Endpoint: `<host>/api/v1/message/<user>/<message-id>`
- Example: `curl --silent http://localhost:8080/api/v1/message/Doris/Martha`

> Also see convenience scripts below.

### Modify a message

- Http verb: `PUT`
- Endpoint: `<host>/api/v1/message`
- Example:

    ```bash
    curl  --request PUT \
          --header "Content-Type: application/json" \
          --data "{"userId":"Doris", "messageId":"Martha", "message":"A MODIFIED message about a book." }" \
          --url  http://localhost:8080/api/v1/message
    ```
> Also see convenience scripts below.

### Delete a message

- Http verb: `DELETE`
- Endpoint: `<host>/api/v1/message`
- Example:

    ```bash
    curl  --request DELETE \
          --header "Content-Type: application/json" \
          --data "{"userId":"Doris", "messageId":"Martha", "message":"" }" \
          --url  http://localhost:8080/api/v1/message
    ```
> Also see convenience scripts below.

## Backlog

This is work in progress and a lot of things can be improved, but keep in mind that this is just a toy project.

- Logging.
- Authentication
- Implement a permanent storage service. 
- More javadoc.
- Extend the REST api.
- Implement available but not implemented endpoints.
- Use swagger to define and document the [api](https://swagger.io).
- I'm sure there are more things... :-)

## Build and deploy with Docker

### Alternative 1

> Docker without a custom Docker image

Tools needed:
 
- Git 
- Docker
- Curl 
- Jq (used in the convenience scripts to prettify the response output) 

In this scenario we use Docker to:

- build an executable jar that ends up on your local drive in directory `<project>/build/libs`
- fire up the built executable jar (with the servlet container) in a docker container to make the message board endpoints available. 
 
```bash
$ git checkout ....
$ cd <project-location>

# Build an executable standalone jar of the project 
$ docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:jdk11 gradle build

# Start
$ docker run --rm -d -v "$PWD":/project -w /project -p 8080:8080 adoptopenjdk/openjdk11:alpine-slim java -jar build/libs/msgboardpoc-0.0.1-SNAPSHOT.jar
```

### Alternative 2

> Custom image with Docker and Buildkit

Tools needed:

- Docker
- Curl and jq for testing.

```bash
# Build custom image
$ DOCKER_BUILDKIT=1 docker build -t messageboard .

# Run container with messageboard service from the custom built image
$ docker run -d --name mymessageboard -p 8080:8080 messageboard

# List image and contaienr
$ docker image ls
$ docker container ls
```

## Build and deploy with local JDK

Tools needed:
- java 11
- [Curl](https://curl.haxx.se)
- [jq](https://stedolan.github.io/jq/)

```bash
$ git checkout ....
$ cd <project-location>
$ export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-11.0.5.jdk/Contents/Home/"
$ ./gradlew build
$ java -jar build/libs/msgboardpoc-0.0.1-SNAPSHOT.jar
```

## Convenience scripts to test the api

Test the API with [Postman](https://www.getpostman.com) or [Curl](https://curl.haxx.se) 
or with the provided convenience scripts (which use curl and [jq](https://stedolan.github.io/jq/)).

```bash
$ cd <project>/scripts

# Fill with data
$ ./createMessages

# Get all messages
$ ./getAll

# Create a message
$ ./create Simone Mandarinerna "A very good book."

# Get the created message
$ ./get Simone Mandarinerna

# Modify the message
$ ./modify Simone Mandarinerna "One of the best novels ever written."

# Inspect the modification
$ ./get Simone Mandarinerna

# Delete the message
$ ./delete Simone Mandarinerna

# Get the message and verify that an error message is returned
$ ./get Simone Mandarinerna
```
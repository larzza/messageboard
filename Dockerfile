# syntax=docker/dockerfile:experimental

FROM gradle:jdk11 AS builder

WORKDIR /src
RUN git clone https://github.com/larzza/messageboard.git
#COPY . /src/messageboard
WORKDIR /src/messageboard
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache,rw \
    ./gradlew build
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache,rw \
    ./gradlew check
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache,rw \
    ./gradlew test

FROM adoptopenjdk/openjdk11:alpine-slim AS executable
COPY --from=builder /src/messageboard/build/libs/msgboardpoc-0.0.1-SNAPSHOT.jar /messageboard/
WORKDIR /messageboard
ENTRYPOINT [ "java", "-jar", "msgboardpoc-0.0.1-SNAPSHOT.jar" ]

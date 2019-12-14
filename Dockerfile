# syntax=docker/dockerfile:experimental

FROM gradle:jdk11 AS builder
RUN git clone
#COPY . /src/messageboard
WORKDIR /src/messageboard
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache,rw \
    gradle build

FROM adoptopenjdk/openjdk11:alpine-slim AS executable
COPY --from=builder /src/messageboard/build/libs/msgboardpoc-0.0.1-SNAPSHOT.jar /messageboard/
WORKDIR /messageboard
ENTRYPOINT [ "java", "-jar", "messageboard/msgboardpoc-0.0.1-SNAPSHOT.jar" ]

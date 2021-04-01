FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk11u-nightly-slim
RUN apk --no-cache add curl
COPY build/libs/*-all.jar kotlin-jpa-grpc-kafka-avro.jar
CMD java ${JAVA_OPTS} -jar kotlin-jpa-grpc-kafka-avro.jar
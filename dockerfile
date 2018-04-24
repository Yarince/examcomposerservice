FROM maven:3.5.0-jdk-8 AS build-env
COPY . /usr/src/app/
WORKDIR /usr/src/app/
RUN mvn clean package

RUN ls /usr/src/app/target

FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY --from=build-env /usr/src/app/target/ .
RUN sh -c 'touch /ExamComposerService.jar'
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /ExamComposerService.jar" ]




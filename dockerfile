FROM openjdk:8-jdk-alpine
ADD target/ExamComposerService.jar ExamComposerService.jar
EXPOSE 8080
CMD ["java", "-jar", "ExamComposerService.jar"]
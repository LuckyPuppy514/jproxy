FROM openjdk:8-jdk-alpine

COPY ./*.jar /app/jproxy.jar
COPY ./application.yml /app/application.yml
COPY ./logback-spring.xml /app/logback-spring.xml

ENTRYPOINT ["java","-jar","/app/jproxy.jar","--spring.config.location=/app/application.yml","-Dfile.encoding=utf-8"]
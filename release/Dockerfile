FROM openjdk:8-jdk-alpine

COPY ./*.jar /app/jproxy.jar
COPY ./application.yml /app/config/application.yml
COPY ./application-prod.yml /app/application-prod.yml
COPY ./logback-spring.xml /app/logback-spring.xml

ENTRYPOINT ["java","-jar","/app/jproxy.jar","--spring.config.location=/app/config/application.yml,/app/application-prod.yml","-Dfile.encoding=utf-8"]
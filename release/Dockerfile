FROM openjdk:8-jdk-alpine

COPY ./*.jar /app/jproxy.jar
COPY ./application.yml /app/conf/application.yml
COPY ./logback-spring.xml /app/conf/logback-spring.xml

ENTRYPOINT ["java","-jar","/app/jproxy.jar","-Dlogging.config=/app/conf/","--spring.config.location=/app/conf/","-Dfile.encoding=utf-8"]
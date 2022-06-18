FROM openjdk:8-jdk-alpine

COPY ./jproxy.jar /app/jproxy.jar
COPY ./application.yml /app/application.yml
COPY ./application-dev.yml /app/application-dev.yml
COPY ./application-prod.yml /app/application-prod.yml
COPY ./application-format.yml /app/application-format.yml
COPY ./application-format-custom.yml /app/application-format-custom.yml
COPY ./logback-spring.xml /app/logback-spring.xml
COPY ./docker_startup.sh /app/docker_startup.sh

CMD ["/bin/sh","/app/docker_startup.sh"]

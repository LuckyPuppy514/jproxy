FROM openjdk:8-jdk-alpine

RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig

COPY ./jproxy.jar /app/
COPY ./config/sqlite.db /app/
COPY ./config/logback-spring.xml /app/
COPY ./docker-startup.sh /app/

CMD ["/bin/sh","/app/docker-startup.sh"]

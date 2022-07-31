FROM openjdk:8-jdk-alpine

RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig

ARG DEFAULT_CONFIG_PATH=/app/default-config/
ARG APP_PATH=/app/

COPY ./config/logback-spring.xml ${DEFAULT_CONFIG_PATH}
COPY ./config/sqlite.db ${DEFAULT_CONFIG_PATH}
COPY ./jproxy.jar ${APP_PATH}
COPY ./docker-startup.sh ${APP_PATH}

CMD ["/bin/sh","/app/docker-startup.sh"]


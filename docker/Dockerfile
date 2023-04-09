FROM eclipse-temurin:17-jre

WORKDIR /app
COPY ./dependencies/ ./
COPY ./spring-boot-loader/ ./
COPY ./snapshot-dependencies/ ./
COPY ./application/ ./
COPY ./docker/entrypoint.sh ./entrypoint.sh

ENV TZ=Asia/Shanghai, PUID=0, PGID=0, UMASK=022

RUN apt update && apt install gosu && rm -rf /var/lib/apt/lists/*

ENTRYPOINT [ "/bin/bash", "/app/entrypoint.sh" ]

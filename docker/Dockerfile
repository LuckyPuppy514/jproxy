FROM eclipse-temurin:17-jre

ENV TERM="xterm" \
    TZ=Asia/Shanghai \
    PUID=0 \
    PGID=0 \
    UMASK=022 \
    JAVA_OPTS="-Xms512m -Xmx512m"

RUN set -ex && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt update -y && \
    apt install -y gosu dumb-init && \
    apt autoremove -y && \
    apt clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY ./dependencies/ ./
COPY ./spring-boot-loader/ ./
COPY ./snapshot-dependencies/ ./
COPY ./application/ ./
COPY --chmod=755 ./docker/entrypoint.sh ./entrypoint.sh

ENTRYPOINT [ "/app/entrypoint.sh" ]

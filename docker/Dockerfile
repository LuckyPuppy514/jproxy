FROM maven:3.9.1-eclipse-temurin-17 AS builder
WORKDIR /app
COPY src ./src
COPY pom.xml ./pom.xml
RUN mvn clean package -Dmaven.test.skip=true -P prod
RUN java -Djarmode=layertools -jar target/jproxy.jar extract

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
COPY ./docker/entrypoint.sh ${APP_PATH}
ENV TZ=Asia/Shanghai, PUID=0, PGID=0, UMASK=022
RUN apt update && apt install gosu && rm -rf /var/lib/apt/lists/*
ENTRYPOINT [ "/bin/bash", "/app/entrypoint.sh" ]

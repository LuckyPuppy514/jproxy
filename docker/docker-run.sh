#!/bin/bash
docker run --name jproxy \
--restart unless-stopped \
-e PUID=1000 \
-e PGID=1000 \
-e TZ=Asia/Shanghai \
-e JAVA_OPTS="-Xms512m -Xmx512m" \
-p 8117:8117 \
-v /docker/jproxy/database:/app/database \
-d luckypuppy514/jproxy:latest

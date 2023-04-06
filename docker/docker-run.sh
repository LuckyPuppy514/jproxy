#!/bin/bash
docker run --name jproxy-redis \
--restart unless-stopped \
-d redis:latest

docker run --name jproxy \
--restart unless-stopped \
-e PUID=1000 \
-e PGID=1000 \
-e TZ=Asia/Shanghai \
-e REDIS_HOST=jproxy-redis \
-e REDIS_PORT=6379 \
-e JAVA_OPTS="-Xms512m -Xmx512m" \
--link jproxy-redis \
-p 8117:8117 \
-v /docker/jproxy/database:/app/database \
-d luckypuppy514/jproxy:latest

#!/bin/bash
docker run --name jproxy \
-v /docker/jproxy/config:/app/config \
--net=host \
-e TZ=Asia/Shanghai \
--restart unless-stopped \
-d luckypuppy514/jproxy

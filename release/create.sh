#!/bin/bash
docker run --name jproxy \
-v /docker/jproxy:/app/config \
--net=host \
--restart unless-stopped \
-d luckypuppy514/jproxy

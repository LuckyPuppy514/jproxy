#!/bin/bash
docker run --name jproxy \
--net=host \
--restart unless-stopped \
-d luckypuppy514/jproxy

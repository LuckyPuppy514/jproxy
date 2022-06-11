#!/bin/bash
docker run --name jproxy \
-p 8117:8117 \
--restart unless-stopped \
-d luckypuppy514/jproxy

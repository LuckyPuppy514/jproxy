#!/bin/bash
docker buildx build --platform arm64 -f arm64v8.Dockerfile -t luckypuppy514/jproxy:arm64v8-latest --push .

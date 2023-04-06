@echo off
chcp 65001

start .\redis\redis-server.exe .\redis\redis.windows.conf
java "-Xms512m" "-Xmx512m" "-Dfile.encoding=utf-8" "-Dspring.config.location=./config/" -jar jproxy.jar
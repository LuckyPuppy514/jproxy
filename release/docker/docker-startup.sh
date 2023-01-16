#!/bin/bash
if [ -f "/app/config/sqlite.db" ];then
echo "/app/config/sqlite.db already exist"
else
awk 'BEGIN {cmd="cp -ri /app/default-config/sqlite.db /app/config/sqlite.db "; print "n" |cmd; }'
fi

if [ -f "/app/config/logback-spring.xml" ];then
echo "/app/config/logback-spring.xml already exist"
else
awk 'BEGIN {cmd="cp -ri /app/default-config/logback-spring.xml /app/config/logback-spring.xml "; print "n" |cmd; }'
fi

java ${JAVA_OPTS} -Dfile.encoding=utf-8 -Dlogging.config=/app/config/logback-spring.xml -jar /app/jproxy.jar
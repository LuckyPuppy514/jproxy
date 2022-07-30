#!/bin/bash
awk 'BEGIN {cmd="cp -ri /app/sqlite.db /app/config/sqlite.db "; print "n" |cmd; }'
rm /app/sqlite.db
awk 'BEGIN {cmd="cp -ri /app/logback-spring.xml /app/config/logback-spring.xml "; print "n" |cmd; }'
rm /app/logback-spring.xml
java -Dfile.encoding=utf-8 -Dlogging.config=/app/config/logback-spring.xml -jar /app/jproxy.jar
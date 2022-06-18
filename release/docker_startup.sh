#!/bin/bash
awk 'BEGIN {cmd="cp -ri /app/application.yml /app/config/application.yml "; print "n" |cmd; }'
rm /app/application.yml
awk 'BEGIN {cmd="cp -ri /app/application-format-custom.yml /app/config/application-format-custom.yml "; print "n" |cmd; }'
rm /app/application-format-custom.yml
java -jar /app/jproxy.jar --spring.config.location=/app/config/,/app/ -Dfile.encoding=utf-8
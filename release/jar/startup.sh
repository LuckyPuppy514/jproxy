#!/bin/bash
java -Xms256m -Xmx256m -Dfile.encoding=utf-8 -Dlogging.config=./config/logback-spring.xml -jar jproxy.jar
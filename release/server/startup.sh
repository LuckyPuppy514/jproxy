#!/bin/bash
java -Xms2048m -Xmx2048m -Dfile.encoding=utf-8 -Dlogging.config=./config/logback-spring.xml -jar jproxy.jar
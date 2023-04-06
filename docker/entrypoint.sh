#!/bin/bash

# 设置时区
if [ "${TZ}" != "$(cat /app/TIME_ZONE)" ]; then
  apk add --no-cache alpine-conf && /sbin/setup-timezone -z ${TZ} && apk del alpine-conf
  echo ${TZ} > /app/TIME_ZONE && echo "Time zone has changed: TZ=${TZ}"
fi

# 初始化持久化目录
CONFIG_PATH=/app/config
DATABASE_PATH=/app/database
if [ ! -d "${CONFIG_PATH}" ]; then
  mkdir -p ${CONFIG_PATH}
fi
if [ ! -d "${DATABASE_PATH}" ]; then
  mkdir -p ${DATABASE_PATH}
fi

# 初始化持久化配置
cp -n /app/resources/application.yml ${CONFIG_PATH}/application.yml
cp -n /app/resources/application-prod.yml ${CONFIG_PATH}/application-prod.yml
cp -n /app/resources/database/jproxy.db ${DATABASE_PATH}/jproxy.db

# 设置权限
chown -R ${PUID}:${PGID} /app/
umask ${UMASK}

# 启动应用
cd /app
apk add --update --no-cache -q su-exec || true
exec su-exec ${PUID}:${PGID} java ${JAVA_OPTS} -Dfile.encoding=utf-8 -Dspring.config.location=${CONFIG_PATH}/ -cp $( cat /app/jib-classpath-file ) $( cat /app/jib-main-class-file )

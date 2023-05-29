#!/bin/bash

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
cp -n /app/BOOT-INF/classes/application.yml ${CONFIG_PATH}/application.yml
cp -n /app/BOOT-INF/classes/application-prod.yml ${CONFIG_PATH}/application-prod.yml
cp -n /app/BOOT-INF/classes/database/jproxy.db ${DATABASE_PATH}/jproxy.db

# 设置权限
chown -R ${PUID}:${PGID} /app/
umask ${UMASK}

# 启动应用
exec gosu ${PUID}:${PGID} dumb-init java ${JAVA_OPTS} -Dfile.encoding=utf-8 -Dspring.config.location=${CONFIG_PATH}/ org.springframework.boot.loader.JarLauncher

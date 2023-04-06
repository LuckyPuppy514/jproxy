Set ws = CreateObject("WScript.Shell")
ws.Run ".\redis\redis-server.exe .\redis\redis.windows.conf", 0
ws.Run "java -Xms512m -Xmx512m -Dfile.encoding=utf-8 -Dspring.config.location=./config/ -jar jproxy.jar", 0
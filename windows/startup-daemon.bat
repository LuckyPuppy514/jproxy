@echo off
chcp 65001

if "%1"=="h" goto begin
start mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit
:begin
java "-Xms512m" "-Xmx512m" "-Dfile.encoding=utf-8" "-Dspring.config.location=./config/" -jar jproxy.jar
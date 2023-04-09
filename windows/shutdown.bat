@echo off

REM 使用 UTF8 编码
chcp 65001

setlocal
set redis=redis-server.exe
tasklist /fi "imagename eq %redis%" | find /i "%redis%" >nul
if %errorlevel% equ 0 (
    echo Redis is running. Try to kill ......
    taskkill /F /IM %redis%
    echo Redis was killed.
) else (
    echo Redis is not running.
)
endlocal

REM 注意：这会杀掉所有 java.exe 进程
setlocal
set jproxy=java.exe
tasklist /fi "imagename eq %jproxy%" | find /i "%jproxy%" >nul
if %errorlevel% equ 0 (
    echo JProxy is running. Try to kill ......
    taskkill /F /IM %jproxy%
    echo JProxy was killed.
) else (
    echo JProxy is not running.
)
endlocal
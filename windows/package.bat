@echo off

cd ..
call mvn clean package -Dmaven.test.skip=true -P prod
xcopy ".\target\jproxy.jar" ".\target\windows\"
xcopy ".\target\classes\application.yml" ".\target\windows\config\"
xcopy ".\target\classes\application-prod.yml" ".\target\windows\config\"
xcopy ".\target\classes\database\jproxy.db" ".\target\windows\database\"
xcopy ".\windows\startup.bat" ".\target\windows\"
xcopy ".\windows\startup-daemon.bat" ".\target\windows\"
xcopy ".\windows\shutdown.bat" ".\target\windows\"

echo.
echo location: target/windows
echo.
@echo off

echo.
echo 1. Runable Fat.jar ==^> target/jproxy.jar
echo 2. Docker Image    ==^> target/jib-image.tar
echo 3. Docker Image    ==^> Local Docker Image Repo
echo 4. Docker Image    ==^> Remote Docker Image Repo
echo 5. Windows Package ==^> target/windows

echo.
set /p number="Please choose (1-5): "
echo.

if %number% == 1 ( 
  mvn clean package -Dmaven.test.skip=true -P prod -Djib.skip=true
  echo.
  echo location: target/jproxy.jar
) ^
else if %number% == 2 ( 
  mvn clean compile jib:buildTar -Dmaven.test.skip=true -P prod -Djib.from.platforms=linux/amd64
  echo.
  echo location: target/jib-image.tar
  echo install command: docker load --input jib-mages.tar
) ^
else if %number% == 3 ( 
  mvn clean compile jib:build -Dmaven.test.skip=true -P prod -Djib.to.image=192.168.6.14:5000/luckypuppy514/jproxy
  echo.
  echo docker pull localhost:5000/luckypuppy514/jproxy:latest
) ^
else if %number% == 4 ( 
  mvn clean compile jib:build -Dmaven.test.skip=true -P prod -Djib.to.image=docker.io/luckypuppy514/jproxy
  echo.
  echo docker pull luckypuppy514/jproxy:latest
) ^
else if %number% == 5 ( 
  mvn clean package -Dmaven.test.skip=true -P prod -Djib.skip=true
  xcopy "target\jproxy.jar" "target\windows\"
  xcopy "target\classes\application.yml" "target\windows\config\"
  xcopy "target\classes\application-prod.yml" "target\windows\config\"
  xcopy "target\classes\database\jproxy.db" "target\windows\database\"
  xcopy "windows\startup.bat" "target\windows\"
  xcopy "windows\startup.vbs" "target\windows\"
  xcopy "windows\shutdown.bat" "target\windows\"
  xcopy "windows\redis\redis.windows.conf" "target\windows\redis\"
  xcopy "windows\redis\redis-server.exe" "target\windows\redis\"
  echo.
  echo location: target/windows
) ^
else (
  echo input is invalid
)
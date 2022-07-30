@echo off 
call mvn clean package -Dmaven.test.skip=true -P jar
del /f /s /q "release\jar\config\*"
del /f /s /q "release\jar\jproxy.jar"

xcopy "target\classes\application.yml" "release\jar\config\"
xcopy "target\classes\logback-spring.xml" "release\jar\config\"
xcopy "target\classes\db\sqlite.db" "release\jar\config\"
xcopy "target\jproxy.jar" "release\jar\"


call mvn clean package -Dmaven.test.skip=true -P docker

del /f /s /q "release\docker\config\*"
del /f /s /q "release\docker\jproxy.jar"

xcopy "target\classes\logback-spring.xml" "release\docker\config\"
xcopy "target\classes\db\sqlite.db" "release\docker\config\"
xcopy "target\jproxy.jar" "release\docker\"


call mvn clean package -Dmaven.test.skip=true -P server

del /f /s /q "release\server\config\*"
del /f /s /q "release\server\jproxy.jar"

xcopy "target\classes\logback-spring.xml" "release\server\config\"
xcopy "target\classes\db\sqlite.db" "release\server\config\"
xcopy "target\jproxy.jar" "release\server\"

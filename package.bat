@echo off 
call mvn clean package -Dmaven.test.skip=true
del /f /s /q "release\*.yml"
del /f /s /q "release\*.xml"
xcopy "target\jproxy*.jar" "release\jproxy.jar"
xcopy "target\classes\*.yml" "release\"
xcopy "target\classes\*.xml" "release\"

del /f /s /q "Z:\cinema\jproxy\*"
xcopy "release\*" "Z:\cinema\jproxy\"
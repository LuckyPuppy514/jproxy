@echo off 
call mvn clean package -Dmaven.test.skip=true
xcopy "target\jproxy*.jar" "release\jproxy.jar"
copy "target\classes\application.yml" "release\application.yml"
copy "target\classes\application-prod.yml" "release\application-prod.yml"
copy "target\classes\logback-spring.xml" "release\logback-spring.xml"
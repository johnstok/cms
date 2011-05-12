@ echo off
cls
call mvn clean install
pause
cls
echo  --------------------
echo   CCC7 Deploy Script 
echo  --------------------
cd core-domain
call mvn jboss:deploy
cd ..
cd business-services
call mvn jboss:deploy
cd ..
cd services-ejb3
call mvn jboss:deploy
cd ..
cd content-server
call mvn jboss:deploy
cd ..
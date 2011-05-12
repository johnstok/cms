rem @ echo off
cls
set selenium_version=1.0-SNAPSHOT
echo  ---------------------------
echo   Selenium Server [V. %selenium_version%]
echo  ---------------------------
%SystemDrive%
cd \
cd %USERPROFILE%\.m2\repository\org\openqa\selenium\server\selenium-server\%selenium_version%
java -jar selenium-server-%selenium_version%-standalone.jar -port 5555
exit
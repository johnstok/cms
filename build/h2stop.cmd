@ echo off
cls
set h2_version=1.1.118
echo  -----------------
echo   H2 [V. %h2_version%]
echo  -----------------
%SystemDrive%
cd \
cd %USERPROFILE%\.m2\repository\com\h2database\h2\%h2_version%
java -cp h2-%h2_version%.jar org.h2.tools.Server -tcpShutdown  tcp://localhost:9092 

@echo off
cd content-server
call mvn selenium:start-server -Dport=5555
exit
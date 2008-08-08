@echo off
cd content-server
call mvn selenium:stop-server -Dport=5555
exit
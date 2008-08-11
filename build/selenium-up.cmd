@echo off
cd modules\content-server
call mvn selenium:start-server -Dport=5555
exit
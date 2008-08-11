@echo off
cd modules\content-server
call mvn selenium:stop-server -Dport=5555
exit
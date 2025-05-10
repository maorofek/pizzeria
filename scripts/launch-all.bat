@echo off
cd /d C:\kafka\kafka_2.13-3.9.0

start cmd /k "bin\windows\zookeeper-server-start.bat config\zookeeper.properties"
timeout /t 5 >nul
start cmd /k "bin\windows\kafka-server-start.bat config\server.properties"
timeout /t 5 >nul

REM Now create topics in this window
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-received
REM …etc…

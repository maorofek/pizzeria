@echo off
REM — Start Kafka broker
cd /d C:\kafka\kafka_2.13-3.9.0
bin\windows\kafka-server-start.bat config\server.properties
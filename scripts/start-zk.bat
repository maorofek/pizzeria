@echo off
REM — Start ZooKeeper
cd /d C:\kafka\kafka_2.13-3.9.0
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

@echo off
cd /d C:\kafka\kafka_2.13-3.9.0

echo -- verifying ports --
netstat -ano ^| findstr :2181
netstat -ano ^| findstr :9092

echo -- existing topics --
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

echo -- creating pipeline topics --
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-received
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-in-oven
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-ready
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-out-for-delivery

echo -- final topic list --
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

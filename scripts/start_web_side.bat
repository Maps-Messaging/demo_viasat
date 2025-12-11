@echo off
start "common-request" mvn -q -Dexec.mainClass=io.mapsmessaging.client.CommonRequestClient exec:java
start "common-request" mvn -q -Dexec.mainClass=io.mapsmessaging.control.CentralControl exec:java

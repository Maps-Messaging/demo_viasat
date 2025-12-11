@echo off
start "weather" mvn -q -Dexec.mainClass=io.mapsmessaging.weather.WeatherGeneratorDemo exec:java
start "common-response" mvn -q -Dexec.mainClass=io.mapsmessaging.client.CommonResponseClient exec:java

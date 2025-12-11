#!/bin/sh
nohup mvn -q -Dexec.mainClass=io.mapsmessaging.weather.WeatherGeneratorDemo exec:java &
nohup mvn -q -Dexec.mainClass=io.mapsmessaging.client.CommonResponseClient exec:java &

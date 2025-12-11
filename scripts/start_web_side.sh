#!/bin/sh
nohup mvn -q -Dexec.mainClass=io.mapsmessaging.client.CommonRequestClient exec:java &
nohup mvn -q -Dexec.mainClass=io.mapsmessaging.control.CentralControl exec:java &
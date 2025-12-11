#!/bin/sh
nohup mvn -q -Dexec.mainClass=io.mapsmessaging.client.CommonRequestClient exec:java &
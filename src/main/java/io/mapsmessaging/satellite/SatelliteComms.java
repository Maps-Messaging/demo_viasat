/*
 *
 *  Copyright [ 2020 - 2024 ] Matthew Buckton
 *  Copyright [ 2024 - 2025 ] MapsMessaging B.V.
 *
 *  Licensed under the Apache License, Version 2.0 with the Commons Clause
 *  (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      https://commonsclause.com/
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.mapsmessaging.satellite;

import io.mapsmessaging.satellite.gateway.InmarsatMockServer;
import io.mapsmessaging.satellite.modem.ModemResponder;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SatelliteComms {

  private final InmarsatMockServer mockWebServer;
  private final ModemResponder modemResponder;

  public SatelliteComms(String comPort, String port) throws IOException {
    Queue<byte[]> modemMessages = new ConcurrentLinkedQueue<>();
    Queue<byte[]> satelliteMessages = new ConcurrentLinkedQueue<>();
    mockWebServer = new InmarsatMockServer(modemMessages, satelliteMessages, Integer.parseInt(port));
    modemResponder =new ModemResponder(satelliteMessages, modemMessages, comPort);
  }

  public void start(){
    modemResponder.start();
    mockWebServer.start();
  }

  public void stop(){
    modemResponder.stop();
    mockWebServer.stop();
  }

  public static void main(String[] args) throws IOException {
    if(args.length != 2){
      System.err.println("Usage: SatelliteComms <serial port> <http port>");
      System.err.println("Options are:\n" +
          "-DMODEM_LOG_MESSAGES=(true|false)\n" +
          "-DOGX=(true|false) if false then IDP modem is used\n" +
          "-DINMARSAT_MOCK_LOG_HEADERS=(true/false)\n" +
          "-DINMARSAT_MOCK_LOG_BODIES=(true/false)");
    }
    else {
      SatelliteComms satelliteComms = new SatelliteComms(args[0], args[1]);
      satelliteComms.start();
    }
  }
}

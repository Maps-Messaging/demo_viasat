package io.mapsmessaging.client;

import common.MqttClientConnection;
import io.mapsmessaging.client.listeners.DirectoryResponse;
import org.eclipse.paho.client.mqttv3.MqttException;


public class CommonResponseClient {

  private MqttClientConnection client;

  public CommonResponseClient() throws MqttException {
    client = new MqttClientConnection("tcp://localhost:1884", "CommonResponseClient");
    client.subscribe("/incoming/24/3", new DirectoryResponse(client));
  }


  public static void main(String[] args) throws MqttException {
    CommonResponseClient responseClient = new CommonResponseClient();
  }
}

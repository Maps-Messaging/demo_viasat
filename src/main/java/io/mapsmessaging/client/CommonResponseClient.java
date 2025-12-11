package io.mapsmessaging.client;

import common.MqttClientConnection;
import io.mapsmessaging.client.listeners.DirectoryResponse;
import org.eclipse.paho.client.mqttv3.MqttException;

import static common.Configuration.MODEM_MQTT_URL;


public class CommonResponseClient {

  private final MqttClientConnection client;

  public CommonResponseClient() throws MqttException {
    client = new MqttClientConnection(MODEM_MQTT_URL, CommonResponseClient.class.getSimpleName());
    client.subscribe("/incoming/24/3", new DirectoryResponse(client));
  }


  public static void main(String[] args) throws MqttException {
    CommonResponseClient responseClient = new CommonResponseClient();
  }
}

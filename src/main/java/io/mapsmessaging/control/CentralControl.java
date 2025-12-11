package io.mapsmessaging.control;

import common.MqttClientConnection;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static common.Configuration.WEB_MQTT_URL;

public class CentralControl {

  private final MqttClientConnection client;

  public CentralControl() throws MqttException, InterruptedException {
    client = new MqttClientConnection(WEB_MQTT_URL, CentralControl.class.getSimpleName());
    while(true){
      Thread.sleep(60000);
      sendBroadcastToAll();
      Thread.sleep(60000);
      sendToSpecific("00000000SKYEE3D", "Time is "+ LocalDateTime.now());
    }
  }

  public void sendBroadcastToAll() throws MqttException {
    client.publish("/inmarsat/broadcast", "Broadcast Event".getBytes(StandardCharsets.UTF_8));
  }

  public void sendToSpecific(String deviceId, String message) throws MqttException {
    client.publish("/outgoing/"+deviceId, message.getBytes(StandardCharsets.UTF_8));
  }

  public static void main(String[] args) throws MqttException, InterruptedException {
    new CentralControl();
  }
}

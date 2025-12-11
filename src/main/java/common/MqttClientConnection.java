package common;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientConnection {

  private MqttClient client;


  public MqttClientConnection(String url, String clientId) throws MqttException {
    MqttConnectOptions options = getOptions();
    client = new MqttClient(url, clientId, new MemoryPersistence());
    client.connect(options);
  }


  public MqttConnectOptions getOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setMqttVersion(4); // 3.1.1
    options.setExecutorServiceTimeout(20000);
    options.setConnectionTimeout(20000);
    options.setCleanSession(true);
    options.setAutomaticReconnect(true);
    return options;
  }
  public void publish(String s, byte[] message) throws MqttException {
    MqttMessage mqttMessage = new MqttMessage(message);
    publish(s, mqttMessage);
  }

  public void publish(String s, MqttMessage message) throws MqttException {
    client.publish(s, message);
  }

  public void subscribe(String s, IMqttMessageListener listener) throws MqttException {
    client.subscribe(s, listener );
  }
}

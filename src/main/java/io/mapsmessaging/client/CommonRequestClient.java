package io.mapsmessaging.client;


import common.MqttClientConnection;
import io.mapsmessaging.client.messages.DirectoryRequest;
import io.mapsmessaging.client.messages.ModemPositionRequests;
import io.mapsmessaging.client.messages.Requests;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static common.Configuration.WEB_MQTT_URL;

public class CommonRequestClient {
  private final MqttClientConnection client;
  private String deviceId;


  public CommonRequestClient() throws MqttException, InterruptedException {
    client = new MqttClientConnection(WEB_MQTT_URL, CommonRequestClient.class.getSimpleName());
    deviceId = "00000000SKYEE3D";

    while(true){
      sendDirectoryRequest();
      Thread.sleep(30000);
      sendPositionRequest();
      Thread.sleep(30000);
    }
  }

  public void sendDirectoryRequest() throws MqttException {
    sendRequest(new DirectoryRequest("."));
  }

  public void sendPositionRequest() throws MqttException {
    sendRequest(new ModemPositionRequests());
  }


  private void sendRequest(Requests request) throws MqttException {
    int min = request.getMin();
    int sin = request.getSin();
    MqttMessage message = new MqttMessage();
    message.setQos(0);
    message.setRetained(false);
    message.setPayload(request.getRequest());
    client.publish("/"+deviceId+"/common/out/"+sin+"/"+min, message);
  }



  public static void main(String[] args) throws MqttException, InterruptedException {
    try {
      CommonRequestClient commonRequestClient = new CommonRequestClient();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    System.exit(0);
  }


}

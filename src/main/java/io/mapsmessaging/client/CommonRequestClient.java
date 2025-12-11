package io.mapsmessaging.client;


import common.MqttClientConnection;
import io.mapsmessaging.client.messages.DirectoryRequest;
import io.mapsmessaging.client.messages.ModemPositionRequests;
import io.mapsmessaging.client.messages.Requests;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CommonRequestClient {
  private MqttClientConnection client;
  private String deviceId;


  public CommonRequestClient() throws MqttException, InterruptedException {
    client = new MqttClientConnection("tcp://localhost:1883", "CommonRequestClient");
    deviceId = "00000000SKYEE3D";

    for(int x=0;x< 10;x++){
      Thread.sleep(30000);
      sendPositionRequest();
      Thread.sleep(30000);
      sendDirectoryRequest();

    }
  }

  public void sendDirectoryRequest() throws MqttException {
    sendRequest(new DirectoryRequest("."));
  }

  public void sendPositionRequest() throws MqttException {
    sendRequest(new ModemPositionRequests());
  }


  private void sendRequest(Requests request) throws MqttException {

    MqttMessage message = new MqttMessage();
    message.setQos(0);
    message.setRetained(false);
    message.setPayload(request.getRequest());
    client.publish("/outgoing/"+deviceId+"/request", message);

  }



  public static void main(String[] args) throws MqttException, InterruptedException {
    CommonRequestClient commonRequestClient = new CommonRequestClient();
    System.exit(0);
  }


}
